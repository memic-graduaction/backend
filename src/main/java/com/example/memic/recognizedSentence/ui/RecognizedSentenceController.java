package com.example.memic.recognizedSentence.ui;

import com.example.memic.recognizedSentence.application.RecognizedSentenceService;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceRequest;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceResponse;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/recognized-sentences")
    public ResponseEntity<RecognizedSentenceResponse> extractRecognizedSentence(
            @RequestPart MultipartFile speech,
            @RequestPart RecognizedSentenceRequest request
    ) {
        RecognizedSentenceResponse response = recognizedSentenceService.transcribe(speech, request);
        return ResponseEntity.ok(response);
    }
}
