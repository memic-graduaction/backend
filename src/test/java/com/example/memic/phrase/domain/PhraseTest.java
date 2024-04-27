package com.example.memic.phrase.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.example.memic.member.domain.Member;
import com.example.memic.phrase.exception.InvalidPhraseException;
import com.example.memic.transcription.domain.TranscriptionSentence;
import java.time.LocalTime;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


@SuppressWarnings("NonAsciiCharacters")
class PhraseTest {

    @Nested
    class 생성 {

        @Test
        void 끝_인덱스는_원본문장의_단어_개수_보다_작아야한다() {
            //given
            final var sentence = sentenceWithoutTranscription("hello bojun");
            final var member = new Member();

            //when
            final ThrowingCallable throwingCallable = () -> new Phrase("안녕 보준", member, sentence, 0, 2);

            //then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidPhraseException.class);
        }

        @Test
        void 시작_인덱스는_0보다_커야한다() {
            //given
            final var sentence = sentenceWithoutTranscription("hello bojun");
            final var member = new Member();

            //when
            final ThrowingCallable throwingCallable = () -> new Phrase("안녕 보준", member, sentence, -1, 1);

            //then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidPhraseException.class);
        }

        @Test
        void 시작_인덱스는_끝_인덱스보다_작아야한다() {
            //given
            final var sentence = sentenceWithoutTranscription("hello bojun");
            final var member = new Member();

            //when
            final ThrowingCallable throwingCallable = () -> new Phrase("안녕 보준", member, sentence, 1, 0);

            //then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidPhraseException.class);
        }

        @ParameterizedTest
        @CsvSource({"0, 0", "0, 1", "1, 1"})
        void 인덱스_조건에_맞으면_정상_생성한다(int startIndex, int endIndex) {
            //given
            final var sentence = sentenceWithoutTranscription("hello bojun");
            final var member = new Member();

            //when
            final ThrowingSupplier<Phrase> supplier = () -> new Phrase("안녕 보준", member, sentence, startIndex, endIndex);

            //then
            assertDoesNotThrow(supplier);
        }
    }

    @Test
    void 이미_가진_태그를_등록하면_무시한다() {
        //given
        final var sentence = sentenceWithoutTranscription("hello bojun");
        final var member = new Member();
        final var phrase = new Phrase("안녕 보준", member, sentence, 0, 1);
        final var tag = new Tag("인사");

        //when
        phrase.addTag(tag);
        phrase.addTag(tag);

        //then
        assertThat(phrase.getTags()).hasSize(1);
    }

    private TranscriptionSentence sentenceWithoutTranscription(String content) {
        return new TranscriptionSentence(null, LocalTime.MIDNIGHT, content);
    }
}
