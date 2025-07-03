package com.spring.memory.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MemoryTagId implements Serializable {

    private static final long serialVersionUID = 2035795955284574190L;

    @NotNull
    @Column(name = "memory_id")
    private Integer memoryId;

    @NotNull
    @Column(name = "tag_id")
    private Integer tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MemoryTagId entity = (MemoryTagId) o;
        return Objects.equals(this.tagId, entity.tagId) &&
                Objects.equals(this.memoryId, entity.memoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, memoryId);
    }

}