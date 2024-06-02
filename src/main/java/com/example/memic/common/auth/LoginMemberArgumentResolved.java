package com.example.memic.common.auth;

import com.example.memic.member.domain.Member;
import com.example.memic.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginMemberArgumentResolved implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final LoginContext loginContext;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Member.class)
                && parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (loginContext.isLoggedIn()) {
            return memberRepository.findById(loginContext.getMemberId())
                                   .orElseThrow(() -> new IllegalArgumentException("유저 정보가 올바르지 않습니다."));
        }
        return Member.NON_MEMBER;
    }
}
