package com.spring.memory.repositories;

import com.spring.memory.entities.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Integer> {
}