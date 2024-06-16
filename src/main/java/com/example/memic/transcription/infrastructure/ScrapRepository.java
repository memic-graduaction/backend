package com.example.memic.transcription.infrastructure;

import com.example.memic.member.domain.Member;
import com.example.memic.transcription.domain.Scrap;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    @EntityGraph(attributePaths = {"transcription"})
    List<Scrap> findAllByMember(Member member);
}
