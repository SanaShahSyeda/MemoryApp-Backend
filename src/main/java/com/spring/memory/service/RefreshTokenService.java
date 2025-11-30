package com.spring.memory.service;

import com.spring.memory.entity.RefreshToken;
import com.spring.memory.entity.User;
import com.spring.memory.exception.InvalidTokenException;
import com.spring.memory.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final long refreshTokenDurationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
            @Value("${jwt.refreshExpiration:2592000000}") long refreshTokenDurationMs) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenDurationMs = refreshTokenDurationMs;
    }

    private String sha256Hex(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash token", e);
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        String hash = sha256Hex(token);
        return refreshTokenRepository.findByTokenHash(hash);
    }

    /**
     * Create a refresh token for the given user and return the raw token string
     * (not stored raw)
     */
    @Transactional
    public String createRefreshToken(User user) {
        // remove old tokens for user to enforce single valid refresh token
        refreshTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        String tokenHash = sha256Hex(token);
        Instant now = Instant.now();
        RefreshToken rt = RefreshToken.builder()
                .tokenHash(tokenHash)
                .user(user)
                .createdAt(now)
                .expiryDate(now.plusMillis(refreshTokenDurationMs))
                .build();
        refreshTokenRepository.save(rt);
        return token;
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteById(token.getId());
            throw new InvalidTokenException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUser(User user) {
        return refreshTokenRepository.deleteByUser(user);
    }
}
