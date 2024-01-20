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
    updated_at      TIMESTAMP,
    confirm_flag    BOOLEAN
);

insert into member (name, email, password, phone, main_address, sub_address, zipcode, type, confirm_flag, created_at, updated_at)
values ('dummy', 'dummy@test.com', '$2a$10$MoAYX5AHJhPMWz2ki5/OC.iYMJ1uoT26/vd58WrcWbfRDB19gxqRK', '010-1111-2222', '서울특별시 강남구', '논현동 어딘가', 12345,'NORMAL', true, CURRENT_DATE, CURRENT_DATE);
