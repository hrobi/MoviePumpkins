SET search_path = app;

INSERT INTO media (title, short_description, directors, writers, actors, release_year, original_title, countries,
                   awards,
                   awards_win_count, nominations_count, other_details, media_type)
VALUES ('Dune: Part One',
        'Paul Atreides arrives on Arrakis after his father accepts the stewardship of the dangerous planet. However, chaos ensues after a betrayal as forces clash to control melange, a precious resource.',
        '{"Denis Villeneuve"}',
        '{"Jon Spaihts", "Denis Villeneuve", "Eric Roth"}',
        '{"Timothée Chalamet", "Rebecca Ferguson", "Zendaya"}',
        2021,
        'Dune: Part One',
        '{"United States", "Canada"}',
        '{"6 Oscars"}',
        175,
        298,
        '{
          "lengthInMinutes": 155
        }',
        'MOVIE');

INSERT INTO media (title, short_description, directors, writers, actors, release_year, original_title, countries,
                   awards,
                   awards_win_count, nominations_count, other_details, media_type)
VALUES ('Jurassic Park',
        'An industrialist invites some experts to visit his theme park of cloned dinosaurs. After a power failure, the creatures run loose, putting everyone''s lives, including his grandchildren''s, in danger.',
        '{"Steven Spielberg"}',
        '{"Michael Crichton", "David Koepp"}',
        '{"Sam Neill", "Laura Dern", "Jeff Goldblum"}',
        1993,
        'Jurassic Park',
        '{"United States"}',
        '{"3 Oscars"}',
        44,
        27,
        '{
          "lengthInMinutes": 127
        }',
        'MOVIE');

INSERT INTO media (title, short_description, directors, writers, actors, release_year, original_title, countries,
                   awards,
                   awards_win_count, nominations_count, other_details, media_type)
VALUES ('Arcane',
        'Amid the stark discord of twin cities Piltover and Zaun, two sisters fight on rival sides of a war between magic technologies and clashing convictions.',
        '{}',
        '{}',
        '{"Kevin Alejandro", "Hailee Steinfeld", "Ella Purnell"}',
        2021,
        'Arcane',
        '{"United States", "France"}',
        '{"4 Primetime Emmys"}',
        33,
        13,
        '{
          "seasonCount": 2,
          "endYear": 2024
        }',
        'SERIES');

INSERT INTO media (title, short_description, directors, writers, actors, release_year, original_title, countries,
                   awards,
                   awards_win_count, nominations_count, other_details, media_type)
VALUES ('Flow',
        'Cat is a solitary animal, but as its home is devastated by a great flod, he finds refuge on a boat populated by various species, and will have to team up with them despite their differences.',
        '{"Gints Zilbalodis"}',
        '{"Gints Zilbalodis", "Matiss Kaza", "Ron Dyens"}',
        '{}',
        2021,
        'Straume',
        '{"Latvia", "Belgium", "France"}',
        '{"1 Oscar"}',
        55,
        74,
        '{
          "lengthInMinutes": 85
        }',
        'MOVIE');

INSERT INTO media (title, short_description, directors, writers, actors, release_year, original_title, countries,
                   awards,
                   awards_win_count, nominations_count, other_details, media_type)
VALUES ('Friends',
        'Friends LOL. Everyone knows what this is about lmao.',
        '{}',
        '{}',
        '{}',
        1994,
        null,
        '{"United States"}',
        '{"6 Primetime Emmys"}',
        79,
        231,
        '{
          "endYear": 2004,
          "seasonCount": 10
        }',
        'SERIES');
