package com.example.memic.phrase.domain;


import com.example.memic.member.domain.Member;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface PhraseRepository extends Repository<Phrase, Long> {

    Phrase save(Phrase phrase);

    List<Phrase> findByMemberAndSentenceIdIn(Member member, List<Long> sentenceIds);

    List<Phrase> findByMember(Member member);
}
