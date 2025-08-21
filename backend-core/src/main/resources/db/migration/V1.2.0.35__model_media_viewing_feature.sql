CREATE TABLE media_details
(
    id                SERIAL                      NOT NULL,
    title             TEXT                        NOT NULL,
    short_description TEXT                        NOT NULL,
    directors         TEXT[]                      NOT NULL,
    writers           TEXT[]                      NOT NULL,
    actors            TEXT[]                      NOT NULL,
    release_year      SMALLINT                    NOT NULL,
    original_title    TEXT,
    country           TEXT,
    awards            TEXT[],
    awards_win_count  SMALLINT,
    nominations_count SMALLINT,
    other_details     JSONB,
    media_type        TEXT                        NOT NULL,
    version           INTEGER                     NOT NULL DEFAULT 0,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE media_details
    ADD CONSTRAINT media_details_pkey PRIMARY KEY (id);

CREATE TABLE media_details_change_request
(
    id                SERIAL                      NOT NULL,
    media_id          SERIAL                      NOT NULL,
    user_id           SERIAL                      NOT NULL,
    title             TEXT,
    short_description TEXT,
    directors         TEXT[],
    writers           TEXT[],
    actors            TEXT[],
    release_year      SMALLINT,
    original_title    TEXT,
    country           TEXT,
    awards            TEXT[],
    awards_win_count  SMALLINT,
    nominations_count SMALLINT,
    other_details     JSONB,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE media_details_change_request
    ADD CONSTRAINT media_details_change_requests_pkey PRIMARY KEY (id),
    ADD CONSTRAINT media_details_change_requests_media_id_fkey FOREIGN KEY (media_id) REFERENCES media_details,
    ADD CONSTRAINT media_details_change_requests_user_id_fkey FOREIGN KEY (user_id) REFERENCES user_account;

CREATE TABLE user_review
(
    id          SERIAL                      NOT NULL,
    media_id    SERIAL                      NOT NULL,
    user_id     SERIAL                      NOT NULL,
    title       TEXT,
    content     TEXT,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE user_review
    ADD CONSTRAINT user_review_pkey PRIMARY KEY (id),
    ADD CONSTRAINT user_review_media_id_fkey FOREIGN KEY (media_id) REFERENCES media_details,
    ADD CONSTRAINT user_review_user_id_fkey FOREIGN KEY (user_id) REFERENCES user_account;

CREATE TABLE user_review_like
(
    id             SERIAL                      NOT NULL,
    user_review_id SERIAL                      NOT NULL,
    user_id        SERIAL                      NOT NULL,
    is_liked       BOOLEAN                     NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE user_review_like
    ADD CONSTRAINT user_review_like_pkey PRIMARY KEY (id),
    ADD CONSTRAINT user_review_like_user_review_id_fkey FOREIGN KEY (user_review_id) REFERENCES user_review,
    ADD CONSTRAINT user_review_like_user_id_fkey FOREIGN KEY (user_id) REFERENCES user_review;


CREATE VIEW user_review_liking_view AS
SELECT user_review_id                              as user_review_id,
       COUNT(CASE WHEN is_liked THEN 1 ELSE 0 END) as likes,
       COUNT(CASE WHEN is_liked THEN 0 ELSE 1 END) as dislikes
FROM user_review_like
group by user_review_id;


CREATE TABLE media_flavour
(
    id                VARCHAR(5)                  NOT NULL,
    flavour_name      TEXT,
    short_description TEXT,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE media_flavour
    ADD CONSTRAINT media_flavour_pkey PRIMARY KEY (id);

CREATE TABLE user_media_flavour_score
(
    id               SERIAL                      NOT NULL,
    user_id          SERIAL                      NOT NULL,
    media_id         SERIAL                      NOT NULL,
    media_flavour_id SERIAL                      NOT NULL,
    score            SMALLINT,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE user_media_flavour_score
    ADD CONSTRAINT user_media_flavour_score_pkey PRIMARY KEY (id),
    ADD CONSTRAINT user_media_flavour_score_user_id_fkey FOREIGN KEY (user_id) REFERENCES user_account,
    ADD CONSTRAINT user_media_flavour_score_media_id_fkey FOREIGN KEY (media_id) REFERENCES media_details,
    ADD CONSTRAINT user_media_flavour_score_media_flavour_id_fkey FOREIGN KEY (media_flavour_id) REFERENCES media_flavour;

CREATE VIEW media_score_by_flavour_view AS
SELECT media_id,
       media_flavour_id,
       AVG(score) as average_score
FROM user_media_flavour_score
group by media_id, media_flavour_id;