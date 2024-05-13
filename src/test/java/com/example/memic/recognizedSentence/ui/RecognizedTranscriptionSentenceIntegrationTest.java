package com.example.memic.recognizedSentence.ui;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.recognizedSentence.dto.RecognizedSentenceRequest;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceResponse;
import com.example.memic.recognizedSentence.dto.RecognizedWordResponse;
import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.domain.TranscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Collectors;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class RecognizedTranscriptionSentenceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TranscriptionRepository transcriptionRepository;

    @Test
    void 음성을_입력하면_문장을_추출한다() throws Exception {
        final var transcription = new Transcription("https://test", Map.of(LocalTime.of(0, 0), "hello"));
        final var savedTranscription = transcriptionRepository.save(transcription);
        final var firstSentence = savedTranscription.getTranscriptionSentences().get(0);

        final var request = new RecognizedSentenceRequest(firstSentence.getId());
        final var speechFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "speech.mp3");

        final var sentencePart = new MockMultipartFile(
                "sentence",
                "ignored",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );
        final var speechPart = new MockMultipartFile(
                "speech",
                "speech.mp3",
                "audio/mpeg",
                Files.readAllBytes(speechFile.toPath())
        );

        final var response = mockMvc.perform(multipart("/v1/recognized-sentences")
                                            .file(sentencePart)
                                            .file(speechPart)
                                            .contentType(MediaType.MULTIPART_FORM_DATA))
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.id").isNumber())
                                    .andExpect(jsonPath("$.sentence").value("hello"))
                                    .andReturn();
        final var recognizedSentenceResponse = objectMapper.readValue(
                response.getResponse().getContentAsString(),
                RecognizedSentenceResponse.class
        );

        final var fullSentence = recognizedSentenceResponse.recognizedWords()
                                                           .stream()
                                                           .map(RecognizedWordResponse::word)
                                                           .collect(Collectors.joining(" "));

        boolean anyMatched = recognizedSentenceResponse.recognizedWords()
                                                       .stream()
                                                       .anyMatch(RecognizedWordResponse::isMatchedWithTranscription);

        SoftAssertions.assertSoftly(
                softly -> {
                    softly.assertThat(recognizedSentenceResponse.sentence()).contains("hello");
                    softly.assertThat(recognizedSentenceResponse.id()).isNotNull();
                    softly.assertThat(fullSentence).contains("Do you mind");
                    softly.assertThat(anyMatched).isFalse();
                }
        );
    }
}
