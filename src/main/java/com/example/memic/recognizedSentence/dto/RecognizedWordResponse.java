package com.example.memic.recognizedSentence.dto;

import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import java.util.List;

public record RecognizedWordResponse(
        Long id,
        String word,
        boolean isMatchedWithTranscription
) {

    public static List<RecognizedWordResponse> from(final RecognizedSentence recognizedSentence) {
        return recognizedSentence.getRecognizedWords()
                                 .stream()
                                 .map(recognizedWord -> new RecognizedWordResponse(
                                         recognizedWord.getId(),
                                         recognizedWord.getWord(),
                                         recognizedWord.isMatchedWithTranscription()
                                 ))
                                 .toList();
    }
}
