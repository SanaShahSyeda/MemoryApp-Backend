package com.spring.memory.mapper;

import com.spring.memory.dto.board.BoardDTO;
import com.spring.memory.dto.memory.MemoryDTO;
import com.spring.memory.dto.tag.TagDTO;
import com.spring.memory.entity.Memory;
import com.spring.memory.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MemoryMapper {
    private final TagMapper tagMapper;
    private final BoardMapper boardMapper;

    public MemoryMapper(TagMapper tagMapper, BoardMapper boardMapper) {
        this.tagMapper = tagMapper;
        this.boardMapper = boardMapper;
    }

    public MemoryDTO toDto(Memory memory) {
        if (memory == null) {
            return null;
        }

        Set<TagDTO> tagDtos = new HashSet<>();
        if (memory.getTags() != null) {
            for (Tag t : memory.getTags()) {
                tagDtos.add(tagMapper.toDto(t));
            }
        }

        BoardDTO boardDto = null;
        if (memory.getBoard() != null) {
            boardDto = boardMapper.toDto(memory.getBoard());
        }

        String[] mediaUrls = null;
        if (memory.getMediaUrl() != null && !memory.getMediaUrl().isBlank()) {
            String[] parts = memory.getMediaUrl().split(",");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }
            mediaUrls = parts;
        }

        return MemoryDTO.builder()
                .id(memory.getId())
                .title(memory.getTitle())
                .content(memory.getContent())
                .mediaUrl(mediaUrls)
                .status(memory.getStatus())
                .createdAt(memory.getCreatedAt())
                .updatedAt(memory.getUpdatedAt())
                .tags(tagDtos)
                .board(boardDto)
                .build();
    }

    public Memory toEntity(MemoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Memory memory = Memory.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .mediaUrl(dto.getMediaUrl() == null ? null : String.join(", ", dto.getMediaUrl()))
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();

        if (dto.getTags() != null) {
            Set<Tag> tags = new HashSet<>();
            for (TagDTO t : dto.getTags()) {
                tags.add(tagMapper.toEntity(t));
            }
            memory.setTags(tags);
        }

        if (dto.getBoard() != null) {
            memory.setBoard(boardMapper.toEntity(dto.getBoard()));
        }
        return memory;
    }
}
