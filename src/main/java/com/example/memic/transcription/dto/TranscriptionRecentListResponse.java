package com.example.memic.transcription.dto;

import com.example.memic.transcription.domain.Transcription;

public record TranscriptionRecentListResponse(
        Long id,
        String url
) {

    public static TranscriptionRecentListResponse fromEntity(final Transcription transcription) {
        return new TranscriptionRecentListResponse(
                transcription.getId(),
                transcription.getUrl()
        );
    }
}
