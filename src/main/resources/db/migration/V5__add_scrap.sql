CREATE TABLE scrap
(
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    transcription_id BIGINT       NOT NULL,
    member_id        BIGINT       NOT NULL,
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE scrap ADD UNIQUE (member_id, transcription_id);
