package com.example.memic.transcription.domain;

import com.example.memic.transcription.exception.InvalidTranscriptionException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Sentence {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalTime startPoint;

    private String content;

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
