package com.example.memic.phrase.dto;

import com.example.memic.phrase.domain.Phrase;
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

    public static List<TranscriptionPhraseResponse> from(final List<Phrase> phrases) {
        return phrases.stream()
                      .map(TranscriptionPhraseResponse::from)
                      .toList();
    }

    public static TranscriptionPhraseResponse from(final Phrase phrase) {
        return new TranscriptionPhraseResponse(
                phrase.getId(),
                phrase.getSentence().getId(),
                phrase.getStartIndex(),
                phrase.getEndIndex(),
                phrase.getContent(),
                phrase.getMeaning(),
                TagResponse.from(phrase.getTags())
        );
    }
}
