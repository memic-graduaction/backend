package com.example.memic.transcription.ui;

import com.example.memic.transcription.service.TranscriptionService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TranscriptionResponse>> extractTranscription(
            @RequestBody TranscriptionCreateRequest request
    ) {
        List<TranscriptionResponse> responses = transcriptionService.transcribe(request);
        return ResponseEntity.ok(responses);
    }
}
