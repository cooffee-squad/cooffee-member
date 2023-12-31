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

insert into member (name, email, password, phone, type, created_at, updated_at)
values ('dummy', 'dummy@test.com', '123', '010-1111-2222', 'NORMAL', CURRENT_DATE, CURRENT_DATE);