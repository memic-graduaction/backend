package com.example.memic.transcription.domain;

import com.example.memic.transcription.exception.InvalidTranscriptionException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TranscriptionSentence implements Comparable<TranscriptionSentence> {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transcription_id", updatable = false, nullable = false)
    private Transcription transcription;

    private LocalTime startPoint;

    private String content;

    public TranscriptionSentence(LocalTime startPoint, String content) {
        this(null, startPoint, content);
    }

    public TranscriptionSentence(Transcription transcription, LocalTime startPoint, String content) {
        validate(content);
        this.transcription = transcription;
        this.startPoint = startPoint;
        this.content = content;
    }

    private void validate(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidTranscriptionException("스크립트 내용이 비어있습니다.");
        }
    }

    public List<String> getWords() {
        return List.of(content.split(" "));
    }

    public int getWordCount() {
        return getWords().size();
    }

    @Override
    public int compareTo(TranscriptionSentence o) {
        return this.startPoint.compareTo(o.startPoint);
    }
}
