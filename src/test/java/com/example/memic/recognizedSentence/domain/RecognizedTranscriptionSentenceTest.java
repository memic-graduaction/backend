package com.example.memic.recognizedSentence.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.memic.transcription.domain.TranscriptionSentence;
import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class RecognizedTranscriptionSentenceTest {

    @Test
    void 원본_문장과_정확히_일치하면_Recognized_Word의_일치여부가_전부_True이다() {
        //given
        final var originalSentence = sentenceWithoutTranscription("This is a test.");
        final var rawRecognizedSentence = "This is a test.";

        //when
        final var recognizedSentence = new RecognizedSentence(rawRecognizedSentence, originalSentence);

        //then
        assertTrue(
                recognizedSentence.getRecognizedWords()
                                  .stream()
                                  .allMatch(RecognizedWord::isMatchedWithTranscription)
        );
    }

    @Test
    void 문장이_일치하면_대소문자_여부와_관계없이_Recognized_Word의_일치여부가_전부_True이다() {
        //given
        final var originalSentence = sentenceWithoutTranscription("This, is a test.");
        final var rawRecognizedSentence = "this IS A Test.";

        //when
        final var recognizedSentence = new RecognizedSentence(rawRecognizedSentence, originalSentence);

        //then
        assertTrue(
                recognizedSentence.getRecognizedWords()
                                  .stream()
                                  .allMatch(RecognizedWord::isMatchedWithTranscription)
        );
    }


    @Test
    void 원본_문장에_없는_단어는_일치여부가_False이다() {
        //given
        final var originalSentence = sentenceWithoutTranscription("This is a test.");
        final var rawRecognizedSentence = "This is a test. Hello";

        //when
        final var recognizedSentence = new RecognizedSentence(rawRecognizedSentence, originalSentence);

        final var notMatchedWords = recognizedSentence.getRecognizedWords()
                                                      .stream()
                                                      .filter(recognizedWord -> !recognizedWord.isMatchedWithTranscription())
                                                      .toList();
        //then
        SoftAssertions.assertSoftly(
                softly -> {
                    assertEquals(1, notMatchedWords.size());
                    softly.assertThat(notMatchedWords.get(0).getWord()).isEqualTo("Hello");
                }
        );
    }

    @Test
    void 인덱스가_일치하지_않더라도_일정_범위_안에_단어가_존재하면_일치여부는_True이다() {
        //given
        final var originalSentence = sentenceWithoutTranscription("This is a Hello test.");
        final var rawRecognizedSentence = "This is a test. Hello";

        //when
        final var recognizedSentence = new RecognizedSentence(rawRecognizedSentence, originalSentence);

        //then
        assertTrue(
                recognizedSentence.getRecognizedWords()
                                  .stream()
                                  .allMatch(RecognizedWord::isMatchedWithTranscription)
        );
    }

    @Test
    void 원본_문장에_존재하더라도_일정_범위_이상_떨어져있다면_일치여부는_False이다() {
        //given
        final var originalSentence = sentenceWithoutTranscription("This is a test.");
        final var rawRecognizedSentence = "This is a wrong range test.";

        //when
        final var recognizedSentence = new RecognizedSentence(rawRecognizedSentence, originalSentence);

        final var notMatchedWords = recognizedSentence.getRecognizedWords()
                                                      .stream()
                                                      .filter(recognizedWord -> !recognizedWord.isMatchedWithTranscription())
                                                      .toList();

        //then
        SoftAssertions.assertSoftly(
                softly -> {
                    assertEquals(3, notMatchedWords.size());
                    softly.assertThat(notMatchedWords.get(0).getWord()).isEqualTo("wrong");
                    softly.assertThat(notMatchedWords.get(1).getWord()).isEqualTo("range");
                    softly.assertThat(notMatchedWords.get(2).getWord()).isEqualTo("test.");
                }
        );
    }

    @Test
    void 성능_측정_테스트() {
        //given
        final var startTime = System.currentTimeMillis();
        final var original = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB";
        final var target = "AAAAAAAAAAAAAAAAB";
        final var originalSentence = sentenceWithoutTranscription(original);

        //when
        final var recognizedSentence = new RecognizedSentence(target, originalSentence);
        final var endTime = System.currentTimeMillis();

        //then
        System.out.println(("실행 시간 : " + (endTime - startTime) + "ms"));
    }

    private TranscriptionSentence sentenceWithoutTranscription(String content) {
        return new TranscriptionSentence(null, LocalTime.MIDNIGHT, content);
    }
}
