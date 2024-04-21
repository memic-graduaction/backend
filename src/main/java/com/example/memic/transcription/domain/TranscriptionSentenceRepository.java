package com.example.memic.transcription.domain;


import com.example.memic.phrase.application.IdWrapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface TranscriptionSentenceRepository extends Repository<TranscriptionSentence, Long> {


    Optional<TranscriptionSentence> findById(Long id);

    default TranscriptionSentence getById(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 문장이 없습니다."));
    }

    List<IdWrapper> getIdsByTranscriptionId(Long transcriptionId);
}
