package com.spring.memory.service;

import com.spring.memory.dto.DeleteResponseDTO;
import com.spring.memory.dto.tag.CreateTagDTO;
import com.spring.memory.dto.tag.TagDTO;
import com.spring.memory.dto.tag.UpdateTagDTO;
import com.spring.memory.entity.Tag;
import com.spring.memory.entity.Memory;
import com.spring.memory.exception.BadRequestException;
import com.spring.memory.mapper.TagMapper;
import com.spring.memory.repository.TagRepository;
import com.spring.memory.repository.MemoryRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepo;
    private final TagMapper tagMapper;
    private final MemoryRepository memoryRepository;

    public TagService(TagRepository tagRepo, TagMapper tagMapper, MemoryRepository memoryRepository) {
        this.tagRepo = tagRepo;
        this.tagMapper = tagMapper;
        this.memoryRepository = memoryRepository;
    }

    public TagDTO createTag(CreateTagDTO req) {
        Tag tag = new Tag();
        tag.setTitle(req.getTitle());
        Tag saved = tagRepo.save(tag);
        return tagMapper.toDto(saved);
    }

    public List<TagDTO> getAllTags() {
        return tagRepo.findAll()
                .stream()
                .map(tagMapper::toDto)
                .toList();
    }

    public TagDTO getTagById(String id) {
        Tag tag = tagRepo.findById(Integer.valueOf(id))
                .orElseThrow(() -> new BadRequestException("Tag not found"));
        return tagMapper.toDto(tag);
    }
    public TagDTO updateTag(String id, @Valid UpdateTagDTO req) {
        Tag tag = tagRepo.findById(Integer.valueOf(id))
                .orElseThrow(() -> new BadRequestException("Tag doesnot exist"));

        tag.setTitle(req.getTitle());
        Tag saved = tagRepo.save(tag);
        return tagMapper.toDto(saved);
    }

    public DeleteResponseDTO<TagDTO> deleteTagById(String id) {
        Integer tid = Integer.valueOf(id);
        Tag tag = tagRepo.findById(tid)
                .orElseThrow(() -> new BadRequestException("Tag doesnot exist"));

        // Find memories that contain this tag and remove the association
        List<Memory> memories = memoryRepository.findByTags_Id(tid);
        for (Memory m : memories) {
            m.getTags().removeIf(t -> t.getId().equals(tid));
            memoryRepository.save(m);
        }
        tagRepo.delete(tag);
        return new DeleteResponseDTO<>("Tag deleted successfully", tagMapper.toDto(tag));
    }
}
