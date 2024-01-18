CREATE TABLE member
(
    id              BIGSERIAL PRIMARY KEY,
    name            TEXT,
    email           TEXT,
    password        TEXT,
    phone           TEXT,
    main_address    TEXT,
    sub_address     TEXT,
    zipcode         INTEGER,
    type            TEXT,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP
);

insert into member (name, email, password, phone, main_address, sub_address, zipcode, type, created_at, updated_at)
values ('dummy', 'dummy@test.com', '$2a$10$MoAYX5AHJhPMWz2ki5/OC.iYMJ1uoT26/vd58WrcWbfRDB19gxqRK', '010-1111-2222', '서울특별시 강남구', '논현동 어딘가', 12345,'NORMAL', CURRENT_DATE, CURRENT_DATE);
