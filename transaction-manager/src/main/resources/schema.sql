DROP TABLE IF EXISTS blockchain_schema.transaction_responses;
DROP TABLE IF EXISTS blockchain_schema.transaction_requests;
DROP TABLE IF EXISTS blockchain_schema.transactions;
DROP TABLE IF EXISTS blockchain_schema.blocks;
DROP TABLE IF EXISTS blockchain_schema.users;

CREATE SCHEMA IF NOT EXISTS blockchain_schema;

CREATE TABLE IF NOT EXISTS blockchain_schema.users
(
    id        UUID PRIMARY KEY    NOT NULL,
    login     varchar(255) UNIQUE NOT NULL,
    name      varchar(255)        NOT NULL,
    password  varchar(255)        NOT NULL,
    user_role varchar(255)        NOT NULL
);

CREATE TABLE IF NOT EXISTS blockchain_schema.blocks
(
    id            SERIAL PRIMARY KEY  NOT NULL,
    hash          varchar(255) UNIQUE NOT NULL,
    previous_hash varchar(255)        NOT NULL,
    moderator_id  UUID                NOT NULL REFERENCES blockchain_schema.users (id),
    merkle_root   varchar(255)        NOT NULL,
    nonce         INTEGER             NOT NULL
);

CREATE TABLE IF NOT EXISTS blockchain_schema.transactions
(
    id           SERIAL PRIMARY KEY NOT NULL,
    block_id     INTEGER            NOT NULL REFERENCES blockchain_schema.blocks (id),
    temporary_id UUID               NOT NULL,
    cache        INTEGER            NOT NULL,
    electric     INTEGER            NOT NULL,
    resident_id  UUID               NOT NULL REFERENCES blockchain_schema.users (id),
    retailer_id  UUID               NOT NULL REFERENCES blockchain_schema.users (id),
    action       varchar(255)       NOT NULL,
    price        INTEGER            NOT NULL,
    amount       INTEGER            NOT NULL
);

CREATE TABLE IF NOT EXISTS blockchain_schema.transaction_requests
(
    id          UUID PRIMARY KEY NOT NULL,
    resident_id UUID             NOT NULL REFERENCES blockchain_schema.users (id),
    action      varchar(255)     NOT NULL,
    price       INTEGER          NOT NULL,
    amount      INTEGER          NOT NULL
);

CREATE TABLE IF NOT EXISTS blockchain_schema.transaction_responses
(
    id          UUID PRIMARY KEY NOT NULL,
    request_id  UUID             NOT NULL REFERENCES blockchain_schema.transaction_requests (id),
    retailer_id UUID             NOT NULL REFERENCES blockchain_schema.users (id)
);
