package com.example.memic.recognizedSentence.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import com.example.memic.member.domain.Member;
import com.example.memic.recognizedSentence.application.RecognizedSentenceService;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class RecognizedSentenceControllerTest {

    @MockBean
    private RecognizedSentenceService recognizedSentenceService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 인증_헤더가_없을_경우_빈_멤버를_인자로_전달한다() throws Exception {
        // given
        final var speech = new MockMultipartFile("speech", "speech.mp3", "audio/mp3", new byte[0]);
        final var request = new RecognizedSentenceRequest(1L);
        final var sentence = new MockMultipartFile(
                "sentence",
                "ignored",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        // when
        final var response = mockMvc.perform(multipart("/v1/recognized-sentences")
                .file(speech)
                .file(sentence))
                .andReturn()
                .getResponse();

        // then
        assertEquals(200, response.getStatus());
        verify(recognizedSentenceService).transcribe(any(), eq(request), eq(Member.NON_MEMBER));
    }
}
