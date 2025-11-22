package com.spring.memory.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
    private String refreshToken;
    private UserDTO user;
}
