package com.example.memic.phrase.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.common.EnableMockMvc;
import com.example.memic.common.database.NoTransactionExtension;
import com.example.memic.member.dto.MemberSignInRequest;
import com.example.memic.member.dto.MemberSignInResponse;
import com.example.memic.phrase.dto.PhraseCreateRequest;
import com.example.memic.phrase.dto.TagResponse;
import com.example.memic.phrase.dto.TranscriptionPhraseResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.sql.DataSource;
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
class PhraseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSource dataSource;

    @Test
    @ExtendWith(NoTransactionExtension.class)
    void 특정_영상의_표현을_조회한다() throws Exception {
        Long transcriptionId = 1L;
        try (final var connection = dataSource.getConnection()) {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            connection.createStatement().execute("INSERT INTO transcription (id, url) VALUES (1, 'test.com')");
            connection.createStatement().execute("INSERT INTO transcription_sentence (id, start_point, content, transcription_id) VALUES (1, '00:00:00', 'hello world', 1)");
            connection.createStatement().execute("INSERT INTO tag (id, name, member_id) VALUES (1, 'tag1', 1)");
            connection.createStatement().execute("INSERT INTO tag (id, name, member_id) VALUES (2, 'tag2', 1)");
            connection.createStatement().execute("INSERT INTO member (id, email, password) VALUES (1, 'test@gmail.com', '123')");
        }

        var memberSignInRequest = new MemberSignInRequest("test@gmail.com", "123");

        final var loginResult = mockMvc.perform(post("/v1/members/sign-in")
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(memberSignInRequest)))
                                       .andDo(print())
                                       .andExpect(status().isOk())
                                       .andReturn();

        final var memberSignInResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), MemberSignInResponse.class);

        final var meaning = "세계";
        final var startIndex = 1;
        final var endIndex = 1;
        final var sentenceId = 1L;
        final var tagIds = List.of(1L, 2L);

        final var request = new PhraseCreateRequest(sentenceId, startIndex, endIndex, meaning, tagIds);

        mockMvc.perform(post("/v1/phrases")
                       .contentType(MediaType.APPLICATION_JSON)
                       .header("Authorization", memberSignInResponse.accessToken())
                       .content(objectMapper.writeValueAsString(request)))
               .andDo(print())
               .andExpect(status().isOk())
               .andReturn();

        // then
        final var phraseMvcResponse = mockMvc.perform(get("/v1/phrases/transcriptions/{transcriptionId}", transcriptionId)
                                                     .header("Authorization", memberSignInResponse.accessToken()))
                                             .andDo(print())
                                             .andExpect(status().isOk())
                                             .andReturn();

        TypeReference<List<TranscriptionPhraseResponse>> listType = new TypeReference<>() {
        };
        final var phraseResponses = objectMapper.readValue(phraseMvcResponse.getResponse().getContentAsString(), listType);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(phraseResponses).hasSize(1);
            final var phraseResponse = phraseResponses.get(0);
            softly.assertThat(phraseResponse.id()).isEqualTo(transcriptionId);
            softly.assertThat(phraseResponse.phrase()).isEqualTo("world");
            softly.assertThat(phraseResponse.meaning()).isEqualTo(meaning);
            softly.assertThat(phraseResponse.sentenceId()).isEqualTo(sentenceId);
            softly.assertThat(phraseResponse.startIndex()).isEqualTo(startIndex);
            softly.assertThat(phraseResponse.endIndex()).isEqualTo(endIndex);
            softly.assertThat(phraseResponse.tags()).containsExactlyInAnyOrderElementsOf(List.of(
                    new TagResponse(1L, "tag1"), new TagResponse(2L, "tag2")
            ));
        });
    }
}
