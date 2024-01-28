package com.example.memic.service;

import com.example.memic.transcription.service.TranscriptionService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AudioTranscriptionServiceTest {

    @Test
    void mp3_자막추출() {
        final var service = new TranscriptionService();
        ResponseEntity<String> response = service.transcribe("/Users/jung-yunho/youtube-mp3/lpcpsCY4Mco.mp3");
        SoftAssertions.assertSoftly(
                softly -> {
                    softly.assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
                }
        );
    }
}