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