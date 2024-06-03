package com.example.memic.transcription.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.transcription.dto.SentenceResponse;
import com.example.memic.transcription.dto.TranscriptionCreateRequest;
import com.example.memic.transcription.dto.TranscriptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Comparator;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
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
                                            .andExpect(jsonPath("$.url").value(containsString("https://youtu.be/lpcpsCY4Mco")))
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
                                           .andExpect(jsonPath("$.url").value(containsString("https://youtu.be/lpcpsCY4Mco")))
                                           .andExpect(jsonPath("$.sentences").isNotEmpty())
                                           .andReturn();

        final var transcriptionResponseFromGet = objectMapper.readValue(
                responseFromGet.getResponse().getContentAsString(),
                TranscriptionResponse.class
        );

        assertThat(transcriptionResponseFromPost).isEqualTo(transcriptionResponseFromGet);
        assertThat(transcriptionResponseFromPost.sentences()).isSortedAccordingTo(Comparator.comparing(SentenceResponse::startPoint));
    }
}
