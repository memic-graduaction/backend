package com.example.memic.phrase.ui;

import com.example.memic.phrase.application.PhraseTranslationService;
import com.example.memic.phrase.dto.PhraseTranslationRequest;
import com.example.memic.phrase.dto.PhraseTranslationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class PhraseTranslationController {

    private final PhraseTranslationService phraseTranslationService;

    public PhraseTranslationController(PhraseTranslationService phraseTranslationService) {
        this.phraseTranslationService = phraseTranslationService;
    }

    @PostMapping("/translate")
    public ResponseEntity<PhraseTranslationResponse> translatePhrase(
            @RequestBody PhraseTranslationRequest request
    ) {
        final PhraseTranslationResponse response = phraseTranslationService.translate(request);
        return ResponseEntity.ok(response);
    }
}
