package com.example.memic.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.memic.transcription.service.YoutubeMp4Extractor;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class YoutubeMp4ExtractServiceTest {

    @Test
    void 유튜브_비디오_추출() throws Exception {
        final var service = new YoutubeMp4Extractor();
        String output = service.extractVideo("https://youtu.be/lpcpsCY4Mco?si=_rxzxxH-fuE78HDf");
        assertThat(output).contains("100%");
    }
}