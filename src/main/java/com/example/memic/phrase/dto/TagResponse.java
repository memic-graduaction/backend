package com.example.memic.phrase.dto;

import com.example.memic.phrase.domain.PhraseTag;
import java.util.List;

public record TagResponse(
        Long id,
        String name
) {

    public static List<TagResponse> from(final List<PhraseTag> tags) {
        return tags.stream()
                   .map(TagResponse::from)
                   .toList();
    }

    public static TagResponse from(final PhraseTag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getTag().getName()
        );
    }
}
