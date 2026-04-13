# Present Project

A modular Spring Boot REST API for managing users, cities, regions, authentication, and profiles. Built with a clean, generic architecture emphasizing reusability and separation of concerns.

## Tech Stack

- **Java 17** / **Spring Boot 4.0.5**
- **Spring Data JPA** + **PostgreSQL**
- **Spring Security** + **JWT** (BCrypt password encoding)
- **Spring Modulith 2.0.5** (modular architecture)
- **Redis** (caching, sessions)
- **Liquibase** (database migrations)
- **Caffeine** (in-memory cache)
- **Hibernate Validator 9.1.0**
- **Lombok** / **Maven**

## Architecture

The project follows a **feature-based modular structure** with reusable generic core components.

```
src/main/java/com/sultanov/present_project/
├── configs/              # Security, Web, Cache configuration
├── core/
│   ├── abstractions/     # Generic base classes (Model, DTO, Repository, Controller, Mapper)
│   ├── actions/
│   │   ├── rest_actions/ # Reusable REST actions (Create, Index, Show)
│   │   ├── LocationActions.java
│   │   ├── PasswordActions.java
│   │   └── PhoneActions.java
│   ├── exceptions/       # Global exception handling
│   ├── interfaces/       # StorageService interface
│   ├── models/           # Base Media entity
│   ├── repositories/     # BaseMediaRepository, MediaRepository
│   ├── services/         # LocalStorageService
│   └── utils/            # Lang, PageResource
└── features/
    ├── auth/             # JWT auth, OTP, sessions, annotations
    ├── cities/           # City management
    ├── profile/          # Profile update, phone change, avatars
    ├── rbac/             # Roles & permissions
    ├── regions/          # Region management
    ├── settings/         # App settings with cache
    └── users/            # User management, UserMedia
```

### Core Abstractions

| Class | Purpose |
|---|---|
| `AbstractModel` | Base JPA entity with `id`, `created_at`, `updated_at` |
| `AbstractDTO<E>` | Type-safe DTO marker interface |
| `AbstractRepository<E>` | Generic JPA repository |
| `AbstractController<E, R, M>` | Generic REST controller |
| `AbstractModelMapper<E, DTO>` | Base entity-to-DTO mapper |
| `Media` | Base polymorphic media entity (single-table inheritance) |
| `BaseMediaRepository<T>` | Generic media repository with discriminator filtering |
| `StorageService` | File storage abstraction (local / cloud) |

### Action Pattern

Instead of traditional service classes, the project uses **single-responsibility action components**:

**REST Actions** (`core/actions/rest_actions/`):
- **`CreateAction<E, R>`** — Generic transactional entity creation
- **`IndexAction<E, R>`** — Paginated list retrieval
- **`ShowAction<E, R>`** — Single entity retrieval by ID

**Shared Actions** (`core/actions/`):
- **`PasswordActions`** — BCrypt password encoding and matching
- **`LocationActions`** — City/Region validation and retrieval
- **`PhoneActions`** — Phone normalization to Uzbekistan format (+998)

### Security Annotations

Custom meta-annotations built on top of `@PreAuthorize`:

| Annotation | Usage |
|---|---|
| `@HasRole("admin")` | Requires `ROLE_admin` authority |
| `@HasPermission("users.create")` | Requires exact permission authority |
| `@Can("users.create")` | Requires permission or matching role |

## API Endpoints

### Init

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/api/init` | Optional | App settings, cities, current user |
| `POST` | `/api/init/cache/clear` | ADMIN | Clear settings cache |

### Auth

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/send-otp` | Send OTP to phone |
| `POST` | `/api/auth/verify-otp` | Verify OTP, receive JWT token |
| `GET` | `/api/auth/me` | Get current authenticated user |
| `DELETE` | `/api/auth/logout` | Revoke current session |
| `GET` | `/api/auth/sessions` | List active sessions |
| `DELETE` | `/api/auth/sessions/{id}` | Revoke session by ID |

### Profile

| Method | Endpoint | Description |
|---|---|---|
| `PATCH` | `/api/profile` | Update profile (name, bio, etc.) |
| `POST` | `/api/profile/change-phone` | Request phone change OTP |
| `POST` | `/api/profile/change-phone/verify` | Verify and apply phone change |
| `GET` | `/api/profile/avatars` | List user avatars |
| `POST` | `/api/profile/avatars` | Upload avatar (multipart) |
| `PATCH` | `/api/profile/avatars/{id}/set-default` | Set avatar as default |
| `DELETE` | `/api/profile/avatars/unset-default` | Unset default avatar |
| `DELETE` | `/api/profile/avatars/{id}` | Delete avatar |

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

### Roles & Permissions (Admin)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/roles` | List roles |
| `POST` | `/api/roles` | Create role |
| `GET` | `/api/permissions` | List permissions |
| `POST` | `/api/permissions` | Create permission |
| `POST` | `/api/admin/users/{id}/roles` | Attach roles to user |
| `DELETE` | `/api/admin/users/{id}/roles` | Detach roles from user |
| `POST` | `/api/admin/users/{id}/permissions` | Attach permissions to user |
| `DELETE` | `/api/admin/users/{id}/permissions` | Detach permissions from user |

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
  "data": { ... },
  "status": "success"
}
```

**Error:**
```json
{
  "status": false,
  "message": "Resource not found"
}
```

## File Storage

Uploaded files are served via `/storage/**` and stored on disk under `storage.root`.

```
storage/
└── users/
    └── {userId}/
        └── avatars/
            └── {uuid}.jpg
```

Configure in `application.properties`:
```properties
storage.root=/path/to/storage
app.url=https://your-domain.com
```

## Caching

| Cache | Backend | TTL | Key |
|---|---|---|---|
| `settings` | Caffeine (in-memory) | 10 min | setting key |
| Default avatar URL | Redis | permanent | `avatar:default:{userId}` |

## Data Model

```
User ──ManyToOne──▶ Region ──ManyToOne──▶ City
User ──ManyToOne──▶ City
User ──ManyToMany──▶ Role ──ManyToMany──▶ Permission
User ──OneToMany──▶ UserMedia (avatars)
User ──OneToMany──▶ PersonalAccessToken (sessions)
```

- **City** — `name`, `slug`, `is_active`, `is_default`, `sort_order`
- **Region** — `name`, `slug`, `is_active`, belongs to a City
- **User** — `username`, `phone`, `language`, `firstName`, `lastName`, `about`, belongs to Region and City
- **UserMedia** — polymorphic media (avatars), `custom_properties` JSON with `is_default`
- **PersonalAccessToken** — JWT sessions with device name and IP

## Database Migrations (Liquibase)

| File | Description |
|---|---|
| `0001_init.sql` | Initial schema |
| `0002_create_settings.sql` | Settings table |
| `0003_drop_laravel_tables.sql` | Cleanup legacy tables |
| `0004_media_recreate_uuid_pk.sql` | Media table with UUID primary key |
| `0005_users_add_language.sql` | Add `language` column to users |

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL
- Redis
- Maven

### Configuration

Edit `application-local.properties`:

```properties
app.url=https://your-domain.com
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.data.redis.host=localhost
spring.data.redis.port=6379
storage.root=/path/to/storage
jwt.secret=your_jwt_secret
jwt.expiration=86400000
```

### Build & Run

```bash
./mvnw clean package
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The API will be available at `http://localhost:8080`.

## Key Design Decisions

- **1-indexed pagination** for user-friendly page numbers
- **Snake case** JSON property naming via Jackson configuration
- **Phone normalization** to Uzbekistan format (+998)
- **OTP-based authentication** — no passwords for end users
- **CSRF disabled** for API-only usage
- **DDL auto set to `none`** — schema managed via Liquibase
- **Polymorphic media** — single `media` table for all entity types via JPA single-table inheritance
- **Redis default avatar caching** — avoids DB query on every `/me` call