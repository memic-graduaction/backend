package com.example.memic.recognizedSentence.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.memic.member.domain.Member;
import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import com.example.memic.recognizedSentence.domain.RecognizedSentenceRepository;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceCountResponse;
import com.example.memic.transcription.domain.TranscriptionSentence;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class RecognizedSentenceServiceTest {

    @MockBean
    WhisperApiClient whisperApiClient;

    @MockBean
    RecognizedSentenceRepository recognizedSentenceRepository;

    @Autowired
    RecognizedSentenceService recognizedSentenceService;

    @Test
    void 말한_문장들이_주어졌을_때_해당_월의_말한_문장_수를_반환한다() {
        // given
        Member member = new Member();
        final var originalSentence = new TranscriptionSentence(
                null,
                LocalTime.MIDNIGHT,
                "hello"
        );
        final var sentence0 = new RecognizedSentence("안녕하세요", originalSentence, member);
        final var sentence1 = new RecognizedSentence("안녕하세요", originalSentence, member);
        final var sentence2 = new RecognizedSentence("안녕하세요", originalSentence, member);
        final var sentence3 = new RecognizedSentence("안녕하세요", originalSentence, member);
        final var sentence4 = new RecognizedSentence("안녕하세요", originalSentence, member);

        sentence0.setSpokenAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        sentence1.setSpokenAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        sentence2.setSpokenAt(LocalDateTime.of(2021, 1, 2, 0, 0));
        sentence3.setSpokenAt(LocalDateTime.of(2021, 1, 3, 0, 0));
        sentence4.setSpokenAt(LocalDateTime.of(2021, 2, 1, 0, 0));

        given(recognizedSentenceRepository.findBySpeaker(any())).willReturn(List.of(
                sentence0,
                sentence1,
                sentence2,
                sentence3,
                sentence4
        ));

        Month month = Month.JANUARY;

        // when
        final var responses = recognizedSentenceService.getRecognizedSentenceCounts(2021, month, member);

        // then
        final var expected = List.of(
                new RecognizedSentenceCountResponse(1, 2),
                new RecognizedSentenceCountResponse(2, 1),
                new RecognizedSentenceCountResponse(3, 1)
        );
        assertThat(responses).containsExactlyElementsOf(expected);
    }
}
