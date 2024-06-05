package com.example.memic.transcription.domain;

import com.example.memic.member.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface TranscriptionRepository extends Repository<Transcription, Long> {

    Optional<Transcription> findById(Long id);

    default Transcription getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 번역본이 없습니다."));
    }

    Transcription save(Transcription transcription);

    List<Transcription> findByUrl(String url);

    List<Transcription> findAllByMember(Member member);

    List<Transcription> findByTranscribedAtAfterAndMember(LocalDateTime oneMonthAgo, Member member);
}
