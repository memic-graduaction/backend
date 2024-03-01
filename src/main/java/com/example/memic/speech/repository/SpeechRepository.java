package com.example.memic.speech.repository;

import com.example.memic.speech.domain.Speech;
import org.springframework.data.repository.Repository;

public interface SpeechRepository extends Repository<Speech, Long> {
    Speech save(Speech speech);
}
