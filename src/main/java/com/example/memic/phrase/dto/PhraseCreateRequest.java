package com.example.memic.phrase.dto;

import java.util.List;

public record PhraseCreateRequest(
        Long sentenceId,
        Integer startIndex,
        Integer endIndex,
        String meaning,
        List<Long> tagIds
) {

}
