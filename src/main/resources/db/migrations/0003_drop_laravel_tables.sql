--liquibase formatted sql

--changeset migrations:3
DROP TABLE IF EXISTS "cache";
DROP TABLE IF EXISTS "cache_locks";
DROP TABLE IF EXISTS "jobs";
DROP TABLE IF EXISTS "job_batches";
DROP TABLE IF EXISTS "failed_jobs";
DROP TABLE IF EXISTS "migrations";

--rollback SELECT 1;
