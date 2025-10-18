INSERT INTO media_rating (media_id, username, media_flavour_id, score)
VALUES ((SELECT id FROM media WHERE title = 'Flow'), 'user7', 'THR1', 4.0);

INSERT INTO media_rating (media_id, username, media_flavour_id, score)
VALUES ((SELECT id FROM media WHERE title = 'Flow'), 'user7', 'GEN2', 5.0);

INSERT INTO media_rating (media_id, username, media_flavour_id, score)
VALUES ((SELECT id FROM media WHERE title = 'Flow'), 'user7', 'GEN4', 5.0);

INSERT INTO media_rating (media_id, username, media_flavour_id, score)
VALUES ((SELECT id FROM media WHERE title = 'Flow'), 'user7', 'EMO3', 3.0);

INSERT INTO media_rating (media_id, username, media_flavour_id, score)
VALUES ((SELECT id FROM media WHERE title = 'Flow'), 'user8', 'THR1', 4.0);

INSERT INTO media_rating (media_id, username, media_flavour_id, score)
VALUES ((SELECT id FROM media WHERE title = 'Flow'), 'user8', 'GEN2', 5.0);

INSERT INTO media_rating (media_id, username, media_flavour_id, score)
VALUES ((SELECT id FROM media WHERE title = 'Flow'), 'user9', 'GEN2', 4.5);
