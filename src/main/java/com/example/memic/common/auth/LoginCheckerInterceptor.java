package com.example.memic.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckerInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final LoginContext authContext;

    public LoginCheckerInterceptor(final JwtTokenProvider jwtTokenProvider, final LoginContext authContext) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authContext = authContext;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null) {
            Long memberId = jwtTokenProvider.parseToken(authHeader);
            authContext.setMemberId(memberId);
        }
        return true;
    }
}
