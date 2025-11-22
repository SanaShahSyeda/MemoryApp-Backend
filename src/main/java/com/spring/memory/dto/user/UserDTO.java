package com.spring.memory.dto.user;


import lombok.Data;
import java.time.Instant;

/**
 * DTO for {@link com.spring.memory.entity.User}
 */
@Data
public class UserDTO {
    private final String email;
    private final String avatarUrl;
    private final Instant createdAt;
    private final Instant updatedAt;
}