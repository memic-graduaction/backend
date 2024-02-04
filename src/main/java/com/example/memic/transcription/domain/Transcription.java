package com.example.memic.transcription.domain;

import com.example.memic.transcription.exception.InvalidTranscriptionException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Transcription {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  private String url;

  @OneToMany(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "transcription_id", updatable = false, nullable = false)
  private List<Sentence> sentences;

  public Transcription(String url, Map<LocalTime, String> sentences) {
    validate(url);
    this.url = url;
    this.sentences = sentences.entrySet().stream()
                              .map(entry -> new Sentence(entry.getKey(), entry.getValue()))
                              .sorted()
                              .toList();
  }

  private void validate(String url) {
    if (!url.startsWith("https://")) {
      throw new InvalidTranscriptionException("https 형식의 오디오 주소가 아닙니다.");
    }
  }
}
