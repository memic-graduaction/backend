package com.example.memic.member.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.common.database.NoTransactionExtension;
import com.example.memic.member.dto.MemberSignInRequest;
import com.example.memic.member.dto.MemberSignInResponse;
import com.example.memic.member.dto.MemberSignUpRequest;
import com.example.memic.member.dto.MemberSignUpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("NonAsciiCharacters")
@AutoConfigureMockMvc
@SpringBootTest
class MemberIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @ExtendWith(NoTransactionExtension.class)
    void 회원가입_후_로그인을_한다() throws Exception {
        final var email = "test@naver.com";
        final var password = "test";

        final var signUpRequest = new MemberSignUpRequest(email, password);
        final var mvcSignUpResponse = mockMvc.perform(post("/v1/members/sign-up")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(signUpRequest)))
                                          .andDo(print())
                                          .andExpect(status().isOk())
                                          .andReturn();

        final var signUpResponse = objectMapper.readValue(
                mvcSignUpResponse.getResponse().getContentAsString(),
                MemberSignUpResponse.class);

        final var signInRequest = new MemberSignInRequest(email, password);
        final var mvcSignInResponse = mockMvc.perform(post("/v1/members/sign-in")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(signInRequest)))
                                          .andDo(print())
                                          .andExpect(status().isOk())
                                          .andReturn();

        final var signInResponse = objectMapper.readValue(
                mvcSignInResponse.getResponse().getContentAsString(),
                MemberSignInResponse.class
        );

        assertThat(signUpResponse.id()).isEqualTo(signInResponse.id());
        // TODO: 특정
        // assertThat(signUpResponse.accessToken()).isEqualTo(signInResponse.accessToken());

    }
}
