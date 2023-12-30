CREATE TABLE member
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT,
    email      TEXT,
    password   TEXT,
    phone      TEXT,
    type       TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);