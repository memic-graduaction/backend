package com.example.memic.config.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.memic.common.auth.JwtTokenProvider;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class JwtTokenProviderTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("testsestesetsetsetsetesttestestetstes!tsetsetse", 1L);

    @Test
    void 토큰을_정상적으로_생성_파싱한다() {
        long expected = 3L;

        String token = jwtTokenProvider.createAccessToken(expected);
        Long actual = jwtTokenProvider.parseToken(token);
        
        assertThat(actual).isEqualTo(expected);
    }
}