package com.example.memic.transcription.domain;

import com.example.memic.transcription.exception.InvalidTranscriptionException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class Transcription {

    private final String url;
    private final List<Sentence> sentences;

    public Transcription(String url, Map<LocalTime, String> sentences) {
        validate(url);
        this.url = url;
        this.sentences = sentences.entrySet().stream()
                .map(entry -> new Sentence(entry.getKey(), entry.getValue()))
                .toList();
    }

    private void validate(String url) {
        if (!url.startsWith("https://")) {
            throw new InvalidTranscriptionException("https 형식의 오디오 주소가 아닙니다.");
        }
    }
}
