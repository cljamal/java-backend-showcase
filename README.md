# Present Project

A modular Spring Boot REST API for managing users, cities, and regions with a hierarchical geographic structure. Built with a clean, generic architecture emphasizing reusability and separation of concerns.

## Tech Stack

- **Java 17** / **Spring Boot 4.0.5**
- **Spring Data JPA** + **PostgreSQL**
- **Spring Security** (BCrypt password encoding)
- **Spring Modulith 2.0.5** (modular architecture)
- **Hibernate Validator 9.1.0**
- **Lombok** / **Maven**

## Architecture

The project follows a **feature-based modular structure** with reusable generic core components.

```
src/main/java/com/sultanov/present_project/
├── configs/              # Security & Web configuration
├── core/
│   ├── abstractions/     # Generic base classes (Model, DTO, Repository, Controller, Mapper)
│   ├── actions/
│   │   ├── rest_actions/ # Reusable REST actions (Create, Index, Show)
│   │   ├── LocationActions.java
│   │   └── PasswordActions.java
│   ├── exceptions/       # Global exception handling
│   └── utils/            # PageResource
└── features/
    ├── users/            # User management feature
    ├── cities/           # City management feature
    └── regions/          # Region management feature
```

### Core Abstractions

| Class | Purpose |
|---|---|
| `AbstractModel` | Base JPA entity with `id`, `created_at`, `updated_at` |
| `AbstractDTO<E>` | Type-safe DTO marker interface |
| `AbstractRepository<E>` | Generic JPA repository |
| `AbstractController<E, R, M>` | Generic REST controller |
| `AbstractModelMapper<E, DTO>` | Base entity-to-DTO mapper with abstract `toIndex`, `toShow`, `toStored` |

### Action Pattern

Instead of traditional service classes, the project uses **single-responsibility action components**:

**REST Actions** (`core/actions/rest_actions/`):
- **`CreateAction<E, R>`** — Generic transactional entity creation
- **`IndexAction<E, R>`** — Paginated list retrieval (read-only)
- **`ShowAction<E, R>`** — Single entity retrieval by ID

**Shared Actions** (`core/actions/`):
- **`PasswordActions`** — BCrypt password encoding and matching
- **`LocationActions`** — City/Region validation and retrieval

Each feature extends REST actions with domain-specific logic (e.g., `CreateUserAction` handles password encoding, phone normalization, and location validation).

## API Endpoints

### Users

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/users` | List users (paginated) |
| `GET` | `/api/users/{id}` | Get user by ID |
| `POST` | `/api/users` | Create a new user |

### Cities

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/cities` | List cities (paginated) |
| `GET` | `/api/cities/{id}` | Get city by ID |
| `POST` | `/api/cities` | Create a new city |

### Regions

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/regions` | List regions (paginated) |
| `GET` | `/api/regions/{id}` | Get region by ID |
| `POST` | `/api/regions` | Create a new region |

## Response Format

**Paginated list:**
```json
{
  "data": [ ... ],
  "meta": {
    "current_page": 1,
    "last_page": 5,
    "per_page": 20,
    "total": 100
  }
}
```

**Single resource:**
```json
{
  "data": { ... }
}
```

**Error:**
```json
{
  "status": false,
  "message": "Resource not found"
}
```

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL
- Maven

### Configuration

Set the following environment variables or edit `application.properties`:

```properties
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

Default database connection: `jdbc:postgresql://localhost:5432/db_name`

### Build & Run

```bash
./mvnw clean package
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Data Model

```
User ──ManyToOne──▶ Region ──ManyToOne──▶ City
User ──ManyToOne──▶ City
```

- **City** — `name`, `slug`, `is_active`, `is_default`, `sort_order`
- **Region** — `name`, `slug`, `is_active`, belongs to a City
- **User** — `username`, `phone`, `password`, `firstName`, `lastName`, `about`, belongs to a Region and City

## Key Design Decisions

- **1-indexed pagination** for user-friendly page numbers
- **Snake case** JSON property naming via Jackson configuration
- **Phone normalization** to Uzbekistan format (+998)
- **CSRF disabled** for API-only usage
- **DDL auto set to `none`** — schema managed manually
