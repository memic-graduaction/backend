package com.example.memic.speech.ui;

import com.example.memic.speech.application.SpeechService;
import com.example.memic.speech.dto.SpeechWordResponse;
import com.example.memic.speech.dto.SpeechWordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
public class SpeechController {

    private final SpeechService speechService;

    @PostMapping("speeches/words")
    public ResponseEntity<SpeechWordResponse> transcribeWord(
            @RequestPart MultipartFile speech,
            @RequestPart SpeechWordRequest word
    ) {
        SpeechWordResponse responses = speechService.transcribe(speech, word);
        return ResponseEntity.ok(responses);
    }
}
