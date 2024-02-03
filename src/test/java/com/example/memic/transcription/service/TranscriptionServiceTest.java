package com.example.memic.transcription.service;

import java.time.LocalTime;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TranscriptionServiceTest {

    @Autowired
    TranscriptionService service;

    @Test
    void 텍스트_파일을_파싱해서_시간과_문장으로_나눈다() {
        //given
        String text = """
                [0:00:00]We didn't see any significant changes in the user metrics, but I think we should leave it for a week to get more data.
                [0:00:05]Other than that, we talked to Legal about the other workstream, and it seems like they're backlogged, so they would have to revisit it in Q4, so we're blocked on that.
                [0:00:15]Jomo, do you have any updates on the infowork?
                [0:00:20]Nope. No updates for me.
                [0:00:24]Okay. Yeah. Steve, do you have anything you want to add to what we just said?
                [0:00:29]Sorry about that. I had to do something.
                [0:00:33]Oh, yeah? Well, I f***ed your mom last night.
                [0:00:36]Sorry, Jomo. What did you say?
                """;

        //when
        Map<LocalTime, String> parsed = service.parseLogText(text);

        //then
        SoftAssertions.assertSoftly(
                softly -> {
                    softly.assertThat(parsed).hasSize(8);
                    softly.assertThat(parsed.get(LocalTime.of(0, 0, 0))).isEqualTo("We didn't see any significant changes in the user metrics, but I think we should leave it for a week to get more data.");
                }
        );
    }
}