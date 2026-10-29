# zhumabay-zhuldyz-student-api

Student CRUD REST API (Spring Boot 3 + PostgreSQL) — DevOps midterm project.

**Author:** Zhuldyz Zhumabay, group IT2-2308, IITU

## Stack

- Java 17+/21, Spring Boot 3.5, Spring Data JPA, Gradle wrapper
- PostgreSQL 16 (Docker), H2 for automated tests
- Docker (multi-stage build), Docker Compose, Jenkins declarative pipeline

## Run with Docker Compose

```bash
cp .env.example .env            # then set your own password in .env
docker compose up -d --build
curl http://localhost:8081/api/info
```

The application listens on port 8080 inside the container and is published on host port **8081**
(host port 8080 is used by Jenkins).

## API

| Method | URL                    | Description          |
|--------|------------------------|----------------------|
| GET    | `/api/info`            | Author and version   |
| POST   | `/api/students`        | Create a student     |
| GET    | `/api/students`        | Get all students     |
| GET    | `/api/students/{id}`   | Get one student      |
| PUT    | `/api/students/{id}`   | Update a student     |
| DELETE | `/api/students/{id}`   | Delete a student     |

A nonexistent ID returns `404` with a JSON error message.

## Change log

- Zhuldyz Zhumabay - feature/zhumabay-zhuldyz-crud: GET /api/students?surname=Zhumabay returns only students with that surname (case-insensitive). Covered by filterStudentsBySurname test.

## Status

Maintained by: Zhuldyz Zhumabay (documentation branch, IT2-2308)
