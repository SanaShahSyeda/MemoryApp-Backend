package com.spring.memory.dto.memory;

import com.spring.memory.enumeration.MemoryStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import com.spring.memory.dto.tag.TagDTO;

@Data
public class UpdateMemoryDTO {
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    private String content;
    private String[] mediaUrl;
    private MemoryStatus status;
    private Integer boardId;
    private Set<String> tagTitles;
    private MultipartFile[] mediaFiles; // NEW uploads
    private Boolean clearMedia; // Explicit delete flag
    private Set<TagDTO> tags; // accept tags array (backward-compatible with tagIds)
}
