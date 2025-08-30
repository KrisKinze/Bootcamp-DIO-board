--liquibase formatted sql
--changeset junior:202408301200
--comment: add created_at column to cards table

ALTER TABLE CARDS ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

--rollback ALTER TABLE CARDS DROP COLUMN created_at;
