package com.example.memic.transcription.domain;

import com.example.memic.member.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface TranscriptionRepository extends Repository<Transcription, Long> {

    Optional<Transcription> findById(Long id);

    default Transcription getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 번역본이 없습니다."));
    }

    Transcription save(Transcription transcription);

    Optional<Transcription> findByUrl(String url);

    @Query("SELECT tm.transcription FROM TranscriptionMember tm WHERE tm.member = :member")
    List<Transcription> findAllByMember(@Param("member") Member member);

    @Query("""
            SELECT t FROM Transcription t 
            JOIN t.members tm 
            WHERE YEAR(t.transcribedAt) = :year 
            AND MONTH(t.transcribedAt) = :month 
            AND tm.member = :member
            """)
    List<Transcription> findByYearMonthAndMember(@Param("year") Integer year, @Param("month") Integer month, @Param("member") Member member);
}
