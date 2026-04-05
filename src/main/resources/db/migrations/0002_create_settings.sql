--liquibase formatted sql

--changeset migrations:1
CREATE TABLE IF NOT EXISTS "settings" (
    "key"         VARCHAR(255) NOT NULL,
    "value"       TEXT         NULL,
    "type"        VARCHAR(20)  NOT NULL DEFAULT 'string',
    "group"       VARCHAR(50)  NOT NULL DEFAULT 'general',
    "label"       VARCHAR(255) NULL,
    "description" TEXT         NULL,
    "created_at"  TIMESTAMP    NULL,
    "updated_at"  TIMESTAMP    NULL,
    PRIMARY KEY ("key")
);

-- Составной: WHERE group = ? AND type = ? (один индекс лучше двух отдельных)
CREATE INDEX IF NOT EXISTS idx_settings_group_type ON "settings" ("group", "type");
-- Отдельный для запросов только по group: WHERE group = ?
CREATE INDEX IF NOT EXISTS idx_settings_group      ON "settings" ("group");

--rollback DROP TABLE IF EXISTS "settings";
