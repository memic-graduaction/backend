package com.example.memic.transcription.ui;

import java.time.LocalTime;

public record TranscriptionResponse(
        Long id,
        LocalTime startPoint,
        String sentence
) {
}
