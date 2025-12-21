# 📘 Error Log — MemoryApp

A collection of errors encountered during backend development of the **MemoryApp** built using **Spring Boot**, **PostgreSQL**, **Lombok**, **Spring Data JPA**, and **Flyway**, along with their causes and solutions.

---

## 📁 Table of Contents

- [Builder Notation Error](#builder-notation-error)
- [ToString Missing](#tostring-missing)
- [NoSuchBeanException](#-nosuchbeandefinitionexception-spring-beans-annotation)
- [NoSuchElementException](#-nosuchelementexception-spring-optional-repository)
- [TransientPropertyValueException](#-transientpropertyvalueexception-hibernate-entity-cascade)
- [ConstraintViolationException](#-constraintviolationexception-validation-jpa-jakarta)
- [InvalidDataAccessResourceUsageException](#-invaliddataaccessresourceusageexception-postgresqlenum-hibernate6-enumerated)
- [FlywayException](#-flywayexception--flywaycleandisabled-spring-flyway-maven)
- [InvalidDataAccessResource](#-invaliddataaccessresourceusageexception--board-vs-integer-comparison)

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
My First Board com.spring.memory.entity.User@710726a3
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
No qualifying bean of type 'com.spring.memory.service.UserService' available
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

---

## 🐞 NoSuchElementException [Spring] [Optional] [Repository]

### Code

```java
public void deleteEntities() {
    var user = userRepository.findById(1).orElseThrow();
    userRepository.save(user);
}
```

### Error Message

```bash
Exception in thread "restartedMain" java.lang.reflect.InvocationTargetException
Caused by: java.util.NoSuchElementException: No value present
	at java.base/java.util.Optional.orElseThrow(Optional.java:377)
```

### Cause

The user with ID `1` does not exist in the database, and `orElseThrow()` was called without a fallback or custom exception.

### Solution

- Checked and the user doesn't exist in the database
- Provide a meaningful fallback or custom exception[not applied yet]

```java
userRepository.findById(1)
    .orElseThrow(() -> new EntityNotFoundException("User not found"));
```

- Avoid deletion of non-existing users by fetching valid user IDs beforehand.

### Related Concepts

`Optional`, `orElseThrow()`, Repository querying, Spring Data JPA, Exception handling

---

## 🐞 TransientPropertyValueException [Hibernate] [Entity] [Cascade]

### Error Message

```bash
Exception in thread "restartedMain" java.lang.reflect.InvocationTargetException
Caused by: org.springframework.dao.InvalidDataAccessApiUsageException:
org.hibernate.TransientPropertyValueException: Not-null property references a transient value -
transient instance must be saved before current operation:
com.spring.memory.entity.Memory.board -> com.spring.memory.entity.Board
```

### Cause

Trying to save a `Memory` entity that has a reference to a `Board` entity that has not been saved (i.e., it is still **transient**).

### Solution

Saved the referenced `Board` entity before saving the `Memory` entity:

```java
Board board= Board.builder()
                .title("First board")
                .description("First board description")
                .build();
boardRepository.save(board);
```

### Related Concepts

Hibernate entity states, transient references, JPA relationships

---

## 🐞 ConstraintViolationException [Validation] [JPA] [Jakarta]

### Error Message

```bash
Exception in thread "restartedMain" java.lang.reflect.InvocationTargetException
Caused by: jakarta.validation.ConstraintViolationException:
Validation failed for classes [com.spring.memory.entity.Board] during persist time
for groups [jakarta.validation.groups.Default, ]
```

### Cause

Tried to persist an entity (`Board`) that violated one or more validation constraints (e.g., `@NotNull`, `@Size`, etc.).

### Solution

Inspected the `Board` entity for missing or invalid values on fields with validation annotations and found User was missing

```java
@Entity
public class Board {
        @NotNull
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;
        // other fields...
}
```

Ensure that before saving:

```java
Board board = new Board();
board.setUser("My First Board"); // must not be null!
boardRepository.save(board);
```

**Tip**: Use logging or debugging to print the actual violations from the exception for clarity.

### Related Concepts

Bean Validation (JSR 380 / Jakarta Validation), `@NotNull`, `@Size`, `@Valid`, entity constraints

---

## 🐞 InvalidDataAccessResourceUsageException [PostgreSQLEnum] [Hibernate6+] [Enumerated]

### Error Message

```bash
Exception in thread "restartedMain" java.lang.reflect.InvocationTargetException
Caused by: org.springframework.dao.InvalidDataAccessResourceUsageException: could not execute statement
[ERROR: column "status" is of type memory_status but expression is of type character varying
  Hint: You will need to rewrite or cast the expression.
```

### Cause

PostgreSQL is rejecting the value for the `status` column because it's receiving a `VARCHAR`, while the column is of a **custom enum type** (`memory_status`). This is a mapping issue caused by Hibernate not knowing how to handle PostgreSQL enum types by default.

### Solution

- Added `@JdbcType(PostgreSQLEnumJdbcType.class)` on the enum field.
- Still used `@Enumerated(EnumType.STRING)` to ensure Java → String enum mapping.
- Hibernate 6+ requires this additional `@JdbcType` to work with PostgreSQL enums.

### Example

```java
@Enumerated(EnumType.STRING)
@JdbcType(PostgreSQLEnumJdbcType.class)
@Column(name = "status", columnDefinition = "memory_status")
private MemoryStatus status;
```

- The enum `MemoryStatus` exists in Java.
- The custom enum type `memory_status` is created in PostgreSQL using Flyway.

### Related Concepts

JPA Enum Mapping, PostgreSQL custom enum types, Hibernate 6 type system, `@JdbcType`, `@Enumerated`

---

## 🐞 FlywayException — flyway.cleanDisabled [Spring] [Flyway] [Maven]

### Error Message

```bash
[ERROR] Failed to execute goal org.flywaydb:flyway-maven-plugin:11.10.0:clean (default-cli) on project memory:
org.flywaydb.core.api.FlywayException: Unable to execute clean as it has been disabled with the 'flyway.cleanDisabled' property.
```

### Cause

Spring Boot and Flyway **disable the `flyway:clean` operation by default** because it is a destructive operation—it drops the entire schema (all tables, views, etc.).

### Solution

Explicitly enabled Flyway's `clean` command by modifying the Flyway Maven plugin configuration in `pom.xml`:

```xml
<plugin>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-maven-plugin</artifactId>
  <version>11.10.0</version>
  <configuration>
    <url>jdbc:postgresql://localhost:5432/memory_app</url>
    <user>postgresql_username</user>
    <password>postgresql_password</password>
    <cleanDisabled>false</cleanDisabled>
  </configuration>
</plugin>
```

> **Note:**  
> Be extremely cautious with `flyway:clean`:
>
> - It **drops all schema objects** (tables, views, sequences, etc.)
> - Only use in **local/dev environments**

### Related Concepts

Flyway Maven plugin, Spring Boot integration with Flyway, `cleanDisabled` safeguard

---

## 🐞 InvalidDataAccessResourceUsageException — Board vs Integer comparison

### Observed response

```
{
    "type": "about:blank",
    "title": "Unsupported Media Type",
    "status": 415,
    "detail": "Content-Type 'multipart/form-data;boundary=--------------------------664780261817957559467651;charset=UTF-8' is not supported.",
    "instance": "/api/v1/memories/update/26/20"
}
```

### Where it happened

- Endpoint: `PATCH /api/v1/memories/update/{boardId}/{memoryId}`
- Controller expected a JSON body mapped with `@RequestBody UpdateMemoryDTO`
- Client sent a `multipart/form-data` request (typically used when uploading files or using `@ModelAttribute`)

### Explanation

Spring rejected the request with HTTP 415 because the controller method did not declare support for `multipart/form-data`. The server-side handler expected JSON (`application/json`) but the client sent multipart form data (often produced by HTML forms or Postman when files are attached). When a controller uses `@RequestBody` Spring only attempts to deserialize the request body as the declared content type (JSON by default) and will not process multipart content unless the handler consumes it.

### Cause

- Sending `multipart/form-data` to an endpoint that expects JSON (`@RequestBody`) triggers 415.
- Using `@ModelAttribute` or posting forms with files requires the controller to accept `multipart/form-data` (via `@ModelAttribute` or `@RequestPart` plus `consumes = MediaType.MULTIPART_FORM_DATA_VALUE`).

### Solution

1. Accept multipart/form-data on the server (support file uploads)

- Change controller to consume multipart and use `@ModelAttribute` for a form-backed DTO or use `@RequestPart` for a JSON part plus files.
- Example controller signature (form-based):

```java
@PatchMapping(value = "update/{boardId}/{memoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<MemoryDTO> updateMemory(
                @PathVariable Integer boardId,
                @PathVariable Integer memoryId,
                @AuthenticationPrincipal CustomUserDetails user,
                @ModelAttribute UpdateMemoryDTO form) {
}
```

Related Concepts
@ModelAttribute, multipart/form-data,
