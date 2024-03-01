package com.example.memic.speech.dto;

import com.example.memic.speech.domain.Speech;
import com.example.memic.transcription.domain.Sentence;

public record SpeechResponse(
        Long id,
        Long SentenceId,
        String sentence,
        String recognizedSentence
) {
    public static SpeechResponse of(Speech speech, Sentence sentence) {
        return new SpeechResponse(
                speech.getId(),
                sentence.getId(),
                sentence.getContent(),
                speech.getRecognizedSentence()
        );
    }
}
