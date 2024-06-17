package com.example.memic.transcription.domain;

import com.example.memic.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

public interface ScrapRepository extends Repository<Scrap, Long> {

    @EntityGraph(attributePaths = {"transcription"})
    List<Scrap> findAllByMember(Member member);

    Scrap save(Scrap scrap);

    Optional<Scrap> findById(Long id);

    void delete(Scrap scrap);
}
