package com.example.memic.transcription.ui;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SuppressWarnings("NonAsciiCharacters")
@AutoConfigureMockMvc
@SpringBootTest
class TranscriptionIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 유튜브_url을_입력하면_자막을_추출한다() throws Exception {
        TranscriptionCreateRequest request = new TranscriptionCreateRequest("https://youtu.be/lpcpsCY4Mco?si=_rxzxxH-fuE78HDf");

        MvcResult responseFromPost = mockMvc.perform(post("/v1/transcriptions")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.url").value("https://youtu.be/lpcpsCY4Mco?si=_rxzxxH-fuE78HDf"))
                .andExpect(jsonPath("$.sentences").isNotEmpty())
                .andReturn();

        TranscriptionResponse transcriptionResponseFromPost = objectMapper.readValue(
                responseFromPost.getResponse().getContentAsString(),
                TranscriptionResponse.class
        );

        MvcResult responseFromGet = mockMvc.perform(get("/v1/transcriptions/{id}", transcriptionResponseFromPost.id()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.url").value("https://youtu.be/lpcpsCY4Mco?si=_rxzxxH-fuE78HDf"))
                .andExpect(jsonPath("$.sentences").isNotEmpty())
                .andReturn();

        TranscriptionResponse transcriptionResponseFromGet = objectMapper.readValue(
                responseFromGet.getResponse().getContentAsString(),
                TranscriptionResponse.class
        );

        transcriptionResponseFromGet.sentences().forEach(e -> System.out.println(e.startPoint()));

        assertThat(transcriptionResponseFromPost).isEqualTo(transcriptionResponseFromGet);
        assertThat(transcriptionResponseFromPost.sentences()).isSortedAccordingTo(Comparator.comparing(SentenceResponse::startPoint));
    }
}