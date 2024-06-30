package com.example.memic.transcription.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.memic.member.domain.Member;
import com.example.memic.member.domain.MemberRepository;
import com.example.memic.transcription.domain.ScrapRepository;
import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.domain.TranscriptionRepository;
import com.example.memic.transcription.dto.ScrapCreateRequest;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ScrapServiceTest {

    @Autowired
    private ScrapService scrapService;

    @Autowired
    private ScrapRepository scrapRepository;

    @Autowired
    private TranscriptionRepository transcriptionRepository;

    @Autowired
    private MemberRepository memberRepository;



    @BeforeEach
    void init() {

        Member member = new Member("yunho@naver.com", "1234");
        memberRepository.save(member);
        Transcription transcription = new Transcription("https://", Map.of(LocalTime.now(), "hello"));
        transcriptionRepository.save(transcription);
    }


    @Test
    void 스크랩을_저장한다() {
        //given
        Member member = memberRepository.findByEmail("yunho@naver.com").get();
        ScrapCreateRequest request = new ScrapCreateRequest(1L);

        //when
        for (int i = 0; i < 10; i++) {
            scrapService.createScrap(member, request);
        }
        //then
        assertEquals(1, scrapRepository.findAllByMember(member).size());
    }

    @Test
    void 병렬_환경에서_스크랩을_저장한다() throws InterruptedException {
        //given
        Member member = memberRepository.findByEmail("yunho@naver.com").get();
        ScrapCreateRequest request = new ScrapCreateRequest(1L);
        int numberOfThread = 50;

        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThread);

        for (int i = 0; i < numberOfThread; i++) {
            threadPool.execute(() -> {
                try {
                    scrapService.createScrap(member, request);
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        //then
        assertEquals(1, scrapRepository.findAllByMember(member).size());
    }
}
