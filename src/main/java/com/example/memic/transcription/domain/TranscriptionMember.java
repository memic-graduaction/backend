package com.example.memic.transcription.domain;


import com.example.memic.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class TranscriptionMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transcription_id")
    private Transcription transcription;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public TranscriptionMember(final Transcription transcription, final Member member) {
        this.transcription = transcription;
        this.member = member;
    }

    public boolean isEqualMember(final Member member) {
        return this.member.equals(member);
    }
}
