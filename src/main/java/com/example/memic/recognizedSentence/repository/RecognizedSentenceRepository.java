package com.example.memic.recognizedSentence.repository;

import com.example.memic.member.domain.Member;
import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface RecognizedSentenceRepository extends Repository<RecognizedSentence, Long> {
    RecognizedSentence save(RecognizedSentence recognizedSentence);

    List<RecognizedSentence> findBySpeaker(Member member);
}
