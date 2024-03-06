package com.example.memic.transcription.ui;

import com.example.memic.transcription.dto.TranscriptionCreateRequest;
import com.example.memic.transcription.dto.TranscriptionResponse;
import com.example.memic.transcription.application.TranscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/v1")
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    @PostMapping("/transcriptions")
    public ResponseEntity<TranscriptionResponse> extractTranscription(
            @RequestBody TranscriptionCreateRequest request
    ) {
        TranscriptionResponse responses = transcriptionService.transcribe(request);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/transcriptions/{id}")
    public ResponseEntity<TranscriptionResponse> getTranscription(
            @PathVariable("id") Long id
    ) {
        TranscriptionResponse responses = transcriptionService.getTranscription(id);
        return ResponseEntity.ok(responses);
    }
}
