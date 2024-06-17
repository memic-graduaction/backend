package com.example.memic.transcription.application;

import com.example.memic.member.domain.Member;
import com.example.memic.transcription.domain.Scrap;
import com.example.memic.transcription.domain.ScrapRepository;
import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.domain.TranscriptionRepository;
import com.example.memic.transcription.dto.ScrapCreateRequest;
import com.example.memic.transcription.dto.ScrapCreatedResponse;
import com.example.memic.transcription.dto.ScrapResponse;
import com.example.memic.transcription.exception.InvalidTranscriptionException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final TranscriptionRepository transcriptionRepository;

    @Transactional
    public ScrapCreatedResponse createScrap(Member member, ScrapCreateRequest request) {
        Transcription transcription = transcriptionRepository.findById(request.transcriptionId())
                                                             .orElseThrow(() -> new InvalidTranscriptionException("스크랩 하려는 번역본을 찾을 수 없습니다."));
        Scrap saved = scrapRepository.save(new Scrap(transcription, member));
        return new ScrapCreatedResponse(saved.getId());
    }

    @Transactional(readOnly = true)
    public List<ScrapResponse> getScraps(Member member) {
        return scrapRepository.findAllByMember(member)
                              .stream()
                              .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                              .map(ScrapResponse::fromEntity)
                              .toList();
    }
}
