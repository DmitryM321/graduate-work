-- liquibase formatted sql

-- changeset dmatveev:2
create table ads(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(255),
    price       INTEGER NOT NULL,
    author_id   INT REFERENCES  users (id),
    image_id    INTEGER  REFERENCES  image (id)
);