# API Reference

This file contains the main HTTP endpoints exposed by the MemoryApp backend. It was moved out of the top-level `README.md` to keep the README concise.

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

| Method | Path                       | Description                         | Auth | Notes                |
| ------ | -------------------------- | ----------------------------------- | ---- | -------------------- |
| POST   | `/api/v1/tags/create`      | Create a tag (body: `CreateTagDTO`) | No   | Body: `CreateTagDTO` |
| PUT    | `/api/v1/tags/update/{id}` | Update a tag (body: `UpdateTagDTO`) | Yes  | Body: `UpdateTagDTO` |
| GET    | `/api/v1/tags/{id}`        | Get a tag by id                     | Yes  | -                    |
| GET    | `/api/v1/tags/all`         | List all tags                       | Yes  | -                    |
| DELETE | `/api/v1/tags/delete/{id}` | Delete a tag                        | Yes  | -                    |

**Users** (`/api/v1/users`)

| Method | Path                    | Description                             | Auth | Notes                                                                                                                                                                                                        |
| ------ | ----------------------- | --------------------------------------- | ---- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| PUT    | `/api/v1/users/profile` | Update the authenticated user's profile | Yes  | `multipart/form-data`; form fields: `avatar` (file, optional), `removeAvatar` (boolean). Returns `UserDTO` (`email`, `avatarUrl`, `createdAt`, `updatedAt`). Include `Authorization: Bearer <token>` header. |
