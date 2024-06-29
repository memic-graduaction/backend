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

//    @Autowired
//    private TransactionTemplate transactionTemplate;
//
//    @Autowired
//    private PlatformTransactionManager platformTransactionManager;


    @BeforeEach
    void init() {
//        transactionTemplate = new TransactionTemplate(platformTransactionManager);
//        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        Member member = new Member("yunho@naver.com", "1234");
        memberRepository.save(member);
        Transcription transcription = new Transcription("https://", Map.of(LocalTime.now(), "hello"));
        transcriptionRepository.save(transcription);
    }


    @Test
    void 일반_테스트() {
        Member member = memberRepository.findByEmail("yunho@naver.com").get();
        ScrapCreateRequest request = new ScrapCreateRequest(1L);
        for (int i = 0; i < 10; i++) {
            scrapService.createScrap(member, request);
        }
        System.out.println("############");
        System.out.println(scrapRepository.findAllByMember(member).size());
        System.out.println("############");
        assertEquals(1, scrapRepository.findAllByMember(member).size());
    }

    @Test
    void 동시성_테스트() throws InterruptedException {
        Member member = memberRepository.findByEmail("yunho@naver.com").get();
        ScrapCreateRequest request = new ScrapCreateRequest(1L);

        //given
        ExecutorService threadPool = Executors.newFixedThreadPool(50);
        CountDownLatch countDownLatch = new CountDownLatch(50);

        for (int i = 0; i < 50; i++) {
//            transactionTemplate.executeWithoutResult((transactionStatus -> {
            threadPool.execute(() -> {
                try {
                    scrapService.createScrap(member, request);
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
//            }));
        }
        countDownLatch.await();

        //then
        System.out.println("############");
        System.out.println(scrapRepository.findAllByMember(member).size());
        System.out.println("############");

        assertEquals(1, scrapRepository.findAllByMember(member).size());
    }
}
