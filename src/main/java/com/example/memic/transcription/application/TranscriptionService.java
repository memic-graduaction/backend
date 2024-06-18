package com.example.memic.transcription.application;

import com.example.memic.member.domain.Member;
import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.domain.TranscriptionRepository;
import com.example.memic.transcription.dto.TranscriptionCreateRequest;
import com.example.memic.transcription.dto.TranscriptionResponse;
import com.example.memic.transcription.dto.TranscriptionUrlListResponse;
import com.example.memic.transcription.infrastructure.Mp4Extractor;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TranscriptionService {

    private final Mp4Extractor extractor;
    private final WhisperApiClient whisperApiClient;
    private final TranscriptionRepository transcriptionRepository;

    public TranscriptionService(
            Mp4Extractor extractor,
            WhisperApiClient whisperApiClient,
            TranscriptionRepository transcriptionRepository
    ) {
        this.extractor = extractor;
        this.whisperApiClient = whisperApiClient;
        this.transcriptionRepository = transcriptionRepository;
    }

    @Transactional
    public TranscriptionResponse transcribe(final TranscriptionCreateRequest request, final Member member) {
        Transcription transcription = transcriptionRepository.findByUrl(request.url())
                                                             .map(t -> {
                                                                 t.addMember(member);
                                                                 return t;
                                                             })
                                                             .orElseGet(() -> transcribeNew(request, member));

        return TranscriptionResponse.fromEntity(transcription);
    }

    private Transcription transcribeNew(final TranscriptionCreateRequest request, final Member member) {
        String filePath = extractor.extractVideo(request.url());
        Transcription transcribed = whisperApiClient.transcribe(request.url(), filePath);
        transcribed.addMember(member);
        return transcriptionRepository.save(transcribed);
    }

    @Transactional(readOnly = true)
    public TranscriptionResponse getTranscription(Long id) {
        Transcription transcription = transcriptionRepository.getById(id);
        return TranscriptionResponse.fromEntity(transcription);
    }

    @Transactional(readOnly = true)
    public List<TranscriptionUrlListResponse> getTranscriptionUrls(final Member member) {
        List<Transcription> transcriptions = transcriptionRepository.findAllByMember(member);
        return TranscriptionUrlListResponse.from(transcriptions);
    }
}
