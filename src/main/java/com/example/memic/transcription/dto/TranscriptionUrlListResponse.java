package com.example.memic.transcription.dto;

import com.example.memic.transcription.domain.Transcription;
import java.util.List;

public record TranscriptionUrlListResponse(
        Long id,
        String url
) {

    public static List<TranscriptionUrlListResponse> from(final List<Transcription> transcriptions) {
        return transcriptions.stream()
                             .map(TranscriptionUrlListResponse::from)
                             .toList();
    }

    public static TranscriptionUrlListResponse from(final Transcription transcription) {
        return new TranscriptionUrlListResponse(
                transcription.getId(),
                transcription.getUrl()
        );
    }
}
