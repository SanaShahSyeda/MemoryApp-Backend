package com.spring.memory.dto.board;

import com.spring.memory.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class BoardDTO {
    private final Integer id;
    private final String title;
    private final String description;
    private final String coverPhoto;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final UserDTO userDto;
}