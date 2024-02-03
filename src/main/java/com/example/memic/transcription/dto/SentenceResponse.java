package com.example.memic.transcription.dto;

import com.example.memic.transcription.domain.Sentence;
import java.time.LocalTime;

public record SentenceResponse(
        Long id,
        LocalTime startPoint,
        String sentence
) {

    public static SentenceResponse fromEntity(Sentence sentence) {
        return new SentenceResponse(
                sentence.getId(),
                sentence.getStartPoint(),
                sentence.getContent()
        );
    }
}
