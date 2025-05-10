CREATE TYPE user_role AS ENUM ('REVIEWER', 'SUPERVISOR', 'ADMIN');

CREATE TABLE user_account
(
    id        SERIAL PRIMARY KEY,
    username  varchar(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email     VARCHAR(320) NOT NULL UNIQUE,
    about     TEXT         NOT NULL,
    role      user_role    NOT NULL default 'REVIEWER'
);