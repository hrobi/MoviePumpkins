DELETE
FROM flyway_schema_history
WHERE version = '1.1.0.16';

DROP TABLE user_account;
DROP TYPE user_role;