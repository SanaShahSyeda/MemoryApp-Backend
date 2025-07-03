package com.spring.memory;

import com.spring.memory.entities.*;
import com.spring.memory.repositories.UserRepository;
import com.spring.memory.services.UserService;
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
		var userService = context.getBean(UserService.class);
        userService.showEntityStates();
	}
}
