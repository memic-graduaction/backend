package com.example.memic.speech.application;

import com.example.memic.speech.domain.Speech;
import com.example.memic.speech.dto.SpeechResponse;
import com.example.memic.speech.repository.SpeechRepository;
import com.example.memic.transcription.domain.Sentence;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import com.example.memic.transcription.service.SentenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpeechService {

    private final WhisperApiClient whisperApiClient;

    private final SentenceService sentenceService;

    private final SpeechRepository speechRepository;

    public SpeechService(
            WhisperApiClient whisperApiClient,
            SentenceService sentenceService,
            SpeechRepository speechRepository
    ) {
        this.whisperApiClient = whisperApiClient;
        this.sentenceService = sentenceService;
        this.speechRepository = speechRepository;
    }

    @Transactional
    public SpeechResponse transcribe(MultipartFile speechFile, Long sentenceId) {
        Sentence sentence = sentenceService.findSentenceById(sentenceId);

        Speech speech = whisperApiClient.transcribeSpeech(speechFile);
        Speech savedSpeech = speechRepository.save(speech);

        return SpeechResponse.of(savedSpeech, sentence);
    }
}
