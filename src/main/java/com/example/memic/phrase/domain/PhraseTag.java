package com.example.memic.phrase.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class PhraseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Phrase phrase;

    @ManyToOne
    private Tag tag;

    public PhraseTag(final Phrase phrase, final Tag tag) {
        this.phrase = phrase;
        this.tag = tag;
    }

    public boolean isEqualTag(final Tag tag) {
        return this.tag.equals(tag);
    }
}
