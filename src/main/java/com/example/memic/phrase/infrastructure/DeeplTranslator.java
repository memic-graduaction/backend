package com.example.memic.phrase.infrastructure;

import com.deepl.api.DeepLException;
import com.deepl.api.LanguageCode;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import com.example.memic.phrase.domain.PhraseTranslator;
import com.example.memic.phrase.dto.PhraseTranslationRequest;
import com.example.memic.phrase.dto.TranslatedPhrase;
import com.example.memic.phrase.exception.TranslationException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class DeeplTranslator implements PhraseTranslator {

    private static final String DEEPL_LOG_FORMAT = """
            Deepl 번역 실패
            stackTrace: %s
            message: %s
            """;
    private static final Logger logger = LoggerFactory.getLogger(DeeplTranslator.class);

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
            final String stackTrace = Arrays.stream(e.getStackTrace())
                                            .sequential()
                                            .map(StackTraceElement::toString)
                                            .collect(Collectors.joining("\n"));
            String formattedLog = String.format(DEEPL_LOG_FORMAT, stackTrace, e.getMessage());
            logger.error(formattedLog);
            throw new TranslationException("Deepl 번역에 실패했습니다. 다시 시도해주세요.");
        }
    }
}
