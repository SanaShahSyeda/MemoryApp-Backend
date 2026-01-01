package com.spring.memory.dto.user;

import com.spring.memory.entity.User;
import lombok.Data;

@Data
public class LogoutResponseDTO {
    private String message;
    private User user;
}
