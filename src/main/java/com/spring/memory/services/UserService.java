package com.spring.memory.services;

import com.spring.memory.entities.Board;
import com.spring.memory.entities.Memory;
import com.spring.memory.entities.Tag;
import com.spring.memory.entities.User;
import com.spring.memory.repositories.BoardRepository;
import com.spring.memory.repositories.MemoryRepository;
import com.spring.memory.repositories.TagRepository;
import com.spring.memory.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final BoardRepository boardRepository;
    private final  MemoryRepository memoryRepository;
    private final TagRepository tagRepository;

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

    public void persistEntities() {

        User user = userRepository.findById(8)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = Board.builder()
                .title("First board")
                .user(user)
                .description("First board description")
                .build();

        boardRepository.save(board);

        Tag tag = Tag.builder()
                .title("first")
                .build();

        Set<Tag> tags = new HashSet<>();
        tags.add(tag);

        tagRepository.saveAll(tags);

        Memory memory = Memory.builder()
                .title("First Memory")
                .content("This is my first memory in my first board")
                .board(board)
                .tags(tags)
                .build();

        memoryRepository.save(memory);
    }

    public void deleteEntities(){
      userRepository.deleteById(8);
    }
}
