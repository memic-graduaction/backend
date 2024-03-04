package com.example.memic.recognizedSentence.repository;

import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import org.springframework.data.repository.Repository;

public interface RecognizedSentenceRepository extends Repository<RecognizedSentence, Long> {
    RecognizedSentence save(RecognizedSentence recognizedSentence);
}
