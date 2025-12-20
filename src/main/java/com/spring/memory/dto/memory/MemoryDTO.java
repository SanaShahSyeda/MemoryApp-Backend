package com.spring.memory.dto.memory;

import com.spring.memory.dto.board.BoardDTO;
import com.spring.memory.dto.tag.TagDTO;
import com.spring.memory.enumeration.MemoryStatus;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.Set;

/**
 * DTO for {@link com.spring.memory.entity.Memory}
 */
@Data
@Builder
public class MemoryDTO {
    private final Integer id;
    private final String title;
    private final String content;
    private final String[] mediaUrl;
    private final MemoryStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Set<TagDTO> tags;
    private final BoardDTO board;
}