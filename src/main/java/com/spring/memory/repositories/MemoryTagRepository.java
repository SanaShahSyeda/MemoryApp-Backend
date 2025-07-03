package com.spring.memory.repositories;

import com.spring.memory.entities.MemoryTag;
import com.spring.memory.entities.MemoryTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryTagRepository extends JpaRepository<MemoryTag, MemoryTagId> {
}