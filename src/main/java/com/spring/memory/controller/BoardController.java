package com.spring.memory.controller;

import com.spring.memory.dto.DeleteResponseDTO;
import com.spring.memory.dto.board.BoardDTO;
import com.spring.memory.dto.board.CreateBoardDTO;
import com.spring.memory.dto.board.UpdateBoardRequestDTO;
import com.spring.memory.entity.CustomUserDetails;
import com.spring.memory.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {
    // Board endpoints controller (no-op comment to create separate commit)
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDTO> createBoard(
            @Valid @ModelAttribute CreateBoardDTO req,
            @AuthenticationPrincipal CustomUserDetails user) {
        BoardDTO dto = boardService.createBoard(req, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDTO> updateBoard(
            @PathVariable Integer id,
            @Valid @ModelAttribute UpdateBoardRequestDTO req,
            @AuthenticationPrincipal CustomUserDetails user) {
        BoardDTO dto = boardService.updateBoard(id, req, user.getId());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<DeleteResponseDTO<BoardDTO>> deleteBoard(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails user) {
        BoardDTO dto = boardService.deleteBoard(id, user.getId());
        DeleteResponseDTO<BoardDTO> response =
                new DeleteResponseDTO<>("Board deleted successfully", dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("all")
    public ResponseEntity<Page<BoardDTO>> getAllBoards(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<BoardDTO> boardPage = boardService.getAllBoards(user.getId(), page, limit);
        return ResponseEntity.ok(boardPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Integer id){
        BoardDTO dto = boardService.getBoardById(id);
        return  ResponseEntity.ok(dto);
    }

}