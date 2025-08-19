CREATE INDEX user_account_email_index ON user_account (email);

ALTER TABLE user_account
    DROP COLUMN id,
    DROP COLUMN about,
    ADD COLUMN display_name varchar(100)                NOT NULL DEFAULT 'Anonymous',
    ADD COLUMN created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    ADD COLUMN modified_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    ADD PRIMARY KEY (username);