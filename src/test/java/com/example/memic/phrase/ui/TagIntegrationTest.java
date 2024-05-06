package com.example.memic.phrase.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.common.EnableMockMvc;
import com.example.memic.common.database.NoTransactionExtension;
import com.example.memic.member.dto.MemberSignInRequest;
import com.example.memic.member.dto.MemberSignInResponse;
import com.example.memic.phrase.dto.TagCreateRequest;
import com.example.memic.phrase.dto.TagCreateResponse;
import com.example.memic.phrase.dto.TagListResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("NonAsciiCharacters")
@EnableMockMvc
@SpringBootTest
@ExtendWith(NoTransactionExtension.class)
class TagIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @ExtendWith(NoTransactionExtension.class)
    void 태그를_저장하고_목록을_가져온다() throws Exception {
        final var memberSignInRequest = new MemberSignInRequest("test@gmail.com", "123");

        final var loginResult = mockMvc.perform(post("/v1/members/sign-up")
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(memberSignInRequest)))
                                       .andDo(print())
                                       .andExpect(status().isOk())
                                       .andReturn();
        final var memberSignInResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(),
                MemberSignInResponse.class);

        final var request = new TagCreateRequest("테스트");

        final var responseFromPost = mockMvc.perform(post("/v1/tags")
                                                    .header("Authorization", memberSignInResponse.accessToken())
                                                    .content(objectMapper.writeValueAsString(request))
                                                    .contentType(MediaType.APPLICATION_JSON))
                                            .andDo(print())
                                            .andExpect(status().isOk())
                                            .andExpect(jsonPath("$.id").isNumber())
                                            .andReturn();

        final var tagCreateResponse = objectMapper.readValue(
                responseFromPost.getResponse().getContentAsString(),
                TagCreateResponse.class);

        final var responseFromGet = mockMvc.perform(get("/v1/tags")
                                                   .header("Authorization", memberSignInResponse.accessToken()))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        TypeReference<List<TagListResponse>> listType = new TypeReference<>() {
        };
        final var tagListResponse = objectMapper.readValue(responseFromGet.getResponse().getContentAsString(),
                listType);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(tagCreateResponse.id()).isEqualTo(tagListResponse.get(0).id());
            softly.assertThat(tagListResponse.get(0).name()).isEqualTo(request.name());
        });
    }
}