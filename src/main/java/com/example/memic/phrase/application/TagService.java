package com.example.memic.phrase.application;

import com.example.memic.member.domain.Member;
import com.example.memic.phrase.domain.Tag;
import com.example.memic.phrase.domain.TagRepository;
import com.example.memic.phrase.dto.TagCreateRequest;
import com.example.memic.phrase.dto.TagCreateResponse;
import com.example.memic.phrase.dto.TagListResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public TagCreateResponse createTag(final Member member, final TagCreateRequest request) {
        final Tag newTag = new Tag(request.name(), member);
        final Tag saved = tagRepository.save(newTag);
        return new TagCreateResponse(saved.getId());
    }

    @Transactional(readOnly = true)
    public List<TagListResponse> getTags(final Member member) {
        List<Tag> tags = tagRepository.findAllByMember(member);
        return TagListResponse.from(tags);
    }
}
