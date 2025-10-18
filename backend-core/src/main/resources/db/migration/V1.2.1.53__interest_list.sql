CREATE TABLE IF NOT EXISTS interest_list
(
    id          SERIAL                      NOT NULL,
    username    TEXT                        NOT NULL,
    media_id    SERIAL                      NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP,
    modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()::TIMESTAMP
);

ALTER TABLE interest_list
    DROP CONSTRAINT IF EXISTS interest_list_pkey,
    DROP CONSTRAINT IF EXISTS interest_list_username_fkey,
    DROP CONSTRAINT IF EXISTS interest_list_media_id_fkey;


ALTER TABLE interest_list
    ADD CONSTRAINT interest_list_pkey PRIMARY KEY (id),
    ADD CONSTRAINT interest_list_username_fkey FOREIGN KEY (username) REFERENCES user_account,
    ADD CONSTRAINT interest_list_media_id_fkey FOREIGN KEY (media_id) REFERENCES media;
