package com.example.memic.speech.dto;

public record SpeechWordResponse(
        String recognizedWord,
        boolean wordMatched
) {
}
