-- liquibase formatted sql

-- changeset dmatveev:3
create table image(
    id         SERIAL PRIMARY KEY,
    file_size  BIGINT,
    media_type VARCHAR(255),
    data       BYTEA
);