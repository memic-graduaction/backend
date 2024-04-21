package com.example.memic.phrase.dto;

import java.util.List;

public record TranscriptionPhraseResponse(
        Long id,
        Long sentenceId,
        Integer startIndex,
        Integer endIndex,
        String phrase,
        String meaning,
        List<TagResponse> tags
) {

}
