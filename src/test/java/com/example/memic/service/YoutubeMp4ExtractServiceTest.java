package com.example.memic.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.memic.transcription.application.YoutubeMp4Extractor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class YoutubeMp4ExtractServiceTest {

    @Disabled("yt-dlp가 설치되어 있어야 합니다.")
    @Test
    void 유튜브_비디오_추출() {
        final var service = new YoutubeMp4Extractor("/Users/jung-yunho/youtube-mp3/", "yt-dlp");
        String output = service.extractVideo("https://youtu.be/lpcpsCY4Mco?si=_rxzxxH-fuE78HDf");
        assertThat(output).contains("100%");
    }
}
