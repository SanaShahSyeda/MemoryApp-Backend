package com.spring.memory.service;

import com.spring.memory.dto.user.*;
import com.spring.memory.entity.User;
import com.spring.memory.exception.BadRequestException;
import com.spring.memory.exception.InvalidTokenException;
import com.spring.memory.repository.UserRepository;
import com.spring.memory.service.RefreshTokenService;
import com.spring.memory.entity.RefreshToken;
import com.spring.memory.security.JWTTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final JWTTokenProvider jwtTokenProvider;
        private final RefreshTokenService refreshTokenService;

        public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider,
                        RefreshTokenService refreshTokenService) {
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.authenticationManager = authenticationManager;
                this.jwtTokenProvider = jwtTokenProvider;
                this.refreshTokenService = refreshTokenService;
        }

        public UserDTO register(RegisterDTO req) {
                if (userRepository.findByEmail(req.getEmail()).isPresent()) {
                        throw new BadRequestException("User with this email already exists");
                }
                User u = User.builder()
                                .email(req.getEmail())
                                .password(passwordEncoder.encode(req.getPassword()))
                                .createdAt(Instant.now())
                                .build();
                User saved = userRepository.save(u);
                return new UserDTO(saved.getEmail(), saved.getAvatarUrl(), saved.getCreatedAt(), saved.getUpdatedAt());
        }

        public LoginResponseDTO login(LoginDTO req) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

                // Fetch user details
                User user = userRepository.findByEmail(req.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                String token = jwtTokenProvider.generateToken(authentication);

                // create refresh token for the user (returns raw token string)
                String refreshToken = refreshTokenService.createRefreshToken(user);

                UserDTO userDTO = new UserDTO(
                                user.getEmail(),
                                user.getAvatarUrl(),
                                user.getCreatedAt(),
                                user.getUpdatedAt());

                LoginResponseDTO res = LoginResponseDTO.builder()
                                .token(token)
                                .refreshToken(refreshToken)
                                .user(userDTO)
                                .build();
                return res;
        }

        public LoginResponseDTO refreshToken(String refreshTokenStr) {
                RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr)
                                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));

                refreshTokenService.verifyExpiration(refreshToken);

                User user = refreshToken.getUser();

                // generate a new JWT
                String token = jwtTokenProvider.generateTokenFromUsername(user.getEmail());

                // rotate refresh token (returns raw token string)
                String newRefreshToken = refreshTokenService.createRefreshToken(user);

                UserDTO userDTO = new UserDTO(
                                user.getEmail(),
                                user.getAvatarUrl(),
                                user.getCreatedAt(),
                                user.getUpdatedAt());

                return LoginResponseDTO.builder()
                                .token(token)
                                .refreshToken(newRefreshToken)
                                .user(userDTO)
                                .build();
        }

        public LogoutResponseDTO logout(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        refreshTokenService.deleteByUser(user);
        LogoutResponseDTO res = new LogoutResponseDTO();
        res.setMessage("Logged out successfully");
        res.setUser(user);
        return res;
    }
}
