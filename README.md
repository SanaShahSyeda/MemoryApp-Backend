# MemoryApp-Backend

A personal project to **keep track of memories** and **practice Spring Boot** development.

---

##  Overview

The **MemoryApp** is a backend system for storing, organizing, and retrieving personal memories. It includes[not implemented] user authentication, memory creation and tagging.

It is built as a learning project to deepen understanding of Spring Boot internals and JPA entity relationships.

---

##  Tech Stack

| Layer            | Technology            |
|------------------|------------------------|
| Framework        | Spring Boot            |
| ORM              | Spring Data JPA        |
| Database         | PostgreSQL             |
| Migrations       | Flyway                 |
| Boilerplate Reduction | Lombok           |

---

## Features

- 🧍 User Registration & Login
- 📓 Create & View Memories & Boards
- 🏷️ Add Tags to Memories
- 📦 Status Management (Draft, Published)
- 📜 Flyway-based versioned schema migrations
- 🔐 Validation using Jakarta annotations

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- PostgreSQL

### 🔧 Setup Instructions

1. **Clone the repository:**

   ```bash
   git clone https://github.com/SanaShahSyeda/memoryapp-backend.git
   cd memoryapp-backend
   ```
2. **Configure PostgreSQL**
    
   ```sql
   CREATE DATABASE memory_app;
   ```
3. **Edit `application.properties`**
     Set your PostgreSQL credentials in src/main/resources/application.properties:
    
   ```yaml
   spring.datasource.url=jdbc:postgresql://localhost:5432/memory_app
   spring.datasource.username=your_username
   spring.datasource.password=your_password 
   ```
4. **Run the application:**

   ```bash
   ./mvnw spring-boot:run 
   ```
---

## Database Migration

Flyway automatically runs all SQL files in the `src/main/resources/db/migration` directory.

---

## ⚠️ Important Notes on Flyway + JPA + Java Bean Validation Annotations

Since this project uses **Flyway** for database schema management, please keep in mind the roles and limitations of the annotations used in your JPA entities:

---

### 🛠️ Flyway Handles Schema Creation

- The **actual database schema** including tables, column types, lengths, `NOT NULL`, foreign keys, constraints, and `ON DELETE CASCADE` is managed **exclusively by Flyway migration scripts**.
- Therefore, **JPA annotations like `@Column`, `@NotNull`, `@Size`, or `@OnDelete` do not affect the schema in any way** when Flyway is used.

---

### ✅ JPA Annotations (Used for Hibernate Entity Mapping)

These are **essential for Hibernate to recognize and map entities** to your Flyway-managed tables. Keep these in your entity classes:

- `@Entity`, `@Table` declares the class as a JPA entity and specifies table mapping.
- `@Id`, `@GeneratedValue`  identifies the primary key and auto-generation strategy.
- `@Column`  maps fields to columns (optionally useful for clarity, especially with custom names or column definitions).
- `@ManyToOne`, `@OneToMany`, `@ManyToMany`, `@JoinColumn` define relationships between entities.
- `@Enumerated(EnumType.STRING)` required to map Java enums to textual DB values.

---

### ✅ Jakarta Bean Validation Annotations (Java-Level Validation Only)

These annotations are used for **runtime validation of Java objects**, especially:

- In REST API request bodies (with `@Valid`)
- When using manual validation before persistence

They **do not impact the database schema**, but they help prevent invalid data entering your application:

| Annotation         | Purpose                                            |
|--------------------|----------------------------------------------------|
| `@NotNull`         | Ensures field is not null at runtime               |
| `@Size(min, max)`  | Enforces string or collection length constraints   |
| `@Min`, `@Max`     | Validates numeric range                            |
| `@Email`           | Ensures email format is valid                      |
| `@Pattern(regex)`  | Validates strings using regex                      |

💡 These should be used **on DTOs or entities** when validating inputs from forms, APIs, etc.

---

### ❌ Annotations with No Effect in This Setup (Can Be Removed)

| Annotation                             | Why it can be removed                                           |
|----------------------------------------|------------------------------------------------------------------|
| `@OnDelete(action = OnDeleteAction.CASCADE)` | Only works when Hibernate manages schema — not used with Flyway |
| `nullable = false` in `@Column`        | Already enforced in Flyway SQL — optional in code               |
| `length = Integer.MAX_VALUE`           | Unnecessary for TEXT columns — consider using `@Lob` or omit     |

---

### ✅ Summary

| Concern                  | What to Use                                                    |
|--------------------------|----------------------------------------------------------------|
| **Schema Definition**    | Flyway SQL scripts                                             |
| **Entity Mapping**       | JPA Annotations (`@Entity`, `@Id`, `@ManyToOne`, etc.)         |
| **Java Object Validation** | Bean Validation Annotations (`@NotNull`, `@Size`, etc.)        |
| **Schema Auto-generation** | ❌ Do not rely on JPA or Hibernate annotations for this when using Flyway |

---

##  Learning Goals

- Practice JPA relationships: OneToMany, ManyToMany, etc.
- Manage schema evolution using Flyway.
- Improve project structure and modularization.

---

## License

This project is licensed under the [MIT License](LICENSE).

---

##  Author
**Sana Gul**  
🚀 Software Engineer | Java & Angular Enthusiast   
🌐 [LinkedIn](https://www.linkedin.com/in/syeda-sana-gul)

---

> “Preserve memories. Practice code. Progress daily.”
