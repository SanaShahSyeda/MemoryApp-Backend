package com.spring.memory.service;

import com.spring.memory.dto.board.BoardDTO;
import com.spring.memory.dto.board.CreateBoardDTO;
import com.spring.memory.dto.board.UpdateBoardRequestDTO;
import com.spring.memory.entity.Board;
import com.spring.memory.entity.User;
import com.spring.memory.exception.BadRequestException;
import com.spring.memory.mapper.BoardMapper;
import com.spring.memory.repository.BoardRepository;
import com.spring.memory.repository.UserRepository;
import com.spring.memory.util.FetchCloudinaryPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardMapper boardMapper;
    private final CloudinaryService cloudinaryService;

    public BoardService(BoardRepository boardRepository,
                        UserRepository userRepository,
                        BoardMapper boardMapper,
                        CloudinaryService cloudinaryService) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.boardMapper = boardMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public BoardDTO createBoard(CreateBoardDTO dto, Integer currentUserId) {

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        Board board = boardMapper.toEntity(dto, user);

        // Upload actual file if present
        MultipartFile file = dto.getCoverPhoto();

        if (file != null && !file.isEmpty()) {
            String url = cloudinaryService.upload(file).getUrl();
            board.setCoverPhoto(url);
        }

        board.setCreatedAt(Instant.now());
        Board saved = boardRepository.save(board);
        return boardMapper.toDto(saved);
    }

    @Transactional
    public BoardDTO updateBoard(Integer id, UpdateBoardRequestDTO req, Integer currentUserId) {

        Board board = boardRepository.findByIdAndUserId(id, currentUserId)
                .orElseThrow(() -> new BadRequestException("Board not found or not owned by user"));

        MultipartFile newFile = req.getCoverPhoto();

        if (newFile != null && !newFile.isEmpty()) {
            String oldCoverPhotoUrl = FetchCloudinaryPublicId.extractPublicId(board.getCoverPhoto());
            cloudinaryService.delete(oldCoverPhotoUrl);
            String url = cloudinaryService.upload(newFile).getUrl();
            req.setCoverPhotoUrl(url);
        }
        boardMapper.patchEntity(board, req);
        Board saved = boardRepository.save(board);
        return boardMapper.toDto(saved);
    }

    @Transactional
    public BoardDTO deleteBoard(Integer id, Integer currentUserId) {
        Board board = boardRepository.findByIdAndUserId(id, currentUserId)
                .orElseThrow(() -> new BadRequestException("Board not found or not owned by user"));

        BoardDTO dto = boardMapper.toDto(board);
        String coverPhotoUrl = FetchCloudinaryPublicId.extractPublicId(board.getCoverPhoto());
        cloudinaryService.delete(coverPhotoUrl);
        boardRepository.delete(board);
        return dto;
    }

    public BoardDTO getBoardById(Integer id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Board not found"));

        return boardMapper.toDto(board);
    }
    
    public Page<BoardDTO> getAllBoards(Integer userId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Board> boardPage = boardRepository.findAllByUserId(userId, pageable);
        return boardPage.map(boardMapper::toDto);
    }
}
