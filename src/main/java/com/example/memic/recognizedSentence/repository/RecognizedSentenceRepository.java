package com.example.memic.recognizedSentence.repository;

import com.example.memic.member.domain.Member;
import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface RecognizedSentenceRepository extends Repository<RecognizedSentence, Long> {
    RecognizedSentence save(RecognizedSentence recognizedSentence);

    List<RecognizedSentence> findBySpeaker(Member member);

    List<RecognizedSentence> findBySpokenAtAfterAndSpeaker(LocalDateTime oneMonthAgo, Member member);

    @Query("""
            SELECT rs FROM RecognizedSentence rs
            WHERE YEAR(rs.spokenAt) = :year
            AND MONTH(rs.spokenAt) = :month AND rs.speaker = :speaker
            """)
    List<RecognizedSentence> findByYearMonthSpeaker(@Param("year") Integer year, @Param("month") Integer month, @Param("speaker") Member speaker);
}
