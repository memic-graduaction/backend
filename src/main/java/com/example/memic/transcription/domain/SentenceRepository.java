package com.example.memic.transcription.domain;


import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface SentenceRepository extends Repository<Sentence, Long> {

    Optional<Sentence> findById(Long id);

    default Sentence getById(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 문장이 없습니다."));
    }
}
