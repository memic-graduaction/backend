package com.example.memic.speech.application;

import com.example.memic.speech.dto.SpeechWordRequest;
import com.example.memic.speech.dto.SpeechWordResponse;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SpeechService {

    private static final Pattern SPECIAL_CHARACTERS = Pattern.compile("[^a-zA-Z]");

    private final WhisperApiClient whisperApiClient;

    public SpeechWordResponse transcribe(final MultipartFile speech, final SpeechWordRequest request) {
        String transcribedSpeech = whisperApiClient.transcribeSpeech(speech)
                                                   .replaceAll(SPECIAL_CHARACTERS.pattern(), "");
        boolean wordMatched = request.originalWord()
                                     .replaceAll(SPECIAL_CHARACTERS.pattern(), "")
                                     .equalsIgnoreCase(transcribedSpeech);

        return new SpeechWordResponse(transcribedSpeech, wordMatched);
    }
}
