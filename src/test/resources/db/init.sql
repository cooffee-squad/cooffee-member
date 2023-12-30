CREATE TABLE member
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100),
    email      VARCHAR(255),
    password   VARCHAR(255),
    phone      VARCHAR(100),
    type       VARCHAR(25),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

insert into member (name, email, password, phone, type, created_at, updated_at)
values ('dummy', 'dummy@test.com', '123', '010-1111-2222', 'NORMAL', CURRENT_DATE, CURRENT_DATE);