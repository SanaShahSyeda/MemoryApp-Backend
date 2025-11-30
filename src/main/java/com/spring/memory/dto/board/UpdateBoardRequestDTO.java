package com.spring.memory.dto.board;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateBoardRequestDTO {
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private MultipartFile coverPhoto;

    private String coverPhotoUrl;

    private Boolean removePhoto;
}
