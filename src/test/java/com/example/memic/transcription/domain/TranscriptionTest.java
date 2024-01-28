package com.example.memic.transcription.domain;

import com.example.memic.transcription.exception.InvalidTranscriptionException;
import java.time.LocalTime;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiChracters")
class TranscriptionTest {

    @Test
    void URL이_Https로_시작하지않으면_예외를_던진다() {
        //given
        //when
        ThrowingCallable throwingCallable = () -> new Transcription("http://", Map.of(LocalTime.now(), "hello"));

        //then
        Assertions.assertThatThrownBy(throwingCallable).isInstanceOf(InvalidTranscriptionException.class);
    }
}