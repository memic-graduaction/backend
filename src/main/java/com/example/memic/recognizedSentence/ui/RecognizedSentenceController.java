package com.example.memic.recognizedSentence.ui;

import com.example.memic.common.auth.Authorization;
import com.example.memic.common.auth.LoginMember;
import com.example.memic.member.domain.Member;
import com.example.memic.recognizedSentence.application.RecognizedSentenceService;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceCountResponse;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceRequest;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceResponse;
import java.time.Month;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/recognized-sentences")
public class RecognizedSentenceController {

    private final RecognizedSentenceService recognizedSentenceService;

    public RecognizedSentenceController(RecognizedSentenceService recognizedSentenceService) {
        this.recognizedSentenceService = recognizedSentenceService;
    }

    @PostMapping("")
    public ResponseEntity<RecognizedSentenceResponse> extractRecognizedSentence(
            @RequestPart MultipartFile speech,
            @RequestPart RecognizedSentenceRequest sentence,
            @LoginMember Member member
    ) {
        RecognizedSentenceResponse response = recognizedSentenceService.transcribe(speech, sentence, member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/counts")
    public ResponseEntity<List<RecognizedSentenceCountResponse>> getRecognizedSentenceCounts(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @Authorization Member member
    ) {
        Month monthEnum = Month.of(month);
        List<RecognizedSentenceCountResponse> responses = recognizedSentenceService.getRecognizedSentenceCounts(year, monthEnum, member);
        return ResponseEntity.ok(responses);
    }
}
