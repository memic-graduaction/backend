package com.example.memic.phrase.domain;


import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface PhraseRepository extends Repository<Phrase, Long> {

    Phrase save(Phrase phrase);

    default Phrase getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 표현이 없습니다. id: " + id));
    }

    Optional<Phrase> findById(Long id);
}
