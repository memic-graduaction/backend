package com.example.memic.phrase.application;

import com.example.memic.phrase.domain.Tag;
import com.example.memic.phrase.domain.TagRepository;
import com.example.memic.phrase.dto.TagCreateRequest;
import com.example.memic.phrase.dto.TagCreateResponse;
import com.example.memic.phrase.dto.TagListResponse;
import jakarta.persistence.EntityExistsException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagCreateResponse createTag(final TagCreateRequest request) {
        if(tagRepository.existsByName(request.name())){
            throw new EntityExistsException("태그 이름이 이미 존재합니다.");
        }

        final Tag newTag = new Tag(request.name());
        final Tag saved = tagRepository.save(newTag);
        return new TagCreateResponse(saved.getId());
    }

    public List<TagListResponse> getTags() {
        List<Tag> tags = tagRepository.findAll();
        return TagListResponse.from(tags);
    }
}
