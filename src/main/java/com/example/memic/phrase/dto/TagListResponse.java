package com.example.memic.phrase.dto;

import com.example.memic.phrase.domain.Tag;
import java.util.List;

public record TagListResponse(
        Long id,
        String name
) {

    public static List<TagListResponse> from(final List<Tag> tags) {
        return tags.stream()
                   .map(TagListResponse::from)
                   .toList();

    }

    public static TagListResponse from(final Tag tag) {
        return new TagListResponse(
                tag.getId(),
                tag.getName()
        );
    }
}
