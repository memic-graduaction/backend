package com.example.memic.common.auth;

import com.example.memic.common.exception.AuthorizationException;
import com.example.memic.member.domain.Member;
import com.example.memic.member.domain.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final AuthContext authContext;

    public MemberArgumentResolver(final MemberRepository memberRepository, final AuthContext authContext) {
        this.memberRepository = memberRepository;
        this.authContext = authContext;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Member.class)
                && parameter.hasParameterAnnotation(Authorization.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        Long possibleMemberId = authContext.getMemberId();
        return memberRepository.findById(possibleMemberId)
                               .orElseThrow(() -> new AuthorizationException("인증된 사용자가 아닙니다."));
    }
}
