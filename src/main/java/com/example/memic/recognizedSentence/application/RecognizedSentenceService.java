package com.example.memic.recognizedSentence.application;

import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceResponse;
import com.example.memic.recognizedSentence.repository.RecognizedSentenceRepository;
import com.example.memic.transcription.domain.Sentence;
import com.example.memic.transcription.domain.SentenceRepository;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RecognizedSentenceService {

    private final WhisperApiClient whisperApiClient;

    private final SentenceRepository sentenceRepository;

    private final RecognizedSentenceRepository recognizedSentenceRepository;

    public RecognizedSentenceService(
            WhisperApiClient whisperApiClient,
            SentenceRepository sentenceRepository,
            RecognizedSentenceRepository recognizedSentenceRepository
    ) {
        this.whisperApiClient = whisperApiClient;
        this.sentenceRepository = sentenceRepository;
        this.recognizedSentenceRepository = recognizedSentenceRepository;
    }

    @Transactional
    public RecognizedSentenceResponse transcribe(MultipartFile speechFile, Long sentenceId) {
        Sentence sentence = sentenceRepository.findById(sentenceId);

        RecognizedSentence recognizedSentence = whisperApiClient.transcribeSpeech(speechFile);
        RecognizedSentence savedRecognizedSentence = recognizedSentenceRepository.save(recognizedSentence);

        return RecognizedSentenceResponse.of(savedRecognizedSentence, sentence);
    }
}
