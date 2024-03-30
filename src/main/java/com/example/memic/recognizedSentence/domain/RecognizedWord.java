package com.example.memic.recognizedSentence.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RecognizedWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private boolean isMatchedWithTranscription;

    public RecognizedWord(final String word) {
        this.word = word;
        this.isMatchedWithTranscription = false;
    }

    void match() {
        this.isMatchedWithTranscription = true;
    }
}
