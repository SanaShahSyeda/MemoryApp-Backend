package com.spring.memory.controller;

import com.spring.memory.dto.user.UpdateProfileDTO;
import com.spring.memory.dto.user.UserDTO;
import com.spring.memory.entity.CustomUserDetails;
import com.spring.memory.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> updateProfile(
            @Valid @ModelAttribute UpdateProfileDTO req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return userService.updateProfile(user.getId(), req);
    }
}
