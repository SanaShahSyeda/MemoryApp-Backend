package com.spring.memory;

import com.spring.memory.entities.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class MemoryApplication {
	public static void main(String[] args) {

//		SpringApplication.run(MemoryApplication.class, args);

		// 1. Create User using builder
		User user = User.builder()
				.email("johndoe@example.com")
				.password("securepassword")
				.build();

		// 2. Create Board using builder
		Board board = Board.builder()
				.title("My First Board")
				.user(user) // set the owner
				.build();

		// 3. Create Tag using Builder
		Tag tag = Tag.builder()
				.title("first")
				.build();

		Set<Tag> tags = new HashSet<>();
		tags.add(tag);

		// 4. Create Memory using Builder
		Memory memory = Memory.builder()
				.title("First Memory")
				.content("This is my first memory in my first board")
				.board(board)
				.tags(tags)
				.build();

		// 5. Create Notification

		Notification notification = Notification.builder()
						.message("First notification")
				        .memory(memory)
				        .user(user)
				        .build();

		System.out.println(user);
		System.out.println(board);
		System.out.println(memory);
		System.out.println(notification);
	}

}
