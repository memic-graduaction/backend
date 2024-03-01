package com.example.memic.speech.ui;

import com.example.memic.speech.application.SpeechService;
import com.example.memic.speech.dto.SpeechResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class SpeechController {

    private final SpeechService speechService;

    public SpeechController(SpeechService speechService) {
        this.speechService = speechService;
    }

    @PostMapping("/recognized-sectences/{sentenceId}")
    public ResponseEntity<SpeechResponse> extractSpeech(
            @RequestPart MultipartFile speech,
            @PathVariable Long sentenceId
    ) {
        SpeechResponse response = speechService.transcribe(speech, sentenceId);
        return ResponseEntity.ok(response);
    }
}
