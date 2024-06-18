ALTER TABLE transcription ADD COLUMN transcribed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

CREATE TABLE transcription_member (
    id BIGINT NOT NULL AUTO_INCREMENT,
    transcription_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);
