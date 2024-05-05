package com.example.memic.phrase.ui;

import com.example.memic.common.auth.Authorization;
import com.example.memic.member.domain.Member;
import com.example.memic.phrase.application.TagService;
import com.example.memic.phrase.dto.TagCreateRequest;
import com.example.memic.phrase.dto.TagCreateResponse;
import com.example.memic.phrase.dto.TagListResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/tags")
@RestController
public class TagController {

    private final TagService tagService;

    public TagController(final TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagCreateResponse> createTag(
            @Authorization Member member,
            @RequestBody TagCreateRequest request
    ) {
        TagCreateResponse response = tagService.createTag(member, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TagListResponse>> getTags(@Authorization Member member) {
        List<TagListResponse> responses = tagService.getTags(member);
        return ResponseEntity.ok(responses);
    }
}
