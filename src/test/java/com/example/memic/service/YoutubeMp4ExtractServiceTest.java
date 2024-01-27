package com.example.memic.service;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class YoutubeMp4ExtractServiceTest {

    @Test
    void 유튜브_비디오_추출() throws Exception {
        final var service = new YoutubeMp4ExtractService();
        service.extractVideo("https://youtu.be/C1jRVpqz44Y?si=G7FJplY2q4cBdkl");
    }
}