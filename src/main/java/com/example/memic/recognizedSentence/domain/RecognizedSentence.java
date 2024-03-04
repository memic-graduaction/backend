package com.example.memic.recognizedSentence.domain;

import com.example.memic.recognizedSentence.exception.InvalidRecognizedException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class RecognizedSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String recognizedSentence;

    public RecognizedSentence(String recognizedSentence) {
        validate(recognizedSentence);
        this.recognizedSentence = recognizedSentence;
    }

    private void validate(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidRecognizedException("인식된 스크립트가 없습니다.");
        }
    }
}
