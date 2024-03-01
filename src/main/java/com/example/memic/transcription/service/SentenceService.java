package com.example.memic.transcription.service;

import com.example.memic.transcription.domain.Sentence;
import com.example.memic.transcription.domain.SentenceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SentenceService {

    private final SentenceRepository sentenceRepository;

    public SentenceService(SentenceRepository sentenceRepository) {
        this.sentenceRepository = sentenceRepository;
    }

    public Sentence findSentenceById(Long id) {
        return sentenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 문장이 없습니다."));
    }
}
