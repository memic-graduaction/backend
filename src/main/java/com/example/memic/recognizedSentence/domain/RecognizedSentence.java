package com.example.memic.recognizedSentence.domain;

import com.example.memic.recognizedSentence.exception.InvalidRecognizedException;
import com.example.memic.transcription.domain.TranscriptionSentence;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class RecognizedSentence {

    private static final double VALID_RANGE_FACTOR = 0.3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<RecognizedWord> recognizedWords;

    public RecognizedSentence(String recognizedSentence, TranscriptionSentence originalSentence) {
        validate(recognizedSentence);
        recognizedWords = createRecognizedWords(recognizedSentence, originalSentence);
    }

    private List<RecognizedWord> createRecognizedWords(String recognizedSentence, TranscriptionSentence originalSentence) {
        List<String> originalWords = originalSentence.getWords();
        int numberOfOriginalWords = originalWords.size();
        int validTargetRange = (int) Math.round(numberOfOriginalWords * VALID_RANGE_FACTOR);
        List<RecognizedWord> recognizedWords = Arrays.stream(recognizedSentence.split(" "))
                                                     .map(RecognizedWord::new)
                                                     .toList();

        for (int i = 0; i < recognizedWords.size(); i++) {
            RecognizedWord targetWord = recognizedWords.get(i);
            int validFirstTargetWordIndex = Math.max(i - validTargetRange, 0);
            int validLastTargetWordIndex = Math.min(i + validTargetRange - 1, numberOfOriginalWords - 1) + 1;

            if (validLastTargetWordIndex < validFirstTargetWordIndex) {
                validFirstTargetWordIndex = validLastTargetWordIndex;
            }

            if (containsInValidRange(originalWords, validFirstTargetWordIndex, validLastTargetWordIndex, targetWord)) {
                targetWord.match();
            }
        }
        return recognizedWords;
    }

    private boolean containsInValidRange(
            List<String> originalWords,
            int validFirstTargetWordIndex,
            int validLastTargetWordIndex,
            RecognizedWord targetWord
    ) {
        return originalWords.subList(validFirstTargetWordIndex, validLastTargetWordIndex)
                            .contains(targetWord.getWord());
    }

    private void validate(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidRecognizedException("인식된 스크립트가 없습니다.");
        }
    }
}
