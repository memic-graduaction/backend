package com.example.memic.transcription.domain;

import com.example.memic.member.domain.Member;
import com.example.memic.transcription.exception.InvalidTranscriptionException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "transcription")
    private List<TranscriptionSentence> transcriptionSentences;

    public Transcription(String url, Map<LocalTime, String> sentences, Member member) {
        validate(url);
        this.url = url;
        this.transcriptionSentences = sentences.entrySet().stream()
                                               .map(entry -> new TranscriptionSentence(this, entry.getKey(), entry.getValue()))
                                               .sorted()
                                               .toList();
        this.member = member;
    }

    public Transcription(String url, Map<LocalTime, String> sentences) {
        this(url, sentences, Member.NON_MEMBER);
    }

    public boolean isEqualsByMember(final Member member) {
        return this.member.equals(member);
    }


    private void validate(String url) {
        if (!url.startsWith("https://")) {
            throw new InvalidTranscriptionException("https 형식의 오디오 주소가 아닙니다.");
        }
    }
}
