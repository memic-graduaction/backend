package com.example.memic.transcription.domain;

import com.example.memic.member.domain.Member;
import com.example.memic.transcription.exception.InvalidTranscriptionException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Transcription {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String url;

    @CreationTimestamp
    private LocalDateTime transcribedAt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "transcription", cascade = CascadeType.ALL)
    private final List<TranscriptionMember> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "transcription")
    private List<TranscriptionSentence> transcriptionSentences;

    public Transcription(String url, Map<LocalTime, String> sentences) {
        validate(url);
        this.url = url;
        this.transcriptionSentences = sentences.entrySet().stream()
                                               .map(entry -> new TranscriptionSentence(this, entry.getKey(), entry.getValue()))
                                               .sorted()
                                               .toList();
    }

    public void addMember(final Member member) {
        if (members.stream()
                   .anyMatch(transcriptionMember -> transcriptionMember.isEqualMember(member))) {
            return;
        }
        members.add(new TranscriptionMember(this, member));
    }

    private void validate(String url) {
        if (!url.startsWith("https://")) {
            throw new InvalidTranscriptionException("https 형식의 오디오 주소가 아닙니다.");
        }
    }
}
