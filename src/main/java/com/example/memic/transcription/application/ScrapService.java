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
import java.util.concurrent.locks.Lock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final TranscriptionRepository transcriptionRepository;
    private final Lock lock;

    @Transactional
    public ScrapCreatedResponse createScrap(Member member, ScrapCreateRequest request) {
        lock.lock();
        Transcription transcription = transcriptionRepository.getById(request.transcriptionId());

        Scrap scrap = scrapRepository.findByMemberAndTranscription(member, transcription)
                                     .orElseGet(() -> createNewScrap(transcription, member));
        lock.unlock();
        return new ScrapCreatedResponse(scrap.getId());
    }

    private Scrap createNewScrap(Transcription transcription, Member member) {
        Scrap scrap = new Scrap(transcription, member);
        return scrapRepository.save(scrap);
    }

    @Transactional(readOnly = true)
    public List<ScrapResponse> getScraps(Member member) {
        return scrapRepository.findAllByMember(member)
                              .stream()
                              .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                              .map(ScrapResponse::fromEntity)
                              .toList();
    }

    public void deleteScrap(Member member, Long id) {
        Scrap scrap = scrapRepository.findById(id)
                                     .orElseThrow(() -> new InvalidTranscriptionException("스크랩을 찾을 수 없습니다."));
        if (scrap.isNotOwner(member)) {
            throw new InvalidTranscriptionException("본인이 작성한 스크랩만 삭제할 수 있습니다.");
        }
        scrapRepository.delete(scrap);
    }
}
