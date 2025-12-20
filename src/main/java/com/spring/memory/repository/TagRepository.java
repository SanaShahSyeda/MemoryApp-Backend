package com.spring.memory.repository;

import com.spring.memory.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TagRepository extends JpaRepository<Tag, Integer> {
    java.util.Optional<Tag> findByTitle(String title);
}