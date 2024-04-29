package com.example.memic.phrase.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.memic.phrase.dto.TagCreateRequest;
import jakarta.persistence.EntityExistsException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    void 중복된_태그가_저장되면_예외를_던진다() {
        //given
        final var tagName = "테스트";
        final var request = new TagCreateRequest(tagName);
        tagService.createTag(request);

        //when
        ThrowingCallable throwingCallable = () -> tagService.createTag(request);

        //then
        assertThatThrownBy(throwingCallable).isInstanceOf(EntityExistsException.class);
    }
}