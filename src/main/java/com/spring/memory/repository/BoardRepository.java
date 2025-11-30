package com.spring.memory.repository;

import com.spring.memory.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findAllByUserId(Integer userId, Pageable pageable);
    Optional<Board> findByIdAndUserId(Integer id, Integer userId);
}