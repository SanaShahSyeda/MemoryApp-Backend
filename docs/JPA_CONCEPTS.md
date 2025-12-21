# 🔍 JPA Internals: Fetching Strategies, @Transactional, and EntityManager

This guide explains key JPA internals that help manage **persistence**, **performance**, and **transaction control** in Spring Boot applications.

---

## 1️⃣ Fetching Strategies

JPA provides two main fetching strategies that control how related entities are loaded from the database.

### 🔄 Lazy vs Eager

| Strategy | Description |
|----------|-------------|
| `LAZY`   | Data is **loaded on demand** — when the field is accessed. Default for `@OneToMany`, `@ManyToMany`. |
| `EAGER`  | Data is **loaded immediately** with the owning entity. Default for `@ManyToOne`, `@OneToOne`. |

### ✅ Best Practices

- Prefer `LAZY` for collections or deeply nested associations to avoid unnecessary joins and improve performance.
- Use `EAGER` only when you're **certain** the relationship is always needed.
- To override default behavior:

```java
@ManyToOne(fetch = FetchType.LAZY)
private User user;
```

---

## 2️⃣ `@Transactional`: Managing Transactions

The `@Transactional` annotation is used to **define transaction boundaries** in Spring.

### ✅ What It Does

- Begins a transaction before the method runs.
- Commits the transaction if the method finishes normally.
- Rolls back the transaction if a runtime exception occurs.

### 💡 Usage Example

```java
@Transactional
public void showEntityStates() {
    User user = User.builder()
        .email("johnny@example.com")
        .password("securepassword")
        .build();

    if (entityManager.contains(user)) {
        System.out.println("Persistent state");
    } else {
        System.out.println("Transient/Detached");
    }

    userRepository.save(user);
    System.out.println("User Saved");

    if (entityManager.contains(user)) {
        System.out.println("Persistent state");
    } else {
        System.out.println("Transient/Detached");
    }
}
```

### 📸 Entity State Visualization

Before `@Transactional`:

![Default Entity States](./static/Default%20Entity%20States.png)

After `@Transactional`:  

![Entity States After Transactional](./static/Entity%20States_After_@Transactional.png)

> With `@Transactional`, a transactional persistence context is active **throughout the method execution**, allowing entity states to be tracked and managed not just during repository calls(`save()`,`delete()`), but also across any interaction in method.

---

## 3️⃣ EntityManager: Direct Persistence Control

The `EntityManager` is the **core interface** in JPA for interacting with the persistence context.

### 🔧 Responsibilities

- Manage entity lifecycle: `persist()`, `merge()`, `remove()`, `detach()`,
- Control the **first-level cache** (persistence context)
- Enable manual interaction with the database

### ✅ Basic Use Case

```java
@PersistenceContext
private EntityManager entityManager;
```

You can use it to check whether an object is in the persistence context, detach it, or flush pending changes.

```java
entityManager.contains(entity);  // Check if managed
entityManager.detach(entity);   // Remove from persistence context
entityManager.flush();          // Force sync with DB
```

### 📸 Entity State Reference

For a visual breakdown of entity states:  
![Entity States Diagram](./static/Entity%20States.png)

---

## 🧠 Summary

| Concept           | Role                                               |
|------------------|----------------------------------------------------|
| **FetchType**     | Controls when related data is loaded              |
| `@Transactional`  | Defines transaction boundaries in service layer   |
| **EntityManager** | Low-level control over persistence context        |

---

## 4️⃣ Every JPA entity MUST have a no-argument constructor (at least protected).

JPA mandates that every entity must have a public or protected no-argument constructor.
This is not optional; it is a JPA specification rule.

### 🔍 Why JPA Needs a No-Arg Constructor
Hibernate does not instantiate entities using your constructors. Instead, it:
- Creates an empty instance using reflection
- Injects field values directly from the database
- Manages the entity inside the persistence context

Without a no-arg constructor, Hibernate cannot instantiate the entity, resulting in errors during startup (IDE validation) or at runtime.

```java
@Entity
public class Board {
    private String title;

    protected Board() {
        // Required by JPA
    }
    public Board(String title) {
        this.title = title;
    }
}

```

Lombok’s @NoArgsConstructor simply generates this constructor for you.
Removing it from an @Entity violates JPA rules and is correctly flagged by IDEs before runtime.

> 🧠 Key Takeaway
JPA entities are not regular Java objects.
The framework controls their lifecycle and that lifecycle starts with a no-arg constructor.