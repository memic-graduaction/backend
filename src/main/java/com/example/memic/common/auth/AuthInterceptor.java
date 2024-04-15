package com.example.memic.common.auth;

import com.example.memic.common.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthContext authContext;

    public AuthInterceptor(final JwtTokenProvider jwtTokenProvider, final AuthContext authContext) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authContext = authContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            throw new InvalidTokenException("인증 정보가 없습니다.");
        }

        Long memberId = jwtTokenProvider.parseToken(authHeader);
        authContext.setMemberId(memberId);
        return true;
    }
}
