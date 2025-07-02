-- Create ENUM type for memory status
CREATE TYPE memory_status AS ENUM ('DRAFT', 'SAVED');

CREATE TABLE users (
     id SERIAL PRIMARY KEY,
     email VARCHAR(150) UNIQUE NOT NULL,
     avatar_url TEXT,
     password VARCHAR(255) NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE boards (
     id SERIAL PRIMARY KEY,
     user_id INTEGER NOT NULL,
     title VARCHAR(150) NOT NULL,
     description TEXT NOT NULL,
     cover_photo TEXT,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT fk_board_user
         FOREIGN KEY (user_id)
             REFERENCES users(id)
             ON DELETE CASCADE
);

CREATE TABLE memories(
     id SERIAL PRIMARY KEY,
     board_id INTEGER NOT NULL,
     title VARCHAR(150) NOT NULL,
     description TEXT NOT NULL,
     media_url TEXT,
     date DATE,
     status memory_status DEFAULT 'DRAFT',
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT fk_board
         FOREIGN KEY (board_id)
             REFERENCES boards(id)
             ON DELETE CASCADE
);

CREATE TABLE notifications (
     id SERIAL PRIMARY KEY,
     memory_id INTEGER NOT NULL,
     user_id INTEGER NOT NULL,
     message TEXT,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     CONSTRAINT fk_notification_memory
         FOREIGN KEY (memory_id)
             REFERENCES memories(id)
             ON DELETE CASCADE,
     CONSTRAINT fk_notification_user
         FOREIGN KEY (user_id)
             REFERENCES users(id)
             ON DELETE CASCADE
);

-- Create tag table
CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Create join table for memory <-> tag many-to-many relationship
CREATE TABLE memory_tags (
    memory_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    PRIMARY KEY (memory_id, tag_id),
    CONSTRAINT fk_memory FOREIGN KEY (memory_id)
        REFERENCES memories(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY (tag_id)
        REFERENCES tags(id)
        ON DELETE CASCADE
);