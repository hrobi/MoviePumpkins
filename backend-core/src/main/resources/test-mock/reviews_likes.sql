SET search_path = app;

INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user7'), 'richard-rider', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user7'), 'emily-wokerson', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user7'), 'chase-wood', false);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user7'), 'justin-case', false);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user7'), 'cameron-geller', true);
-- INSERT INTO review_like (review_id, username, is_liked)
-- VALUES ((SELECT r.id FROM review r JOIN media m ON m.id = r.media_id WHERE m.title = 'Dune: Part One' AND username = 'user7'), 'user7', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user7'), 'user8', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user7'), 'user9', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user7'), 'user10', true);

INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user8'), 'richard-rider', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user8'), 'emily-wokerson', false);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user8'), 'chase-wood', false);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user8'), 'justin-case', false);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user8'), 'cameron-geller', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user8'), 'user7', true);
-- INSERT INTO review_like (review_id, username, is_liked)
-- VALUES ((SELECT r.id FROM review r JOIN media m ON m.id = r.media_id WHERE m.title = 'Dune: Part One' AND username = 'user8'), 'user8', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user8'), 'user9', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user8'), 'user10', true);

INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user9'), 'richard-rider', false);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user9'), 'emily-wokerson', false);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user9'), 'chase-wood', false);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user9'), 'justin-case', false);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user9'), 'cameron-geller', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user9'), 'user7', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user9'), 'user8', true);
-- INSERT INTO review_like (review_id, username, is_liked)
-- VALUES ((SELECT r.id FROM review r JOIN media m ON m.id = r.media_id WHERE m.title = 'Dune: Part One' AND username = 'user9'), 'user9', true);
INSERT INTO review_like (review_id, username, is_liked)
VALUES ((SELECT r.id
         FROM review r
                  JOIN media m ON m.id = r.media_id
         WHERE m.title = 'Dune: Part One'
           AND username = 'user9'), 'user10', true);