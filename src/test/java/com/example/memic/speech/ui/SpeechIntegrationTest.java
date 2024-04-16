package com.example.memic.speech.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.speech.dto.SpeechWordRequest;
import com.example.memic.speech.dto.SpeechWordResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

@SuppressWarnings("NonAsciiCharacters")
@AutoConfigureMockMvc
@SpringBootTest
class SpeechIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 음성과_단어를_입력하면_동일_여부를_반환한다() throws Exception {
        final var speechFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "speech.mp3");
        final var request = new SpeechWordRequest("test");

        final var speechPart = new MockMultipartFile(
                "speech",
                "speech.mp3",
                "audio/mpeg",
                Files.readAllBytes(speechFile.toPath())
        );

        final var wordPart = new MockMultipartFile(
                "word",
                "ignored",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        final var response = mockMvc.perform(multipart("/v1/speeches/words")
                                            .file(speechPart)
                                            .file(wordPart)
                                            .contentType(MediaType.MULTIPART_FORM_DATA))
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.recognizedWord").isString())
                                    .andExpect(jsonPath("$.wordMatched").isBoolean())
                                    .andReturn();

        final var speechWordResponse = objectMapper.readValue(
                response.getResponse().getContentAsString(),
                SpeechWordResponse.class
        );

        SoftAssertions.assertSoftly(
                softly -> {
                    softly.assertThat(speechWordResponse.recognizedWord()).isNotNull();
                    softly.assertThat(speechWordResponse.wordMatched()).isFalse();
                }
        );
    }
}