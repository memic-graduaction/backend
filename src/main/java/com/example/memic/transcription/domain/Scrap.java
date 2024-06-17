package com.example.memic.transcription.domain;

import com.example.memic.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@Getter
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Transcription transcription;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Scrap(Transcription transcription, Member member) {
        this.transcription = transcription;
        this.member = member;
    }
}
