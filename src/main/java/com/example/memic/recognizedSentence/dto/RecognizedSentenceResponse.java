package com.example.memic.recognizedSentence.dto;

import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import com.example.memic.transcription.domain.TranscriptionSentence;
import java.util.List;

public record RecognizedSentenceResponse(
        Long id,
        Long SentenceId,
        String sentence,
        List<RecognizedWordResponse> recognizedWords
) {
    public static RecognizedSentenceResponse of(RecognizedSentence recognizedSentence, TranscriptionSentence transcriptionSentence) {
        return new RecognizedSentenceResponse(
                recognizedSentence.getId(),
                transcriptionSentence.getId(),
                transcriptionSentence.getContent(),
                RecognizedWordResponse.from(recognizedSentence)
        );
    }
}
