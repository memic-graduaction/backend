package com.example.memic.transcription.service;

import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.domain.TranscriptionRepository;
import com.example.memic.transcription.dto.TranscriptionCreateRequest;
import com.example.memic.transcription.dto.TranscriptionResponse;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TranscriptionService {

    private final YoutubeMp4Extractor extractor;
    private final WhisperApiClient whisperApiClient;
    private final TranscriptionRepository transcriptionRepository;

    public TranscriptionService(
            YoutubeMp4Extractor extractor,
            WhisperApiClient whisperApiClient,
            TranscriptionRepository transcriptionRepository
    ) {
        this.extractor = extractor;
        this.whisperApiClient = whisperApiClient;
        this.transcriptionRepository = transcriptionRepository;
    }

    @Transactional
    public TranscriptionResponse transcribe(TranscriptionCreateRequest request) {
        String filePath = extractor.extractVideo(request.url());

        Transcription transcription = whisperApiClient.transcribe(request.url(), filePath);
        transcriptionRepository.save(transcription);
        
        return TranscriptionResponse.fromEntity(transcription);
    }

    @Transactional(readOnly = true)
    public TranscriptionResponse getTranscription(Long id) {
        Transcription transcription = transcriptionRepository.getById(id);
        return TranscriptionResponse.fromEntity(transcription);
    }
}
