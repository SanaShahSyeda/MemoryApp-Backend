# MemoryApp-Backend

A personal project to **keep track of memories** and **practice Spring Boot** development.

---

## Overview

MemoryApp allows users to store and organize personal memories using:
- Boards (grouping memories)
- Tags (categorization)
- Media attachments
- Secure authentication

> [!Note]
> Notification support is planned but not yet implemented.

---

## Tech Stack

| Layer                 | Technology      |
| --------------------- | --------------- |
| Framework             | Spring Boot     |
| ORM                   | Spring Data JPA |
| Database              | PostgreSQL      |
| Migrations            | Flyway          |
| Boilerplate Reduction | Lombok          |

---

## Architectural Decisions

- Controllers are **thin** and request-focused
- Business logic lives in the **service layer**
- Repositories expose **minimal query methods**
- DTOs prevent **entity leakage**
- Transactions are handled explicitly
- Flyway owns schema evolution

---

## Security Model

- Stateless JWT-based authentication
- Short-lived **access tokens**
- Refresh tokens stored server-side
- Logout implemented via **refresh token invalidation**
- Protected endpoints enforced through Spring Security filters

Authentication flows are documented with sequence diagrams.

---

## Database Strategy

- PostgreSQL as the primary datastore
- Flyway for controlled, versioned schema migrations
- No automatic schema generation in production
- Explicit migration scripts under `db/migration`

---

## Quick Start

1. For getting started (see `docs/SETUP.md` for details).
2. Update `src/main/resources/application.properties` with your DB credentials (see Configuration below).
3. Run the application locally

---

## Configuration

Set the following essential properties in `src/main/resources/application.properties` or via environment variables:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/memory_app
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT (example keys - replace in production)
app.jwt.secret=replace-with-your-secret
app.jwt.expirationMs=3600000

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

## Running (Dev)

- Run via Maven wrapper:

```powershell
./mvnw spring-boot:run
```

- Run tests:

```powershell
./mvnw test
```

- Run Flyway migrations manually:

```powershell
./mvnw flyway:migrate
```

---

## API Summary

Core endpoints include authentication, boards, memories, tags, and user profile management.

- **API Reference**: `docs/API_REFERENCE.md`; detailed endpoint tables and notes (moved from README).

---

## Documentation

- **Setup**: Step-by-step local setup and prerequisites; [SETUP.md](`docs/SETUP.md`)
- **Annotation Guide**: Flyway vs JPA annotations and best practices; [ANNOTATION_GUIDE](`docs/ANNOTATION_GUIDE.md`)
- **JPA Concepts**: Fetch strategies, `@Transactional`, and `EntityManager` notes; [JPA_CONCEPTS](`docs/JPA_CONCEPTS.md`)
- **Performance & Best Practices**: Performance tips and common pitfalls; [PERFORMANCE_&_BEST_PRACTICES_GUIDE](`docs/PERFORMANCE_&_BEST_PRACTICES_GUIDE.md`)
- **Error Log / Troubleshooting**: Collected errors, causes, and solutions; [ERROR_LOG](`docs/ERROR_LOG.md`)
- **Auth Diagrams**: Sequence diagrams showing authentication flows; [auth](`docs/diagrams/auth.md`)

---

## Architecture

Main packages: `com.spring.memory` → `controller`, `service`, `repository`, `entity`, `dto`, `security`, `configuration`.

Database migrations are in `src/main/resources/db/migration` (Flyway).

---

> This project is licensed under the [MIT License](LICENSE).

---

## Author

**Sana Gul**
🌐 [LinkedIn](https://www.linkedin.com/in/syeda-sana-gul)

---

> “Preserve memories. Practice code. Progress daily.”
