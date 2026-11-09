CREATE TABLE review
(
    id         BIGSERIAL PRIMARY KEY    NOT NULL,
    title      VARCHAR(250)             NOT NULL,
    content    VARCHAR(1500)            NOT NULL,
    reviewer   TEXT                     NOT NULL,
    media_id   SERIAL                   NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE review
    ADD CONSTRAINT fkey_review_reviewer FOREIGN KEY (reviewer) REFERENCES user_account (username),
    ADD CONSTRAINT fkey_review_media_id FOREIGN KEY (media_id) REFERENCES media (id),
    ADD CONSTRAINT ukey_review_media_id_reviewer UNIQUE (media_id, reviewer);

CREATE INDEX idx_review_media_id_updated_at ON review (media_id, updated_at);