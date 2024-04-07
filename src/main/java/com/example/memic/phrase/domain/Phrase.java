package com.example.memic.phrase.domain;

import com.example.memic.member.domain.Member;
import com.example.memic.phrase.exception.InvalidPhraseException;
import com.example.memic.transcription.domain.TranscriptionSentence;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "meaning", nullable = false)
    private String meaning;

    @ManyToOne
    private Member member;

    @ManyToOne
    private TranscriptionSentence sentence;

    @Column(name = "start_index", nullable = false)
    private Integer startIndex;

    @Column(name = "end_index", nullable = false)
    private Integer endIndex;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "phrase", cascade = CascadeType.ALL)
    private List<PhraseTag> tags = new ArrayList<>();

    public Phrase(
            final String meaning,
            final Member member,
            final TranscriptionSentence sentence,
            final Integer startIndex,
            final Integer endIndex
    ) {
        this.meaning = meaning;
        this.member = member;
        this.sentence = sentence;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        validateIndex();
    }

    private void validateIndex() {
        if (sentence.getWordCount() <= endIndex) {
            throw new InvalidPhraseException("표현의 끝 인덱스는 원본문장의 단어 개수보다 작아야합니다.");
        }
        if (startIndex < 0) {
            throw new InvalidPhraseException("표현의 시작 인덱스는 0보다 커야합니다.");
        }
        if (startIndex > endIndex) {
            throw new InvalidPhraseException("표현의 시작 인덱스는 끝 인덱스보다 작거나 같아야 합니다.");
        }
    }

    public void addTag(final Tag tag) {
        if (tags.stream()
                .anyMatch(phraseTag -> phraseTag.isEqualTag(tag))) {
            return;
        }
        tags.add(new PhraseTag(this, tag));
    }
}
