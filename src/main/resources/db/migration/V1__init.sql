-- Create new tables
CREATE TABLE member (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE phrase (
    id BIGINT NOT NULL AUTO_INCREMENT,
    end_index INTEGER NOT NULL,
    start_index INTEGER NOT NULL,
    member_id BIGINT,
    sentence_id BIGINT,
    meaning VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE phrase_tag (
    id BIGINT NOT NULL AUTO_INCREMENT,
    phrase_id BIGINT,
    tag_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE recognized_sentence (
    id BIGINT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id)
);

CREATE TABLE recognized_sentence_recognized_words (
    recognized_sentence_id BIGINT NOT NULL,
    recognized_words_id BIGINT NOT NULL UNIQUE
);

CREATE TABLE recognized_word (
    id BIGINT NOT NULL AUTO_INCREMENT,
    is_matched_with_transcription BOOLEAN NOT NULL,
    word VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE tag (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE transcription (
    id BIGINT NOT NULL AUTO_INCREMENT,
    url VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE transcription_sentence (
    id BIGINT NOT NULL AUTO_INCREMENT,
    start_point TIME(6),
    transcription_id BIGINT NOT NULL,
    content VARCHAR(255),
    PRIMARY KEY (id)
);
