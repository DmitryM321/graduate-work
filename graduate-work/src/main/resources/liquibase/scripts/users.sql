-- liquibase formatted sql

-- changeset dmatveev:1
create table users(
    id                  SERIAL PRIMARY KEY,
    first_name          VARCHAR(255),
    last_name           VARCHAR(255),
    username            VARCHAR(255),
    password            VARCHAR(255) NOT NULL,
    enabled             BOOLEAN,
    phone               VARCHAR(255) NOT NULL,
    image_id            INTEGER  REFERENCES image (id),
    role                VARCHAR(255)
);