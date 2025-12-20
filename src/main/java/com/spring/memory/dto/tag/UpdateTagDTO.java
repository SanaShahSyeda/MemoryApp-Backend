package com.spring.memory.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTagDTO {
    @NotBlank(message = "Tag title is required")
    @Size(max = 50, message = "Tag title must not exceed 50 characters")
    private String title;
}
