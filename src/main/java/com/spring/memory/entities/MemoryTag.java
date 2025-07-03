package com.spring.memory.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "memory_tags")
public class MemoryTag {
    @EmbeddedId
    private MemoryTagId id;

    @MapsId("memoryId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "memory_id")
    private Memory memory;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id")
    private Tag tag;

}