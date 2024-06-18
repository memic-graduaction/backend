package com.example.memic.transcription.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.common.database.NoTransactionExtension;
import com.example.memic.member.dto.MemberSignInRequest;
import com.example.memic.member.dto.MemberSignInResponse;
import com.example.memic.transcription.dto.SentenceResponse;
import com.example.memic.transcription.dto.TranscriptionCreateRequest;
import com.example.memic.transcription.dto.TranscriptionRecentListResponse;
import com.example.memic.transcription.dto.TranscriptionResponse;
import com.example.memic.transcription.dto.TranscriptionUrlListResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Comparator;
import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("NonAsciiCharacters")
@AutoConfigureMockMvc
@SpringBootTest
class TranscriptionIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Test
    void 유튜브_url을_입력하면_자막을_추출한다() throws Exception {
        try (final var connection = dataSource.getConnection()) {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
        }

        final var request = new TranscriptionCreateRequest("https://youtu.be/lpcpsCY4Mco?si=_rxzxxH-fuE78HDf");

        final var responseFromPost = mockMvc.perform(post("/v1/transcriptions")
                                                    .content(objectMapper.writeValueAsString(request))
                                                    .contentType(MediaType.APPLICATION_JSON))
                                            .andDo(print())
                                            .andExpect(status().isOk())
                                            .andExpect(jsonPath("$.id").isNumber())
                                            .andExpect(jsonPath("$.url").value(
                                                    containsString("https://youtu.be/lpcpsCY4Mco")))
                                            .andExpect(jsonPath("$.sentences").isNotEmpty())
                                            .andReturn();

        final var transcriptionResponseFromPost = objectMapper.readValue(
                responseFromPost.getResponse().getContentAsString(),
                TranscriptionResponse.class
        );

        final var responseFromGet = mockMvc.perform(get("/v1/transcriptions/{id}", transcriptionResponseFromPost.id()))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andExpect(jsonPath("$.id").isNumber())
                                           .andExpect(jsonPath("$.url").value(
                                                   containsString("https://youtu.be/lpcpsCY4Mco")))
                                           .andExpect(jsonPath("$.sentences").isNotEmpty())
                                           .andReturn();

        final var transcriptionResponseFromGet = objectMapper.readValue(
                responseFromGet.getResponse().getContentAsString(),
                TranscriptionResponse.class
        );

        assertThat(transcriptionResponseFromPost).isEqualTo(transcriptionResponseFromGet);
        assertThat(transcriptionResponseFromPost.sentences()).isSortedAccordingTo(
                Comparator.comparing(SentenceResponse::startPoint));
    }

    @Test
    @ExtendWith(NoTransactionExtension.class)
    void 사용자가_요청한_유튜브_url_목록을_반환한다() throws Exception {
        try (final var connection = dataSource.getConnection()) {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            connection.createStatement().execute("INSERT INTO member (id, email, password) VALUES (1, 'yunho@naver.com', '1234')");
            connection.createStatement().execute("INSERT INTO transcription (id, url, transcribed_at) VALUES (1, 'https://test01.com',  '2024-06-04 12:34:56')");
            connection.createStatement().execute("INSERT INTO transcription (id, url, transcribed_at) VALUES (2, 'https://test02.com',  '2024-06-04 12:34:56')");
            connection.createStatement().execute("INSERT INTO transcription (id, url, transcribed_at) VALUES (3, 'https://test03.com',  '2024-06-04 12:34:56')");
            connection.createStatement().execute("INSERT INTO transcription (id, url, transcribed_at) VALUES (4, 'https://test04.com',  '2024-06-04 12:34:56')");
            connection.createStatement().execute("INSERT INTO transcription_member(id, transcription_id, member_id) VALUES (1, 1, 1)");
            connection.createStatement().execute("INSERT INTO transcription_member(id, transcription_id, member_id) VALUES (2, 2, 1)");
            connection.createStatement().execute("INSERT INTO transcription_member(id, transcription_id, member_id) VALUES (3, 3, 1)");
            connection.createStatement().execute("INSERT INTO transcription_member(id, transcription_id, member_id) VALUES (4, 4, 2)");

        }

        final var signInRequest = new MemberSignInRequest("yunho@naver.com", "1234");


        final var mvcSignInResponse = mockMvc.perform(post("/v1/members/sign-in")
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .content(objectMapper.writeValueAsString(signInRequest)))
                                             .andDo(print())
                                             .andExpect(status().isOk())
                                             .andReturn();

        final var signInResponse = objectMapper.readValue(
                mvcSignInResponse.getResponse().getContentAsString(),
                MemberSignInResponse.class
        );

        final var responseFromGet = mockMvc.perform(get("/v1/transcriptions/my/all")
                                                   .header("Authorization", signInResponse.accessToken()))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        TypeReference<List<TranscriptionUrlListResponse>> listType = new TypeReference<>() {
        };
        final var transcriptionUrlListResponse = objectMapper.readValue(responseFromGet.getResponse().getContentAsString(),
                listType);

        assertEquals(transcriptionUrlListResponse.size(), 3);;
    }

    @Test
    @ExtendWith(NoTransactionExtension.class)
    void 사용자가_최근_조회한_동영상_목록을_반환한다() throws Exception{
        try (final var connection = dataSource.getConnection()) {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            connection.createStatement().execute("INSERT INTO member (id, email, password) VALUES (1, 'yunho@naver.com', '1234')");
            connection.createStatement().execute("INSERT INTO transcription (id, url, transcribed_at) VALUES (1, 'https://test01.com',  '2024-06-04 12:34:56')");
            connection.createStatement().execute("INSERT INTO transcription (id, url, transcribed_at) VALUES (2, 'https://test02.com',  '2024-06-05 12:34:56')");
            connection.createStatement().execute("INSERT INTO transcription (id, url, transcribed_at) VALUES (3, 'https://test03.com',  '2024-06-06 12:34:56')");
            connection.createStatement().execute("INSERT INTO transcription (id, url, transcribed_at) VALUES (4, 'https://test04.com',  '2024-06-07 12:34:56')");
            connection.createStatement().execute("INSERT INTO transcription_member(id, transcription_id, member_id) VALUES (1, 1, 1)");
            connection.createStatement().execute("INSERT INTO transcription_member(id, transcription_id, member_id) VALUES (2, 2, 1)");
            connection.createStatement().execute("INSERT INTO transcription_member(id, transcription_id, member_id) VALUES (3, 3, 1)");
            connection.createStatement().execute("INSERT INTO transcription_member(id, transcription_id, member_id) VALUES (4, 4, 1)");
            connection.createStatement().execute("INSERT INTO transcription_member(id, transcription_id, member_id) VALUES (5, 4, 2)");
        }

        final var signInRequest = new MemberSignInRequest("yunho@naver.com", "1234");


        final var mvcSignInResponse = mockMvc.perform(post("/v1/members/sign-in")
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .content(objectMapper.writeValueAsString(signInRequest)))
                                             .andDo(print())
                                             .andExpect(status().isOk())
                                             .andReturn();

        final var signInResponse = objectMapper.readValue(
                mvcSignInResponse.getResponse().getContentAsString(),
                MemberSignInResponse.class
        );

        final var responseFromGet = mockMvc.perform(get("/v1/transcriptions/recent/all")
                                                   .header("Authorization", signInResponse.accessToken()))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        TypeReference<List<TranscriptionRecentListResponse>> listType = new TypeReference<>() {
        };
        final var transcriptionRecentListResponses = objectMapper.readValue(responseFromGet.getResponse().getContentAsString(),
                listType);

        SoftAssertions.assertSoftly(
                softly ->{
                    softly.assertThat(transcriptionRecentListResponses).hasSize(4);
                    softly.assertThat(transcriptionRecentListResponses)
                          .isSortedAccordingTo(Comparator.comparing(TranscriptionRecentListResponse::id).reversed());
                }
        );
    }
}
