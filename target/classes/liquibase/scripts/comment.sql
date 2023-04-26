-- liquibase formatted sql

-- changeset dmatveev:2
create table comment(
    id           SERIAL PRIMARY KEY,
    created_at   TIMESTAMP NOT NULL,
    text         VARCHAR(255),
    author_id    INTEGER  REFERENCES users (id),
    ads_id       INTEGER  REFERENCES ads (id)
);
