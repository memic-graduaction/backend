package com.example.memic.transcription.domain;

import com.example.memic.transcription.exception.InvalidTranscriptionException;
import java.time.LocalTime;

public class Sentence {

    private final LocalTime startPoint;
    private final String content;

    public Sentence(LocalTime startPoint, String content) {
        validate(content);
        this.startPoint = startPoint;
        this.content = content;
    }

    private void validate(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidTranscriptionException("스크립트 내용이 비어있습니다.");
        }
    }
}
