package com.example.memic.transcription.domain;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranscriptionRepository extends JpaRepository<Transcription, Long> {

    default Transcription getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 번역본이 없습니다."));
    }
}
