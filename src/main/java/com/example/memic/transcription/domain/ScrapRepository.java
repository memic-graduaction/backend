package com.example.memic.transcription.domain;

import com.example.memic.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

public interface ScrapRepository extends Repository<Scrap, Long> {

    @EntityGraph(attributePaths = {"transcription"})
    List<Scrap> findAllByMember(Member member);

    Scrap save(Scrap scrap);
}
