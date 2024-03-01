package com.example.memic.transcription.domain;


import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface SentenceRepository extends Repository<Sentence, Long> {

    Optional<Sentence> findById(Long id);
}
