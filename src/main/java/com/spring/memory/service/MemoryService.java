package com.spring.memory.service;

import com.spring.memory.dto.memory.UpdateMemoryDTO;
import com.spring.memory.dto.memory.CreateMemoryDTO;
import com.spring.memory.dto.memory.MemoryDTO;
import com.spring.memory.entity.Board;
import com.spring.memory.entity.Memory;
import com.spring.memory.entity.Tag;
import com.spring.memory.exception.BadRequestException;
import com.spring.memory.mapper.MemoryMapper;
import com.spring.memory.util.FetchCloudinaryPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import com.spring.memory.repository.BoardRepository;
import com.spring.memory.repository.MemoryRepository;
import com.spring.memory.repository.TagRepository;

import java.time.Instant;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemoryService {
    private final MemoryRepository memoryRepository;
    private final BoardRepository boardRepository;
    private final TagRepository tagRepository;
    private final MemoryMapper memoryMapper;
    private final CloudinaryService cloudinaryService;

    public MemoryService(MemoryRepository memoryRepository,
            BoardRepository boardRepository,
            MemoryMapper memoryMapper, TagRepository tagRepository,
            CloudinaryService cloudinaryService) {
        this.memoryRepository = memoryRepository;
        this.boardRepository = boardRepository;
        this.memoryMapper = memoryMapper;
        this.tagRepository = tagRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public MemoryDTO createMemory(CreateMemoryDTO req, Integer userId, Integer boardId) {

        Board board = boardRepository.findByIdAndUserId(boardId, userId)
                .orElseThrow(() -> new BadRequestException("Board not found or not owned by user"));

        // build tags set from titles
        Set<Tag> tags = new LinkedHashSet<>();

        if (req.getTagTitles() != null && !req.getTagTitles().isEmpty()) {
            for (String title : req.getTagTitles()) {
                if (title == null || title.trim().isEmpty())
                    continue;
                String t = title.trim();
                Optional<Tag> existing = tagRepository.findByTitle(t);
                if (existing.isPresent()) {
                    tags.add(existing.get());
                } else {
                    Tag newTag = Tag.builder().title(t).build();
                    Tag savedTag = tagRepository.save(newTag);
                    tags.add(savedTag);
                }
            }
        }

        if (tags.isEmpty()) {
            throw new BadRequestException("At least one tag (title) is required");
        }

        // collect media URLs either from uploaded files or from provided URLs
        java.util.List<String> collectedUrls = new java.util.ArrayList<>();

        MultipartFile[] files = req.getMediaFiles();
        if (files != null && files.length > 0) {
            for (MultipartFile f : files) {
                if (f == null || f.isEmpty())
                    continue;
                var resp = cloudinaryService.upload(f);
                if (resp != null && resp.getUrl() != null) {
                    collectedUrls.add(resp.getUrl());
                }
            }
        }

        Memory memory = Memory.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .mediaUrl(collectedUrls.isEmpty() ? null : String.join(", ", collectedUrls))
                .status(req.getStatus())
                .createdAt(Instant.now())
                .board(board)
                .tags(tags)
                .build();

        Memory saved = memoryRepository.save(memory);
        return memoryMapper.toDto(saved);
    }

    @Transactional
    public MemoryDTO updateMemory(
            Integer boardId,
            Integer memoryId,
            Integer userId,
            UpdateMemoryDTO req) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new BadRequestException("Memory not found"));

        if (memory.getBoard() == null ||
                memory.getBoard().getUser() == null ||
                !memory.getBoard().getUser().getId().equals(userId) ||
                !memory.getBoard().getId().equals(boardId)) {
            throw new BadRequestException("Memory not found or not owned by user");
        }

        // ---- Simple fields ----
        if (req.getTitle() != null && !req.getTitle().isBlank()) {
            memory.setTitle(req.getTitle());
        }
        if (req.getContent() != null) {
            memory.setContent(req.getContent());
        }
        if (req.getStatus() != null) {
            memory.setStatus(req.getStatus());
        }

        // ---- Move board ----
        if (req.getBoardId() != null &&
                !req.getBoardId().equals(memory.getBoard().getId())) {

            Board newBoard = boardRepository
                    .findByIdAndUserId(req.getBoardId(), userId)
                    .orElseThrow(() -> new BadRequestException("Target board not found or not owned by user"));

            memory.setBoard(newBoard);
        }

        // ---- Tags (replace semantics) ----
        if (req.getTagTitles() != null) {
            memory.getTags().clear();
            for (String tagTitle : req.getTagTitles()) {
                tagRepository.findByTitle(tagTitle)
                        .ifPresent(memory.getTags()::add);
            }
        }

        // ---- Media handling ----
        if (Boolean.TRUE.equals(req.getClearMedia())) {
            if (memory.getMediaUrl() != null && !memory.getMediaUrl().isBlank()) {
                for (String url : memory.getMediaUrl().split(",")) {
                    String trimmed = url.trim();
                    if (!trimmed.isEmpty()) {
                        String publicId = FetchCloudinaryPublicId.extractPublicId(trimmed);
                        cloudinaryService.delete(publicId);
                    }
                }
            }
            memory.setMediaUrl(null);
        }

        if (req.getMediaFiles() != null && req.getMediaFiles().length > 0) {
            // delete existing media first
            if (memory.getMediaUrl() != null && !memory.getMediaUrl().isBlank()) {
                for (String url : memory.getMediaUrl().split(",")) {
                    String trimmed = url.trim();
                    if (!trimmed.isEmpty()) {
                        String publicId = FetchCloudinaryPublicId.extractPublicId(trimmed);
                        cloudinaryService.delete(publicId);
                    }
                }
            }

            List<String> uploadedUrls = new ArrayList<>();
            for (MultipartFile file : req.getMediaFiles()) {
                var resp = cloudinaryService.upload(file);
                if (resp != null && resp.getUrl() != null) {
                    uploadedUrls.add(resp.getUrl());
                }
            }

            memory.setMediaUrl(String.join(", ", uploadedUrls));
        }

        memory.setUpdatedAt(Instant.now());
        Memory saved = memoryRepository.save(memory);

        return memoryMapper.toDto(saved);
    }

    @Transactional
    public MemoryDTO deleteMemory(Integer memoryId, Integer userId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new BadRequestException("Memory not found"));

        if (memory.getBoard() == null || memory.getBoard().getUser() == null ||
                !memory.getBoard().getUser().getId().equals(userId)) {
            throw new BadRequestException("Memory not found or not owned by user");
        }

        // clear associations so related rows in memory_tags are removed
        memory.getTags().clear();

        String files = memory.getMediaUrl();
        if (files != null && !files.isBlank()) {
            String[] urls = files.split(",");
            for (String f : urls) {
                String url = f == null ? null : f.trim();
                if (url == null || url.isEmpty())
                    continue;
                String publicId = FetchCloudinaryPublicId.extractPublicId(url);
                cloudinaryService.delete(publicId);
            }
        }
        MemoryDTO deletedMemory = memoryMapper.toDto(memory);
        memoryRepository.delete(memory);
        return deletedMemory;
    }

    public Page<MemoryDTO> getAllMemoriesOfBoard(Integer id, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Memory> memoryPage = memoryRepository.findByBoardId(id, pageable);
        return memoryPage.map(memoryMapper::toDto);
    }

    public MemoryDTO getMemoryById(Integer memoryId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new BadRequestException("Memory not found"));

        // map to DTO here so related lazy associations are initialized by the mapper
        return memoryMapper.toDto(memory);
    }
}
