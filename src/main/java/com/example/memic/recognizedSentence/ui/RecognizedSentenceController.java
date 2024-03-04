package com.example.memic.recognizedSentence.ui;

import com.example.memic.recognizedSentence.application.RecognizedSentenceService;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class RecognizedSentenceController {

    private final RecognizedSentenceService recognizedSentenceService;

    public RecognizedSentenceController(RecognizedSentenceService recognizedSentenceService) {
        this.recognizedSentenceService = recognizedSentenceService;
    }

    @PostMapping("/recognized-sectences/{sentenceId}")
    public ResponseEntity<RecognizedSentenceResponse> extractSpeech(
            @RequestPart MultipartFile speech,
            @PathVariable Long sentenceId
    ) {
        RecognizedSentenceResponse response = recognizedSentenceService.transcribe(speech, sentenceId);
        return ResponseEntity.ok(response);
    }
}
