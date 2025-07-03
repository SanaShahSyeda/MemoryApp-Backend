# Getting Started

## Prerequisites

- Java 17+
- Maven
- PostgreSQL

## 🔧 Setup Instructions

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
