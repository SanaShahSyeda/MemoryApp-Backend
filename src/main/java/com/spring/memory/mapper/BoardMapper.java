package com.spring.memory.mapper;

import com.spring.memory.dto.board.BoardDTO;
import com.spring.memory.dto.board.CreateBoardDTO;
import com.spring.memory.dto.board.UpdateBoardRequestDTO;
import com.spring.memory.dto.user.UserDTO;
import com.spring.memory.entity.Board;
import com.spring.memory.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class BoardMapper {

    public BoardDTO toDto(Board b) {
        if (b == null)
            return null;

        User user = b.getUser();
        UserDTO userDto = null;
        if (user != null) {
            userDto = new UserDTO(
                    user.getEmail(),
                    user.getAvatarUrl(),
                    user.getCreatedAt(),
                    user.getUpdatedAt());
        }

        return new BoardDTO(
                b.getId(),
                b.getTitle(),
                b.getDescription(),
                b.getCoverPhoto(),
                b.getCreatedAt(),
                b.getUpdatedAt(),
                userDto);
    }

    public Board toEntity(CreateBoardDTO dto, User user) {
        if (dto == null)
            return null;
        return Board.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .coverPhoto(null)
                .user(user)
                .build();
    }

    public void patchEntity(Board board, UpdateBoardRequestDTO req) {

        if (req.getTitle() != null)
            board.setTitle(req.getTitle());

        if (req.getDescription() != null)
            board.setDescription(req.getDescription());

        if (req.getCoverPhotoUrl() != null)
            board.setCoverPhoto(req.getCoverPhotoUrl());

        // removePhoto takes precedence
        if (Boolean.TRUE.equals(req.getRemovePhoto())) {
            board.setCoverPhoto(null);
        } else if (req.getCoverPhotoUrl() != null) {
            board.setCoverPhoto(req.getCoverPhotoUrl());
        }




        board.setUpdatedAt(Instant.now());
    }

}
