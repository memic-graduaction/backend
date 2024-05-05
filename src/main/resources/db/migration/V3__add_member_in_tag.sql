ALTER TABLE tags ADD COLUMN member_id BIGINT NOT NULL;

ALTER TABLE tags
    ADD CONSTRAINT fk_tags_member
        FOREIGN KEY (member_id)
            REFERENCES members(id);

ALTER TABLE tags ADD CONSTRAINT unique_tag_per_member UNIQUE (name, member_id);
