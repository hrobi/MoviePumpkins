CREATE INDEX IF NOT EXISTS review_modified_at_idx ON review (modified_at);
CREATE INDEX IF NOT EXISTS review_media_id_username_idx ON review (media_id, username);
CREATE INDEX IF NOT EXISTS review_like_review_id_username_idx ON review_like (review_id, username);

DROP VIEW IF EXISTS media_rating_aggregate_view;

ALTER TABLE IF EXISTS media_rating
    ALTER COLUMN score TYPE FLOAT;

CREATE VIEW media_rating_aggregate_view AS
SELECT media_id,
       media_flavour_id,
       AVG(score)      as average_score,
       COUNT(username) as rater_count
FROM media_rating
group by media_id, media_flavour_id;