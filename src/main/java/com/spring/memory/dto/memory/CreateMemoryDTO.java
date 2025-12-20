package com.spring.memory.dto.memory;

import com.spring.memory.enumeration.MemoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateMemoryDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    @NotBlank(message = "Content is required")
    private String content;
    private MultipartFile[] mediaFiles;
    private MemoryStatus status;
    private Set<String> tagTitles;
}
