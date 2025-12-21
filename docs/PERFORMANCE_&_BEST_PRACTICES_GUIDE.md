# Memory App Performance Guide

This document contains performance tips and best practices for the Memory App project, focusing on **JPA, Lombok, and entity design**.

---

## 1. `@ToString` Pitfalls & `@ToString.Exclude`

### 🔹 The Problem

Lombok's `@ToString` automatically includes **all fields** of an entity.  
When these fields include **lazy-loaded JPA associations** (like `@ManyToOne` or `@OneToMany`), calling `toString()` can trigger:

- **Extra SQL queries** (lazy loading each association)
- **Performance degradation** for large datasets
- **Memory overhead** due to additional entities loaded in the persistence context

Example: 

```java
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Board {
    //
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // lazy association
}
```

🔹 The Solution: `@ToString.Exclude`

```java
@ManyToOne(fetch = FetchType.LAZY)
@ToString.Exclude
private User user;
```

Example Test:

```java
@Component
public class ToStringPerformanceRunner implements CommandLineRunner {
    private final BoardRepository boardRepository;
    public ToStringPerformanceRunner(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<Board> boards = boardRepository.findAll();
        System.out.println("Number of boards fetched: " + boards.size());
        System.out.println("--- Testing toString() performance ---");

        long totalTime = 0;

        for (Board board : boards) {
            long start = System.nanoTime();
            String boardString = board.toString(); // <-- This triggers lazy loading if not excluded
            long end = System.nanoTime();

            totalTime += (end - start);

            System.out.println("Board ID " + board.getId() + " toString() completed in "
                    + (end - start) / 1_000_000 + " ms");
            // Optional: print the string
            System.out.println(boardString);
        }
        System.out.println("Total time for all boards: " + totalTime / 1_000_000 + " ms");
        System.out.println("--- Done ---");
    }
}

```

- Before `@ToString.Exclude`: triggers extra queries for Users
![@ToString.Exclude excluded](docs/static/@ToString.Exclude excluded.png)

- After `@ToString.Exclude`: safe, no extra queries
![@ToString.Exclude included](docs/static/@ToString.Excluded included.png)

> 💡 Rule of Thumb
Never include lazy associations in @ToString. Use @ToString.Exclude or map to DTOs for safe logging.


## 2. Controller should not use `Optional`

Optional is a Java-level abstraction, not an API contract.
While repositories correctly return Optional to represent possible absence of data, controllers should never expose Optional to API clients.

Before
```java
// Controller level
@GetMapping("/{id}")
public ResponseEntity<Optional<Tag>> getTagById(@PathVariable String id) {
    return ResponseEntity.ok(tagService.getTagById(id));
}

// Service level
public Optional<Tag> getTagById(String id) {
    return tagRepo.findById(Integer.valueOf(id));
}
```
Problems
- API response becomes ambiguous
- Client receives no meaningful HTTP signal (404 vs 200)

![optional at Controller layer](docs/static/GET_tag(not exists)_with_Optional.png)

> Returning Optional from a controller always results in 200 OK, even when the resource does not exist, preventing the client from receiving a meaningful HTTP signal such as 404 Not Found.

✅ After (Resolve Optional Before Controller)

```java
//Controller level
@GetMapping("/{id}")
public ResponseEntity<TagDTO> getTagById(@PathVariable String id) {
    return ResponseEntity.ok(tagService.getTagById(id));
}

//Service level
public TagDTO getTagById(String id) {
    Tag tag = tagRepo.findById(Integer.valueOf(id))
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
    return tagMapper.toDto(tag);
}
```
Benefits
- Missing resource → proper 404 Not Found
- Clean separation of responsibilities

![optional at Controller layer](docs/static/GET_tag(not exists)_Optional_Before_controller.png)

> Optional only helps the SERVER decide. While, HTTP status helps the CLIENT understand.

Returning Optional from controller removes the client’s ability to understand what happened.


## 3. @EqualsAndHashCode is dangerous for JPA entities
Using Lombok’s @EqualsAndHashCode on JPA entities is strongly discouraged.  It can introduce severe performance issues, memory leaks, and subtle bugs, especially in Hibernate-based applications.

Before
```java
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
@Table(name = "tags")
public class Tag {
}
```
- Lombok generates equals() and hashCode() using all fields by default, including mutable ones like id.
- In JPA, IDs are null until persistence, which means hashCode() and equals() change after saving.
- This can lead to HashSet/HashMap corruption, lazy-loading issues, or infinite recursion in bidirectional relationships.
- Never rely on Lombok’s default @EqualsAndHashCode for JPA entities.