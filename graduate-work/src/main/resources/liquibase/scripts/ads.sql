-- liquibase formatted sql

-- changeset dmatveev:2
create table ads
(
     id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(255),
    price       INTEGER,
    author_id   BIGINT  REFERENCES  users (id),
    image_id    BIGINT  REFERENCES  image (id)
);