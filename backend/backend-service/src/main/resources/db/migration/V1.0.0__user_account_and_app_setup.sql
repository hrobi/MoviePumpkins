CREATE TABLE user_account
(
    username     TEXT PRIMARY KEY,
    first_name   TEXT NOT NULL,
    last_name    TEXT NOT NULL,
    display_name TEXT NOT NULL,
    email        TEXT NOT NULL UNIQUE,
    role         TEXT NOT NULL default 'REVIEWER',
    enabled      BOOLEAN
);