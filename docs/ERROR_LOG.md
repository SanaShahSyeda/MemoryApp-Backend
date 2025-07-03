# 📘 Error Log — MemoryApp

A collection of errors encountered during backend development of the **MemoryApp** built using **Spring Boot**, **PostgreSQL**, **Lombok**, **Spring Data JPA**, and **Flyway**, along with their causes and solutions.

---

## 📁 Table of Contents

- [Builder Notation Error](#builder-notation-error)
- [ToString Missing](#tostring-missing)
- [NoSuchBeanException](#nosuchbeandefinitionexception)
---

## 🐞 Builder Notation Error [Lombok] [Builder] [Annotation]

### Code

```java
@SpringBootApplication
public class MemoryApplication {
    public static void main(String[] args) {

        // 1. Create User using builder
        User user = User.builder()
            .email("johndoe@example.com")
            .password("securepassword")
            .build();

        // 2. Create Board using builder
        Board board = Board.builder()
            .title("My First Board")
            .user(user)
            .build();

        System.out.println(board.getTitle() + " " + board.getUser());
    }
}
```

### Error Message

```java
java: cannot find symbol
  symbol: method builder()
```

### Cause

The `@Builder` annotation was not working because the Lombok dependency version was missing in `pom.xml`.

### Solution

- Added the latest version of Lombok in `pom.xml`:

```xml
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <version>1.18.38</version>
</dependency>
```

- Enabled annotation processing in IntelliJ IDEA:
  - Navigate to `File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors`
  - Check: "Enable annotation processing"

- Add the `@Builder` annotation to the class:

```java
@Builder
public class User {
    // code here
}
```

### Related Concepts

Lombok's annotations, annotation processing, Builder design pattern.

**Note:** This issue was discovered while testing the `User` and `Board` classes.

---

## 🐞 ToString Missing [Lombok] [ToString]

### Error Message

```java
"C:\Program Files\Java\jdk-21\bin\java.exe"
My First Board com.spring.memory.entities.User@710726a3
```

### Cause

The object printed its memory reference because the `toString()` method was not overridden.

### Solution

- Add `@ToString` annotation to the class:

```java
@ToString
public class User {
    // code here
}
```

- To exclude sensitive fields like `password`, use:

```java
@ToString(exclude = "password")
public class User {
    private String email;
    private String password;
}
```

### Related Concepts

Lombok's annotations, `Object.toString()` override behavior.

**Note:** This issue was identified while printing `User` data in the `Board` class.

---


## 🐞 NoSuchBeanDefinitionException [Spring] [Beans] [Annotation]

### Code

```java
// MemoryApplication.java
public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(MemoryApplication.class, args);
    var userService = context.getBean(UserService.class);
    userService.showEntityStates();
}
```

```java
// UserService.java
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public void showEntityStates() {
        // logic here
    }
}
```

### Error Message

```java
Exception in thread "restartedMain" java.lang.reflect.InvocationTargetException 
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: 
No qualifying bean of type 'com.spring.memory.services.UserService' available
```

### Cause

The Spring container could not find a bean definition for `UserService` because the class lacked a Spring annotation such as `@Service`, `@Component`, or `@Repository`.

### Solution

Add the appropriate bean annotation (in this case, `@Service`) to `UserService`:

```java
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public void showEntityStates() {
        // logic here
    }
}
```

### Related Concepts

Spring Component Scanning, Bean Lifecycle, Dependency Injection, Annotations (`@Service`, `@Component`, `@Repository`)

