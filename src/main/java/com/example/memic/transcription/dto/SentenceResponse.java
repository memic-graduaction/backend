package com.example.memic.transcription.dto;

import com.example.memic.transcription.domain.TranscriptionSentence;
import java.time.LocalTime;

public record SentenceResponse(
        Long id,
        LocalTime startPoint,
        String sentence
) {

    public static SentenceResponse fromEntity(TranscriptionSentence transcriptionSentence) {
        return new SentenceResponse(
                transcriptionSentence.getId(),
                transcriptionSentence.getStartPoint(),
                transcriptionSentence.getContent()
        );
    }
}
