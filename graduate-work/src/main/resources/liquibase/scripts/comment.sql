-- liquibase formatted sql

-- changeset dmatveev:2
create table comment
(
    id           BIGSERIAL PRIMARY KEY,
    comment         VARCHAR(255),
    created_at   VARCHAR(255),
    author_id     BIGINT  REFERENCES users (id),
    ads_id     BIGINT  REFERENCES ads (id)
);
