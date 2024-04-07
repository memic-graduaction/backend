package com.example.memic.phrase.domain;

import com.example.memic.phrase.dto.PhraseTranslationRequest;
import com.example.memic.phrase.dto.TranslatedPhrase;

public interface PhraseTranslator {

    TranslatedPhrase translate(PhraseTranslationRequest request);
}
