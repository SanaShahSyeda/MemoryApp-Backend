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
