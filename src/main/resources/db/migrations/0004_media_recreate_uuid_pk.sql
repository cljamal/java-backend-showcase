--liquibase formatted sql

--changeset media_uuid:1
DROP INDEX IF EXISTS idx_media_collection;
DROP INDEX IF EXISTS idx_media_ordered;
DROP INDEX IF EXISTS idx_media_disk;
DROP TABLE IF EXISTS "media";

CREATE TABLE "media" (
    "id"                    UUID         NOT NULL DEFAULT gen_random_uuid(),
    "model_type"            VARCHAR(255) NOT NULL,
    "model_id"              BIGINT       NOT NULL,
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
    PRIMARY KEY ("id")
);

CREATE INDEX IF NOT EXISTS idx_media_collection ON "media" ("model_type", "model_id", "collection_name");
CREATE INDEX IF NOT EXISTS idx_media_ordered    ON "media" ("model_type", "model_id", "order_column");
CREATE INDEX IF NOT EXISTS idx_media_disk       ON "media" ("disk");

--rollback SELECT 1;