--liquibase formatted sql

--changeset init:1
-- Расширение для полнотекстового поиска по подстроке (LIKE '%term%')
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE IF NOT EXISTS "cities" (
    "id"         BIGSERIAL    NOT NULL,
    "name"       VARCHAR(255) NOT NULL,
    "slug"       VARCHAR(255) NOT NULL,
    "is_active"  BOOLEAN      NOT NULL DEFAULT true,
    "sort_order" SMALLINT     NOT NULL DEFAULT 0,
    "is_default" BOOLEAN      NOT NULL DEFAULT false,
    "created_at" TIMESTAMP    NULL,
    "updated_at" TIMESTAMP    NULL,
    PRIMARY KEY ("id"),
    UNIQUE ("slug")
);

-- Частичный индекс: только активные города, сортированные — намного меньше и быстрее
CREATE INDEX IF NOT EXISTS idx_cities_active_sort ON "cities" ("sort_order") WHERE "is_active" = true;

CREATE TABLE IF NOT EXISTS "regions" (
    "id"         BIGSERIAL    NOT NULL,
    "city_id"    BIGINT       NOT NULL,
    "name"       VARCHAR(255) NOT NULL,
    "slug"       VARCHAR(255) NOT NULL,
    "is_active"  BOOLEAN      NOT NULL DEFAULT true,
    "created_at" TIMESTAMP    NULL,
    "updated_at" TIMESTAMP    NULL,
    PRIMARY KEY ("id"),
    UNIQUE ("slug"),
    CONSTRAINT "regions_city_id_foreign" FOREIGN KEY ("city_id") REFERENCES "cities" ("id") ON DELETE CASCADE
);

-- Частичный индекс: только активные регионы конкретного города
CREATE INDEX IF NOT EXISTS idx_regions_city_active ON "regions" ("city_id") WHERE "is_active" = true;
-- Полный индекс city_id нужен для FK enforcement при DELETE/UPDATE в cities
CREATE INDEX IF NOT EXISTS idx_regions_city_id     ON "regions" ("city_id");

CREATE TABLE IF NOT EXISTS "permissions" (
    "id"         BIGSERIAL    NOT NULL,
    "name"       VARCHAR(255) NOT NULL,
    "slug"       VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMP    NULL,
    "updated_at" TIMESTAMP    NULL,
    PRIMARY KEY ("id"),
    -- Раздельные UNIQUE: slug используется в hasAuthority(slug) — должен быть уникальным независимо
    UNIQUE ("name"),
    UNIQUE ("slug")
);

CREATE TABLE IF NOT EXISTS "roles" (
    "id"         BIGSERIAL    NOT NULL,
    "name"       VARCHAR(255) NOT NULL,
    "slug"       VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMP    NULL,
    "updated_at" TIMESTAMP    NULL,
    PRIMARY KEY ("id"),
    -- Раздельные UNIQUE: slug используется в hasRole(slug) — должен быть уникальным независимо
    UNIQUE ("name"),
    UNIQUE ("slug")
);

CREATE TABLE IF NOT EXISTS "role_has_permissions" (
    "permission_id" BIGINT NOT NULL,
    "role_id"       BIGINT NOT NULL,
    PRIMARY KEY ("permission_id", "role_id"),
    CONSTRAINT "role_has_permissions_permission_id_foreign" FOREIGN KEY ("permission_id") REFERENCES "permissions" ("id") ON DELETE CASCADE,
    CONSTRAINT "role_has_permissions_role_id_foreign"       FOREIGN KEY ("role_id")       REFERENCES "roles"       ("id") ON DELETE CASCADE
);

-- PK покрывает (permission_id, *) → отдельный индекс для обратного: все права у роли
CREATE INDEX IF NOT EXISTS idx_role_has_permissions_role_id ON "role_has_permissions" ("role_id");

CREATE TABLE IF NOT EXISTS "users" (
    "id"         BIGSERIAL     NOT NULL,
    "password"   VARCHAR(255)  NOT NULL,
    "username"   VARCHAR(255)  NULL,
    "phone"      VARCHAR(255)  NULL,
    "first_name" VARCHAR(255)  NULL,
    "last_name"  VARCHAR(255)  NULL,
    "full_name"  VARCHAR(255)  GENERATED ALWAYS AS (NULLIF(TRIM(COALESCE(first_name, '') || ' ' || COALESCE(last_name, '')), '')) STORED,
    "about"      TEXT          NULL,
    "lat"        NUMERIC(10,7) NULL,
    "lng"        NUMERIC(11,7) NULL,
    "city_id"    BIGINT        NULL,
    "region_id"  BIGINT        NULL,
    "created_at" TIMESTAMP     NULL,
    "updated_at" TIMESTAMP     NULL,
    PRIMARY KEY ("id"),
    UNIQUE ("username"),
    UNIQUE ("phone"),
    CONSTRAINT "users_city_id_foreign"   FOREIGN KEY ("city_id")   REFERENCES "cities"  ("id") ON DELETE SET NULL,
    CONSTRAINT "users_region_id_foreign" FOREIGN KEY ("region_id") REFERENCES "regions" ("id") ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_users_city_id   ON "users" ("city_id");
CREATE INDEX IF NOT EXISTS idx_users_region_id ON "users" ("region_id");
-- Геопространственный: WHERE lat BETWEEN ? AND ? AND lng BETWEEN ? AND ?
CREATE INDEX IF NOT EXISTS idx_users_lat_lng   ON "users" ("lat", "lng");
-- B-tree: для ORDER BY full_name
CREATE INDEX IF NOT EXISTS idx_users_full_name      ON "users" ("full_name");
-- GIN триграммный: покрывает LIKE '%любой%' поиск по имени
CREATE INDEX IF NOT EXISTS idx_users_full_name_trgm ON "users" USING gin ("full_name" gin_trgm_ops);

CREATE TABLE IF NOT EXISTS "model_has_permissions" (
    "permission_id" BIGINT NOT NULL,
    "model_id"      BIGINT NOT NULL,
    -- PK предотвращает дубликаты прав у пользователя
    PRIMARY KEY ("model_id", "permission_id"),
    CONSTRAINT "model_has_permissions_permission_id_foreign" FOREIGN KEY ("permission_id") REFERENCES "permissions" ("id") ON DELETE CASCADE,
    CONSTRAINT "model_has_permissions_model_id_foreign"      FOREIGN KEY ("model_id")      REFERENCES "users"       ("id") ON DELETE CASCADE
);

-- PK покрывает (model_id, *) → обратный индекс для FK enforcement
CREATE INDEX IF NOT EXISTS idx_model_has_permissions_permission_id ON "model_has_permissions" ("permission_id");

CREATE TABLE IF NOT EXISTS "model_has_roles" (
    "role_id"  BIGINT NOT NULL,
    "model_id" BIGINT NOT NULL,
    -- PK предотвращает дубликаты ролей у пользователя
    PRIMARY KEY ("model_id", "role_id"),
    CONSTRAINT "model_has_roles_role_id_foreign"  FOREIGN KEY ("role_id")  REFERENCES "roles" ("id") ON DELETE CASCADE,
    CONSTRAINT "model_has_roles_model_id_foreign" FOREIGN KEY ("model_id") REFERENCES "users" ("id") ON DELETE CASCADE
);

-- PK покрывает (model_id, *) → обратный индекс для FK enforcement
CREATE INDEX IF NOT EXISTS idx_model_has_roles_role_id ON "model_has_roles" ("role_id");

CREATE TABLE IF NOT EXISTS "personal_access_tokens" (
    "id"             BIGSERIAL    NOT NULL,
    "tokenable_type" VARCHAR(255) NOT NULL,
    "tokenable_id"   BIGINT       NOT NULL,
    "name"           TEXT         NOT NULL,
    "token"          VARCHAR(64)  NOT NULL,
    "abilities"      TEXT         NULL,
    "last_used_at"   TIMESTAMP    NULL,
    "expires_at"     TIMESTAMP    NULL,
    "created_at"     TIMESTAMP    NULL,
    "updated_at"     TIMESTAMP    NULL,
    PRIMARY KEY ("id"),
    UNIQUE ("token")
);

-- Поиск всех сессий пользователя (по типу + id)
CREATE INDEX IF NOT EXISTS idx_pat_tokenable ON "personal_access_tokens" ("tokenable_type", "tokenable_id");
-- Активные сессии пользователя: WHERE tokenable_id = ? AND expires_at > NOW()
CREATE INDEX IF NOT EXISTS idx_pat_active_sessions ON "personal_access_tokens" ("tokenable_id", "expires_at");
-- Очистка истёкших токенов по расписанию: WHERE expires_at < NOW()
CREATE INDEX IF NOT EXISTS idx_pat_expires_at ON "personal_access_tokens" ("expires_at");

CREATE TABLE IF NOT EXISTS "media" (
    "id"                    BIGSERIAL    NOT NULL,
    "model_type"            VARCHAR(255) NOT NULL,
    "model_id"              BIGINT       NOT NULL,
    "uuid"                  UUID         NULL,
    "collection_name"       VARCHAR(255) NOT NULL,
    "name"                  VARCHAR(255) NOT NULL,
    "file_name"             VARCHAR(255) NOT NULL,
    "mime_type"             VARCHAR(255) NULL,
    "disk"                  VARCHAR(255) NOT NULL,
    "conversions_disk"      VARCHAR(255) NULL,
    "size"                  BIGINT       NOT NULL,
    "manipulations"         JSON         NOT NULL,
    "custom_properties"     JSON         NOT NULL,
    "generated_conversions" JSON         NOT NULL,
    "responsive_images"     JSON         NOT NULL,
    "order_column"          INTEGER      NULL,
    "created_at"            TIMESTAMP    NULL,
    "updated_at"            TIMESTAMP    NULL,
    PRIMARY KEY ("id"),
    UNIQUE ("uuid")
);

-- Выборка по коллекции: WHERE model_type = ? AND model_id = ? AND collection_name = ?
CREATE INDEX IF NOT EXISTS idx_media_collection ON "media" ("model_type", "model_id", "collection_name");
-- Сортированная выборка: WHERE model_type = ? AND model_id = ? ORDER BY order_column
CREATE INDEX IF NOT EXISTS idx_media_ordered    ON "media" ("model_type", "model_id", "order_column");
-- Очистка файлов с конкретного диска: WHERE disk = 's3'
CREATE INDEX IF NOT EXISTS idx_media_disk       ON "media" ("disk");

--rollback SELECT 1;
