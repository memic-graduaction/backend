package com.example.memic.transcription.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.common.auth.AuthContext;
import com.example.memic.common.auth.AuthInterceptor;
import com.example.memic.common.database.NoTransactionExtension;
import com.example.memic.member.domain.Member;
import com.example.memic.member.domain.MemberRepository;
import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.domain.TranscriptionRepository;
import com.example.memic.transcription.dto.ScrapCreateRequest;
import com.example.memic.transcription.dto.ScrapCreatedResponse;
import com.example.memic.transcription.dto.ScrapResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@AutoConfigureMockMvc
@ExtendWith(NoTransactionExtension.class)
class ScrapIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TranscriptionRepository transcriptionRepository;

    @MockBean
    AuthInterceptor authInterceptor;

    @MockBean
    AuthContext authContext;

    @Test
    void 저장한_스크랩을_최신순으로_조회할_수_있다() throws Exception {
        var transcription = new Transcription("https://test", Map.of(LocalTime.of(0, 0), "hello"));
        var transcription2 = new Transcription("https://test2", Map.of(LocalTime.of(0, 0), "hello"));
        var savedTranscription = transcriptionRepository.save(transcription);
        var savedTranscription2 = transcriptionRepository.save(transcription2);
        var newMember = memberRepository.save(new Member("hello@memeic.com", "world"));

        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(authContext.getMemberId()).thenReturn(newMember.getId());

        final var response1 = saveScrap(savedTranscription);

        final var response2 = saveScrap(savedTranscription2);

        final var mvcResult = getScraps();

        List<ScrapResponse> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        SoftAssertions.assertSoftly(
                softly -> {
                    softly.assertThat(response).hasSize(2);
                    softly.assertThat(response).containsExactly(
                            new ScrapResponse(response2.id(), transcription2.getId(), transcription2.getUrl()),
                            new ScrapResponse(response1.id(), transcription.getId(), transcription.getUrl())
                    );
                }
        );
    }

    @Test
    void 저장한_스크랩을_삭제할_수_있다() throws Exception {
        var transcription = new Transcription("https://test", Map.of(LocalTime.of(0, 0), "hello"));
        var savedTranscription = transcriptionRepository.save(transcription);
        var newMember = memberRepository.save(new Member("hello@memeic.com", "world"));

        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(authContext.getMemberId()).thenReturn(newMember.getId());

        final var response = saveScrap(savedTranscription);

        mockMvc.perform(delete("/v1/scraps/{id}", response.id()))
               .andExpect(status().isOk());

        final var mvcResult = getScraps();

        List<ScrapResponse> response2 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(response2).isEmpty();
    }

    private ScrapCreatedResponse saveScrap(final Transcription savedTranscription) throws Exception {
        final var postResponse1 = mockMvc.perform(post("/v1/scraps")
                                                 .contentType(MediaType.APPLICATION_JSON)
                                                 .content(objectMapper.writeValueAsString(new ScrapCreateRequest(savedTranscription.getId()))))
                                         .andExpect(status().isOk())
                                         .andReturn();

        return objectMapper.readValue(postResponse1.getResponse().getContentAsString(), ScrapCreatedResponse.class);
    }

    private MvcResult getScraps() throws Exception {
        return mockMvc.perform(get("/v1/scraps"))
                      .andExpect(status().isOk())
                      .andReturn();
    }
}
