-- TABLES AND VIEWS

CREATE TABLE IF NOT EXISTS media
(
    id                SERIAL                      NOT NULL,
    title             TEXT                        NOT NULL,
    short_description TEXT                        NOT NULL,
    directors         TEXT[]                      NOT NULL,
    writers           TEXT[]                      NOT NULL,
    actors            TEXT[]                      NOT NULL,
    release_year      SMALLINT                    NOT NULL,
    original_title    TEXT,
    countries         TEXT[],
    awards            TEXT[],
    awards_win_count  SMALLINT,
    nominations_count SMALLINT,
    other_details     JSONB,
    media_type        TEXT                        NOT NULL,
    version           INTEGER                     NOT NULL DEFAULT 0,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

CREATE TABLE IF NOT EXISTS media_modification
(
    id                SERIAL                      NOT NULL,
    media_id          SERIAL                      NOT NULL,
    username          varchar(100)                NOT NULL,
    title             TEXT,
    short_description TEXT,
    directors         TEXT[],
    writers           TEXT[],
    actors            TEXT[],
    release_year      SMALLINT,
    original_title    TEXT,
    countries         TEXT[],
    awards            TEXT[],
    awards_win_count  SMALLINT,
    nominations_count SMALLINT,
    other_details     JSONB,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

CREATE TABLE IF NOT EXISTS review
(
    id          SERIAL                      NOT NULL,
    media_id    SERIAL                      NOT NULL,
    username    VARCHAR(100)                NOT NULL,
    title       TEXT,
    content     TEXT,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

CREATE TABLE IF NOT EXISTS review_like
(
    id          SERIAL                      NOT NULL,
    review_id   SERIAL                      NOT NULL,
    username    VARCHAR(100)                NOT NULL,
    is_liked    BOOLEAN                     NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

CREATE TABLE IF NOT EXISTS media_flavour
(
    id                VARCHAR(5)                  NOT NULL,
    flavour_name      TEXT,
    short_description TEXT,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

CREATE TABLE IF NOT EXISTS media_rating
(
    id               SERIAL                      NOT NULL,
    username         VARCHAR(100)                NOT NULL,
    media_id         SERIAL                      NOT NULL,
    media_flavour_id VARCHAR(5)                  NOT NULL,
    score            SMALLINT,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

CREATE TABLE IF NOT EXISTS media_poster
(
    id   SERIAL NOT NULL,
    path TEXT   NOT NULL
);

CREATE TABLE IF NOT EXISTS media_modification_poster
(
    id   SERIAL NOT NULL,
    path TEXT   NOT NULL
);

DROP VIEW IF EXISTS review_likes_aggregate_view;

CREATE VIEW review_likes_aggregate_view AS
SELECT review_id                                   as review_id,
       COUNT(CASE WHEN is_liked THEN 1 ELSE 0 END) as likes,
       COUNT(CASE WHEN is_liked THEN 0 ELSE 1 END) as dislikes
FROM review_like
group by review_id;

DROP VIEW IF EXISTS media_rating_aggregate_view;

CREATE VIEW media_rating_aggregate_view AS
SELECT media_id,
       media_flavour_id,
       AVG(score) as average_score
FROM media_rating
group by media_id, media_flavour_id;

-- DROP CONSTRAINTS IF EXIST (lifo)

ALTER TABLE media_modification_poster
    DROP CONSTRAINT IF EXISTS media_modification_poster_pkey,
    DROP CONSTRAINT IF EXISTS media_modification_poster_id_fkey;

ALTER TABLE media_poster
    DROP CONSTRAINT IF EXISTS media_poster_pkey,
    DROP CONSTRAINT IF EXISTS media_poster_id_fkey;

ALTER TABLE media_rating
    DROP CONSTRAINT IF EXISTS media_rating_pkey,
    DROP CONSTRAINT IF EXISTS media_rating_username_fkey,
    DROP CONSTRAINT IF EXISTS media_rating_media_id_fkey,
    DROP CONSTRAINT IF EXISTS media_rating_media_flavour_id_fkey;

ALTER TABLE media_flavour
    DROP CONSTRAINT IF EXISTS media_flavour_pkey;

ALTER TABLE review_like
    DROP CONSTRAINT IF EXISTS review_like_pkey,
    DROP CONSTRAINT IF EXISTS review_like_user_review_id_fkey,
    DROP CONSTRAINT IF EXISTS review_like_username_fkey;

ALTER TABLE review
    DROP CONSTRAINT IF EXISTS review_pkey,
    DROP CONSTRAINT IF EXISTS review_media_id_fkey,
    DROP CONSTRAINT IF EXISTS review_username_fkey;

ALTER TABLE media_modification
    DROP CONSTRAINT IF EXISTS media_modification_pkey,
    DROP CONSTRAINT IF EXISTS media_modification_media_id_fkey,
    DROP CONSTRAINT IF EXISTS media_modification_username_fkey;

ALTER TABLE media
    DROP CONSTRAINT IF EXISTS media_pkey;

-- ADD CONSTRAINTS (fifo)

ALTER TABLE media
    ADD CONSTRAINT media_pkey PRIMARY KEY (id);

ALTER TABLE media_modification
    ADD CONSTRAINT media_modification_pkey PRIMARY KEY (id),
    ADD CONSTRAINT media_modification_media_id_fkey FOREIGN KEY (media_id) REFERENCES media,
    ADD CONSTRAINT media_modification_username_fkey FOREIGN KEY (username) REFERENCES user_account;

ALTER TABLE review
    ADD CONSTRAINT review_pkey PRIMARY KEY (id),
    ADD CONSTRAINT review_media_id_fkey FOREIGN KEY (media_id) REFERENCES media,
    ADD CONSTRAINT review_username_fkey FOREIGN KEY (username) REFERENCES user_account;

ALTER TABLE review_like
    ADD CONSTRAINT review_like_pkey PRIMARY KEY (id),
    ADD CONSTRAINT review_like_user_review_id_fkey FOREIGN KEY (review_id) REFERENCES review,
    ADD CONSTRAINT review_like_username_fkey FOREIGN KEY (username) REFERENCES user_account;

ALTER TABLE media_flavour
    ADD CONSTRAINT media_flavour_pkey PRIMARY KEY (id);

ALTER TABLE media_rating
    ADD CONSTRAINT media_rating_pkey PRIMARY KEY (id),
    ADD CONSTRAINT media_rating_username_fkey FOREIGN KEY (username) REFERENCES user_account,
    ADD CONSTRAINT media_rating_media_id_fkey FOREIGN KEY (media_id) REFERENCES media,
    ADD CONSTRAINT media_rating_media_flavour_id_fkey FOREIGN KEY (media_flavour_id) REFERENCES media_flavour;

ALTER TABLE media_poster
    ADD CONSTRAINT media_poster_pkey PRIMARY KEY (id),
    ADD CONSTRAINT media_poster_id_fkey FOREIGN KEY (id) REFERENCES media;

ALTER TABLE media_modification_poster
    ADD CONSTRAINT media_modification_poster_pkey PRIMARY KEY (id),
    ADD CONSTRAINT media_modification_poster_id_fkey FOREIGN KEY (id) REFERENCES media_modification;