package com.example.memic.recognizedSentence.application;

import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceRequest;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceResponse;
import com.example.memic.recognizedSentence.repository.RecognizedSentenceRepository;
import com.example.memic.transcription.domain.TranscriptionSentence;
import com.example.memic.transcription.domain.TranscriptionSentenceRepository;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RecognizedSentenceService {

    private final WhisperApiClient whisperApiClient;
    private final TranscriptionSentenceRepository transcriptionSentenceRepository;
    private final RecognizedSentenceRepository recognizedSentenceRepository;

    public RecognizedSentenceService(
            WhisperApiClient whisperApiClient,
            TranscriptionSentenceRepository transcriptionSentenceRepository,
            RecognizedSentenceRepository recognizedSentenceRepository
    ) {
        this.whisperApiClient = whisperApiClient;
        this.transcriptionSentenceRepository = transcriptionSentenceRepository;
        this.recognizedSentenceRepository = recognizedSentenceRepository;
    }

    @Transactional
    public RecognizedSentenceResponse transcribe(MultipartFile speechFile, RecognizedSentenceRequest request) {
        TranscriptionSentence transcriptionSentence = transcriptionSentenceRepository.getById(request.id());
        String transcribedSpeech = whisperApiClient.transcribeSpeech(speechFile);

        RecognizedSentence recognizedSentence = new RecognizedSentence(
                transcribedSpeech,
                transcriptionSentence
        );
        RecognizedSentence savedRecognizedSentence = recognizedSentenceRepository.save(recognizedSentence);

        return RecognizedSentenceResponse.of(savedRecognizedSentence, transcriptionSentence);
    }
}
