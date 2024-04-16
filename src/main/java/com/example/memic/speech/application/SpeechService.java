package com.example.memic.speech.application;

import com.example.memic.speech.dto.SpeechWordRequest;
import com.example.memic.speech.dto.SpeechWordResponse;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SpeechService {

    private final WhisperApiClient whisperApiClient;

    public SpeechWordResponse transcribe(final MultipartFile speech, final SpeechWordRequest request) {
        String transcribedSpeech = whisperApiClient.transcribeSpeech(speech);
        boolean wordMatched = request.originalWord().equalsIgnoreCase(transcribedSpeech);

        return new SpeechWordResponse(transcribedSpeech, wordMatched);
    }
}
