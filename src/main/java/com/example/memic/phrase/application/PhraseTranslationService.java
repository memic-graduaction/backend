package com.example.memic.phrase.application;

import com.example.memic.phrase.domain.PhraseTranslator;
import com.example.memic.phrase.dto.PhraseTranslationRequest;
import com.example.memic.phrase.dto.PhraseTranslationResponse;
import com.example.memic.phrase.dto.TranslatedPhrase;
import org.springframework.stereotype.Service;

@Service
public class PhraseTranslationService {

    private final PhraseTranslator phraseTranslator;

    public PhraseTranslationService(PhraseTranslator phraseTranslator) {
        this.phraseTranslator = phraseTranslator;
    }

    public PhraseTranslationResponse translate(PhraseTranslationRequest request) {
        final TranslatedPhrase translatedPhrase = phraseTranslator.translate(request);
        return new PhraseTranslationResponse(translatedPhrase.meaningInKorean());
    }
}
