package com.example.memic.transcription.dto;

import com.example.memic.transcription.domain.Scrap;

public record ScrapResponse(
        Long id,
        Long transcriptionId,
        String url
) {

    public static ScrapResponse fromEntity(Scrap scrap) {
        return new ScrapResponse(
                scrap.getId(),
                scrap.getTranscription().getId(),
                scrap.getTranscription().getUrl()
        );
    }
}
