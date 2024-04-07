package com.example.memic.phrase.infrastructure;

import com.deepl.api.DeepLException;
import com.deepl.api.LanguageCode;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import com.example.memic.phrase.domain.PhraseTranslator;
import com.example.memic.phrase.dto.PhraseTranslationRequest;
import com.example.memic.phrase.dto.TranslatedPhrase;
import com.example.memic.phrase.exception.TranslationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class DeeplTranslator implements PhraseTranslator {

    private final Translator translator;

    public DeeplTranslator(
            @Value("${deepl.apiKey}") final String apiKey
    ) {
        this.translator = new Translator(apiKey);
    }

    @Override
    public TranslatedPhrase translate(final PhraseTranslationRequest request) {
        try {
            final String phraseInEnglish = request.phrase();
            final TextResult translation = translator.translateText(phraseInEnglish, LanguageCode.English, LanguageCode.Korean);
            return new TranslatedPhrase(phraseInEnglish, translation.getText());
        } catch (final DeepLException | InterruptedException e) {
            throw new TranslationException("Deepl 번역에 실패했습니다. 다시 시도해주세요.");
        }
    }
}
