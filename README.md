# MemoryApp-Backend

A personal project to **keep track of memories** and **practice Spring Boot** development.

---

## Overview

The **MemoryApp** is a backend system for storing, organizing, and retrieving personal memories. It includes[not implemented] user authentication, memory creation and tagging.

It is built as a learning project to deepen understanding of Spring Boot internals and JPA entity relationships.

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

## 📁 Project Setup

See [docs/SETUP.md](docs/SETUP.md) for full instructions on prerequisites, database setup, and running the app.

---

## 🧠 Key Concepts

Learn about:

- [Flyway vs JPA Annotations](docs/ANNOTATION_GUIDE.md)
- [JPA Internals: Fetching, Transactional, EntityManager](docs/JPA_CONCEPTS.md)

---

## 🐞 Errors & Fixes

Ongoing list of common issues and their resolutions:
📄 [docs/ERROR_LOG.md](docs/ERROR_LOG.md)

---

## Learning Goals

- Practice JPA relationships: OneToMany, ManyToMany, etc.
- Manage schema evolution using Flyway.
- Improve project structure and modularization.

---

## License

## Diagrams

- **Authentication diagrams:** sequence diagrams for refresh-token rotation and JWT validation are available at [Auth Diagrams](docs/diagrams/auth.md)

## API Endpoints

- **Postman collection:** `https://www.postman.com/syedasanashah/workspace/my-workspace/collection/29239196-8c01f643-fe4b-4a8f-bfbf-cf3a040f1a10?action=share&creator=29239196`

- **Auth:**

  - `POST /api/v1/auth/register` : Register a new user (body: `RegisterDTO`).
  - `POST /api/v1/auth/login` : Login and receive access + refresh tokens (body: `LoginDTO`).
  - `POST /api/v1/auth/refresh` : Exchange a refresh token for a new access + refresh pair (body: `RefreshTokenRequestDTO`).
  - `POST /api/v1/auth/logout` : Logout the currently authenticated user (authenticated). Returns `LogoutResponseDTO` (fields: `message`, `user`).

- **Boards (`/api/v1/boards`):**

  - `POST /api/v1/boards/create` : Create a board (multipart form, authenticated).
  - `PATCH /api/v1/boards/update/{id}` : Update a board (multipart form, authenticated).
  - `DELETE /api/v1/boards/delete/{id}` : Delete a board (authenticated).
  - `GET /api/v1/boards/all` : List boards for the authenticated user (query: `page`, `limit`).
  - `GET /api/v1/boards/{id}` : Get a board (authenticated).

- **Memories (`/api/v1/memories`):**

  - `POST /api/v1/memories/create/{boardId}` : Create a memory on a board (multipart form, authenticated).
  - `PATCH /api/v1/memories/update/{boardId}/{memoryId}` : Update a memory (multipart form, authenticated).
  - `GET /api/v1/memories/board/all/{id}` : List memories of a board (query: `page`, `limit`).
  - `GET /api/v1/memories/{id}` : Get a memory by id (authenticated).
  - `DELETE /api/v1/memories/delete/{id}` : Delete a memory (authenticated).

- **Tags (`/api/v1/tags`):**

  - `POST /api/v1/tags/create` : Create a tag (body: `CreateTagDTO`).
  - `PUT /api/v1/tags/update/{id}` : Update a tag (body: `UpdateTagDTO`, authenticated).
  - `GET /api/v1/tags/{id}` : Get a tag by id(authenticated).
  - `GET /api/v1/tags/all` : List all tags(authenticated).
  - `DELETE /api/v1/tags/delete/{id}` : Delete a tag(authenticated).

- **Users (`/api/v1/users`):**
  - `PUT /api/v1/users/profile` : Update the authenticated user's profile (consumes `multipart/form-data`, authenticated).
    - Form fields:
      - `avatar` (file, optional): New avatar image to upload.
      - `removeAvatar` (boolean, optional): Set to `true` to remove existing avatar.
    - Returns: `UserDTO` (fields: `email`, `avatarUrl`, `createdAt`, `updatedAt`).
    - Notes: Use `Content-Type: multipart/form-data` and include the JWT in the `Authorization: Bearer <token>` header.

Notes:

- Endpoints marked as **authenticated** require a valid JWT in the `Authorization: Bearer <token>` header. The security filter validates the token and sets the authenticated principal (`CustomUserDetails`).
- For multipart endpoints (board/memory create & update) use `Content-Type: multipart/form-data` and send media files in the `mediaFiles` field.
- See the `src/main/java/com/spring/memory/controller` folder for controller-level DTOs and method signatures.

This project is licensed under the [MIT License](LICENSE).

---

## Author

**Sana Gul**
🌐 [LinkedIn](https://www.linkedin.com/in/syeda-sana-gul)

---

> “Preserve memories. Practice code. Progress daily.”
