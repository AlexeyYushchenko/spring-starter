--liquibase formatted sql

--changeset ayushchenko:1
CREATE TABLE if not exists revision
(
        id SERIAL PRIMARY KEY ,
        timestamp BIGINT NOT NULL
);

--changeset ayushchenko:2
CREATE TABLE IF NOT EXISTS users_aud
(
    id BIGINT,
    rev INT REFERENCES revision (id),
    revtype SMALLINT ,
    username VARCHAR(64) NOT NULL UNIQUE ,
    birth_date DATE,
    firstname VARCHAR(64),
    lastname VARCHAR(64),
    role VARCHAR(32),
    company_id INT
);