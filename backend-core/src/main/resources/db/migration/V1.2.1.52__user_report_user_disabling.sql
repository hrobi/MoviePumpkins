CREATE TABLE IF NOT EXISTS user_report
(
    id          SERIAL                      NOT NULL,
    reporter    TEXT                        NOT NULL,
    reported    TEXT                        NOT NULL,
    review_id   SERIAL                      NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE user_report
    DROP CONSTRAINT IF EXISTS user_report_pkey,
    DROP CONSTRAINT IF EXISTS user_report_reporter_fkey,
    DROP CONSTRAINT IF EXISTS user_report_reported_fkey,
    DROP CONSTRAINT IF EXISTS user_report_review_id_fkey;

ALTER TABLE user_report
    ADD CONSTRAINT user_report_pkey PRIMARY KEY (id),
    ADD CONSTRAINT user_report_reporter_fkey FOREIGN KEY (reporter) REFERENCES user_account,
    ADD CONSTRAINT user_report_reported_fkey FOREIGN KEY (reported) REFERENCES user_account,
    ADD CONSTRAINT user_report_review_id_fkey FOREIGN KEY (review_id) REFERENCES review;

CREATE INDEX IF NOT EXISTS user_report_reported_idx ON user_report (reported);

DROP VIEW IF EXISTS user_report_aggregate_view;

CREATE VIEW user_report_aggregate_view AS
SELECT reported,
       count(*) as report_count
FROM user_report
GROUP BY reported
ORDER BY report_count DESC;

ALTER TABLE user_account
    ADD COLUMN IF NOT EXISTS enabled BOOLEAN NOT NULL DEFAULT true;

CREATE TABLE IF NOT EXISTS disabled_user
(
    username    TEXT                        NOT NULL,
    disabler    TEXT                        NOT NULL,
    reason      TEXT                        NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE disabled_user
    DROP CONSTRAINT IF EXISTS user_disabling_pkey,
    DROP CONSTRAINT IF EXISTS user_disabling_disabled_fkey,
    DROP CONSTRAINT IF EXISTS user_disabling_disabler_fkey;

ALTER TABLE disabled_user
    ADD CONSTRAINT user_disabling_pkey PRIMARY KEY (username),
    ADD CONSTRAINT user_disabling_username_fkey FOREIGN KEY (username) REFERENCES user_account,
    ADD CONSTRAINT user_disabling_disabler_fkey FOREIGN KEY (disabler) REFERENCES user_account;

CREATE TABLE IF NOT EXISTS user_reinstatement
(
    username    TEXT                        NOT NULL,
    reason      TEXT                        NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE user_reinstatement
    ADD CONSTRAINT user_reinstatement_pkey PRIMARY KEY (username),
    ADD CONSTRAINT user_reinstatement_username_fkey FOREIGN KEY (username) REFERENCES user_account;