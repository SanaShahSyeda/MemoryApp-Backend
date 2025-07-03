package com.spring.memory;

import com.spring.memory.entities.Board;
import com.spring.memory.entities.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

		System.out.println(board.getTitle()+" "+ board.getUser());
	}

}
