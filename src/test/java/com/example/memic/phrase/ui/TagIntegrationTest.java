package com.example.memic.phrase.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.common.EnableMockMvc;
import com.example.memic.phrase.dto.TagCreateRequest;
import com.example.memic.phrase.dto.TagCreateResponse;
import com.example.memic.phrase.dto.TagListResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("NonAsciiCharacters")
@EnableMockMvc
@SpringBootTest
class TagIntegrationTest {

    @Autowired
    private ObjectMapper objectmapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 태그를_저장하고_목록을_가져온다() throws Exception{
        final var request = new TagCreateRequest("테스트");

        final var responseFromPost = mockMvc.perform(post("/v1/tags")
                        .content(objectmapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        final var tagCreateResponse = objectmapper.readValue(
                responseFromPost.getResponse().getContentAsString(),
                TagCreateResponse.class);

        final var responseFromGet = mockMvc.perform(get("/v1/tags"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<TagListResponse>> listType = new TypeReference<>(){};
        final var tagListResponse = objectmapper.readValue(responseFromGet.getResponse().getContentAsString(), listType);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(tagCreateResponse.id()).isEqualTo(tagListResponse.get(0).id());
            softly.assertThat(tagListResponse.get(0).name()).isEqualTo(request.name());
        });
    }
}