--liquibase formatted sql

--changeset ayushchenko:1
ALTER TABLE users
ADD COLUMN image varchar(64);

--changeset ayushchenko:2
ALTER TABLE users_aud
ADD COLUMN image varchar(64);