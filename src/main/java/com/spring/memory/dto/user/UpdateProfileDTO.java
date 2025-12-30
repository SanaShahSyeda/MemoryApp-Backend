package com.spring.memory.dto.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileDTO {
    private MultipartFile avatar;
    private Boolean removeAvatar;
}
