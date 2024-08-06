-- Changeset me:1
CREATE TABLE translations
(
    id                           SERIAL PRIMARY KEY,
    ip_address                   VARCHAR(255) NOT NULL,
    original_string_to_translate TEXT         NOT NULL,
    translated_string            TEXT         NOT NULL
);
