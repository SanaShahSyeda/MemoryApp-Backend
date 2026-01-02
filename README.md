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

**Auth** (`/api/v1/auth`)

| Method | Path                    | Description                                                                               | Auth |
| ------ | ----------------------- | ----------------------------------------------------------------------------------------- | ---- |
| POST   | `/api/v1/auth/register` | Register a new user (body: `RegisterDTO`)                                                 | No   |
| POST   | `/api/v1/auth/login`    | Login and receive access + refresh tokens (body: `LoginDTO`)                              | No   |
| POST   | `/api/v1/auth/refresh`  | Exchange a refresh token for a new access + refresh pair (body: `RefreshTokenRequestDTO`) | No   |
| POST   | `/api/v1/auth/logout`   | Logout the currently authenticated user; returns `LogoutResponseDTO` (`message`, `user`)  | Yes  |

**Boards** (`/api/v1/boards`)

| Method | Path                         | Description                            | Auth | Notes                  |
| ------ | ---------------------------- | -------------------------------------- | ---- | ---------------------- |
| POST   | `/api/v1/boards/create`      | Create a board                         | Yes  | `multipart/form-data`  |
| PATCH  | `/api/v1/boards/update/{id}` | Update a board                         | Yes  | `multipart/form-data`  |
| DELETE | `/api/v1/boards/delete/{id}` | Delete a board                         | Yes  | -                      |
| GET    | `/api/v1/boards/all`         | List boards for the authenticated user | Yes  | Query: `page`, `limit` |
| GET    | `/api/v1/boards/{id}`        | Get a board by id                      | Yes  | -                      |

**Memories** (`/api/v1/memories`)

| Method | Path                                           | Description                | Auth | Notes                                    |
| ------ | ---------------------------------------------- | -------------------------- | ---- | ---------------------------------------- |
| POST   | `/api/v1/memories/create/{boardId}`            | Create a memory on a board | Yes  | `multipart/form-data` (use `mediaFiles`) |
| PATCH  | `/api/v1/memories/update/{boardId}/{memoryId}` | Update a memory            | Yes  | `multipart/form-data`                    |
| GET    | `/api/v1/memories/board/all/{id}`              | List memories of a board   | Yes  | Query: `page`, `limit`                   |
| GET    | `/api/v1/memories/{id}`                        | Get a memory by id         | Yes  | -                                        |
| DELETE | `/api/v1/memories/delete/{id}`                 | Delete a memory            | Yes  | -                                        |

**Tags** (`/api/v1/tags`)

| Method | Path                       | Description                         | Auth |
| ------ | -------------------------- | ----------------------------------- | ---- |
| POST   | `/api/v1/tags/create`      | Create a tag (body: `CreateTagDTO`) | Yes  |
| PUT    | `/api/v1/tags/update/{id}` | Update a tag (body: `UpdateTagDTO`) | Yes  |
| GET    | `/api/v1/tags/{id}`        | Get a tag by id                     | Yes  |
| GET    | `/api/v1/tags/all`         | List all tags                       | Yes  |
| DELETE | `/api/v1/tags/delete/{id}` | Delete a tag                        | Yes  |

**Users** (`/api/v1/users`)

| Method | Path                    | Description                             | Auth | Notes                                                                                                                                                                                                        |
| ------ | ----------------------- | --------------------------------------- | ---- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| PUT    | `/api/v1/users/profile` | Update the authenticated user's profile | Yes  | `multipart/form-data`; form fields: `avatar` (file, optional), `removeAvatar` (boolean). Returns `UserDTO` (`email`, `avatarUrl`, `createdAt`, `updatedAt`). Include `Authorization: Bearer <token>` header. |

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
