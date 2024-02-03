package com.example.memic.transcription.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.memic.transcription.exception.InvalidTranscriptionException;
import java.time.LocalTime;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
class SentenceTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 내용은_비어있으면_예외를_던진다(String content) {
        //given
        LocalTime time = LocalTime.of(0, 0, 1);

        //when
        ThrowingCallable throwingCallable = () -> new Sentence(time, content);

        //then
        assertThatThrownBy(throwingCallable).isExactlyInstanceOf(InvalidTranscriptionException.class);
    }
}