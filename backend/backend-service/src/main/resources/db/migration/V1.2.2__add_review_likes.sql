CREATE TABLE review_like
(
    review_id       BIGSERIAL NOT NULL,
    liker_username  TEXT      NOT NULL,
    disposable_fake BOOLEAN   NOT NULL DEFAULT FALSE,
    CONSTRAINT pkey_review_like PRIMARY KEY (review_id, liker_username),
    CONSTRAINT fkey_review_like_review_id FOREIGN KEY (review_id) REFERENCES review (id) ON DELETE CASCADE,
    CONSTRAINT fkey_review_like_liker_username FOREIGN KEY (liker_username) REFERENCES user_account (username)
);

CREATE INDEX idx_review_like_disposable_fake ON review_like (disposable_fake);

CREATE TABLE review_like_aggregate
(
    review_id BIGSERIAL PRIMARY KEY NOT NULL,
    like_sum  INT                   NOT NULL,
    CONSTRAINT fkey_review_like_aggregate_review_id FOREIGN KEY (review_id) REFERENCES review (id) ON DELETE CASCADE
);

CREATE INDEX idx_review_like_aggregate_like_sum ON review_like_aggregate (like_sum);
