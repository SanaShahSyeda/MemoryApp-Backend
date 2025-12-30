package com.spring.memory.service;

import com.spring.memory.dto.user.UpdateProfileDTO;
import com.spring.memory.dto.user.UserDTO;
import com.spring.memory.entity.User;
import com.spring.memory.exception.BadRequestException;
import com.spring.memory.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService; // or any storage service

    public UserService(UserRepository userRepository,
                       CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public ResponseEntity<UserDTO> updateProfile(Integer id, UpdateProfileDTO req) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));


        // 3️⃣ Remove avatar explicitly
        if (Boolean.TRUE.equals(req.getRemoveAvatar())) {
            if (user.getAvatarUrl() != null) {
                cloudinaryService.delete(user.getAvatarUrl()); // optional
            }
            user.setAvatarUrl(null);
        }


        MultipartFile avatar = req.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {

            if (user.getAvatarUrl() != null) {
                cloudinaryService.delete(user.getAvatarUrl());
            }

            String uploadedUrl = cloudinaryService.upload(avatar).getUrl();
            user.setAvatarUrl(uploadedUrl);
        }

        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        return ResponseEntity.ok(mapToDTO(user));
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getEmail(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
