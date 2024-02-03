package com.example.memic.transcription.dto;

import com.example.memic.transcription.domain.Transcription;
import java.util.List;

public record TranscriptionResponse(
        Long id,
        String url,
        List<SentenceResponse> sentences
) {

    public static TranscriptionResponse fromEntity(Transcription transcription) {
        return new TranscriptionResponse(
                transcription.getId(),
                transcription.getUrl(),
                transcription.getSentences().stream()
                        .map(SentenceResponse::fromEntity)
                        .toList()
        );
    }
}
