package com.spring.memory.repository;

import com.spring.memory.entity.MemoryTag;
import com.spring.memory.entity.MemoryTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryTagRepository extends JpaRepository<MemoryTag, MemoryTagId> {
}