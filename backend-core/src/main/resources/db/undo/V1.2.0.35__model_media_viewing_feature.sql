DELETE
FROM flyway_schema_history
WHERE version = '1.2.0.35';

DROP VIEW Wmedia_score_by_flavour_view;
DROP TABLE user_media_flavour_score;
DROP TABLE media_flavour;
DROP TABLE user_report;
DROP VIEW user_review_liking_view;
DROP TABLE user_review_like;
DROP TABLE user_review;
DROP TABLE media_details_change_request;
DROP TABLE media_details;