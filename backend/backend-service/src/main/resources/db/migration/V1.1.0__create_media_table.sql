CREATE TABLE media
(
    id                     UUID         NOT NULL PRIMARY KEY,
    title                  VARCHAR(500) NOT NULL,
    plot                   VARCHAR(1000),
    release_year           INT,
    mpa_rating             VARCHAR(50),
    award_win_count        INT,
    award_nomination_count INT,
    directors              VARCHAR(500),
    writers                VARCHAR(500),
    actors                 VARCHAR(500),
    genres                 VARCHAR(500),
    poster_url             TEXT,
    imdb_id                VARCHAR(100) NOT NULL,
    format                 VARCHAR(100) NOT NULL,
    runtime                INT,
    number_of_seasons      INT,
    last_year              INT,
    version                BIGINT       NOT NULL DEFAULT 1
);
