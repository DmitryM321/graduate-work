-- liquibase formatted sql

-- changeset dmatveev:1
create table users(
    id                       BIGSERIAL PRIMARY KEY,
    first_name              VARCHAR(255),
    last_name              VARCHAR(255),
    user_name                    VARCHAR(255),
    password_auth            VARCHAR(255),
    phone                   VARCHAR(255),
    image_id                BIGINT  REFERENCES image (id),
    role_auth     VARCHAR(255)
);