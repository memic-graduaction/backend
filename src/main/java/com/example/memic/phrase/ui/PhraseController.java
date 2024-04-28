package com.example.memic.phrase.ui;

import com.example.memic.common.auth.Authorization;
import com.example.memic.member.domain.Member;
import com.example.memic.phrase.application.PhraseService;
import com.example.memic.phrase.dto.PhraseCreateRequest;
import com.example.memic.phrase.dto.PhraseCreatedResponse;
import com.example.memic.phrase.dto.PhraseResponse;
import com.example.memic.phrase.dto.TranscriptionPhraseResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/phrases")
public class PhraseController {

    private final PhraseService phraseService;

    public PhraseController(final PhraseService phraseService) {
        this.phraseService = phraseService;
    }

    @PostMapping
    public ResponseEntity<PhraseCreatedResponse> createPhrase(
            @RequestBody final PhraseCreateRequest request,
            @Authorization final Member member
    ) {
        PhraseCreatedResponse response = phraseService.createPhrase(request, member);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PhraseResponse>> getPhrases(
            @Authorization final Member member
    ) {
        List<PhraseResponse> responses = phraseService.getPhrases(member);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/transcriptions/{transcriptionId}")
    public ResponseEntity<List<TranscriptionPhraseResponse>> getTranscriptionPhrases(
            @PathVariable final Long transcriptionId,
            @Authorization final Member member
    ) {
        List<TranscriptionPhraseResponse> responses = phraseService.getTranscriptionPhrases(transcriptionId, member);
        return ResponseEntity.ok(responses);
    }
}
