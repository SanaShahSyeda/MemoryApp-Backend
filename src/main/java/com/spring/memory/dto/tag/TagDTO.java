package com.spring.memory.dto.tag;

import lombok.Builder;
import lombok.Data;


/**
 * DTO for {@link com.spring.memory.entity.Tag}
 */
@Data
@Builder
public class TagDTO {
    private final Integer id;
    private final String title;
}