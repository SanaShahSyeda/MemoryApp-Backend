package com.spring.memory;


import com.spring.memory.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class MemoryApplication {
	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(MemoryApplication.class, args);
		var userService = context.getBean(UserService.class);
//      userService.persistEntities();
		userService.deleteEntities();
	}
}
