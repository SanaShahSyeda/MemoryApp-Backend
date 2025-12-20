package com.spring.memory.mapper;


import com.spring.memory.dto.tag.TagDTO;
import com.spring.memory.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagDTO toDto(Tag tag) {
        if (tag == null) {
            return null;
        }

        return TagDTO.builder()
                .id(tag.getId())
                .title(tag.getTitle())
                .build();
    }

    public Tag toEntity(TagDTO dto) {
        if (dto == null) {
            return null;
        }

        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setTitle(dto.getTitle());
        return tag;
    }
}
