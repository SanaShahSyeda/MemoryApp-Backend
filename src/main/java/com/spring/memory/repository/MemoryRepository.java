package com.spring.memory.repository;

import com.spring.memory.entity.Memory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemoryRepository extends JpaRepository<Memory, Integer> {
    Page<Memory> findByBoardId(Integer id, Pageable pageable);
    List<Memory> findByTags_Id(Integer tid);
}