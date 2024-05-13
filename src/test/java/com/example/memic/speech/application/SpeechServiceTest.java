package com.example.memic.speech.application;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.memic.speech.dto.SpeechWordRequest;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class SpeechServiceTest {

    @Test
    void 텍스트_변환_결과에서_특수문자를_빼고_매칭여부를_비교한다() {
        // given
        final var mockWhisperApiClient = mock(WhisperApiClient.class);
        final var service = new SpeechService(mockWhisperApiClient);
        final var request = new SpeechWordRequest("hello");
        final var speech = "!!.hello!$%^";

        when(mockWhisperApiClient.transcribeSpeech(any())).thenReturn(speech);

        // when
        final var response = service.transcribe(null, request);

        // then
        assertTrue(response.wordMatched());
    }
}
