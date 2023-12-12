CREATE TABLE member
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(100),
    email     VARCHAR(255),
    password  VARCHAR(255),
    phone     VARCHAR(100),
    type      VARCHAR(25),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);