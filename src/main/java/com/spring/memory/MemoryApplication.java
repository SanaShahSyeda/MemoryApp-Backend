package com.spring.memory;

import com.spring.memory.entities.*;
import com.spring.memory.repositories.UserRepository;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class MemoryApplication {
	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(MemoryApplication.class, args);
		var repository = context.getBean(UserRepository.class);

		// 1. Create User using builder
		User user = User.builder()
				.email("johndoe@example.com")
				.password("securepassword")
				.build();

		repository.save(user);

		 repository.deleteById(1);
	}

}
