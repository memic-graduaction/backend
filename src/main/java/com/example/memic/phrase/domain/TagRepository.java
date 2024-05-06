package com.example.memic.phrase.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface TagRepository extends Repository<Tag, Long> {

    List<Tag> findByIdIn(List<Long> ids);

    Tag save(Tag tag);

    List<Tag> findAll();
}
