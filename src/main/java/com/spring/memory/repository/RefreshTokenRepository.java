package com.spring.memory.repository;

import com.spring.memory.entity.RefreshToken;
import com.spring.memory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    int deleteByUser(User user);
}
