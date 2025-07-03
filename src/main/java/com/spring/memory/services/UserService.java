package com.spring.memory.services;

import com.spring.memory.entities.User;
import com.spring.memory.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional
    public void showEntityStates(){
        User user = User.builder()
                .email("johnny@example.com")
                .password("securepassword")
                .build();

        if (entityManager.contains(user)){
            System.out.println("Persistent state");
        }
        else {
            System.out.println("Transient/Detached");
        }

        userRepository.save(user);
        System.out.println("User Saved");

        if (entityManager.contains(user)){
            System.out.println("Persistent state");
        }
        else {
            System.out.println("Transient/Detached");
        }
    }
}
