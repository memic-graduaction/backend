package com.example.memic.recognizedSentence.dto;

import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import com.example.memic.transcription.domain.Sentence;

public record RecognizedSentenceResponse(
        Long id,
        Long SentenceId,
        String sentence,
        String recognizedSentence
) {
    public static RecognizedSentenceResponse of(RecognizedSentence recognizedSentence, Sentence sentence) {
        return new RecognizedSentenceResponse(
                recognizedSentence.getId(),
                sentence.getId(),
                sentence.getContent(),
                recognizedSentence.getRecognizedSentence()
        );
    }
}
