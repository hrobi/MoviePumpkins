ALTER TABLE user_account
    ADD COLUMN disposable_fake BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX idx_user_account_disposable_fake ON user_account (disposable_fake);

ALTER TABLE review
    ADD COLUMN disposable_fake BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX idx_review_disposable_fake ON review (disposable_fake);