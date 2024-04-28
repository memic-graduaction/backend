package com.example.memic.phrase.dto;

import com.example.memic.phrase.domain.Phrase;
import java.util.List;

public record PhraseResponse(
        Long id,
        Long sentenceId,
        String url,
        String phrase,
        String meaning,
        List<TagResponse> tags
) {

    public static List<PhraseResponse> from(final List<Phrase> phrases) {
        return phrases.stream()
                      .map(PhraseResponse::from)
                      .toList();
    }

    public static PhraseResponse from(final Phrase phrase) {
        return new PhraseResponse(
                phrase.getId(),
                phrase.getSentence().getId(),
                phrase.getSentence().getTranscription().getUrl(),
                phrase.getContent(),
                phrase.getMeaning(),
                TagResponse.from(phrase.getTags())
        );
    }
}
