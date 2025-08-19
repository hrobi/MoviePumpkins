DELETE FROM flyway_schema_history WHERE version = '1.1.1.29';

DROP INDEX user_account_email_index;

ALTER TABLE user_account
ADD COLUMN id    SERIAL PRIMARY KEY,
ADD COLUMN about TEXT NOT NULL default '',
DROP CONSTRAINT user_account_username_key,
DROP CONSTRAINT user_account_pkey,
DROP COLUMN display_name,
DROP COLUMN created_at,
DROP COLUMN modified_at;