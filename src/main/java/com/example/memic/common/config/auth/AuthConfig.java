package com.example.memic.common.config.auth;

import com.example.memic.common.auth.AuthInterceptor;
import com.example.memic.common.auth.LoginCheckerInterceptor;
import com.example.memic.common.auth.LoginMemberArgumentResolved;
import com.example.memic.common.auth.MemberArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final AuthInterceptor authInterceptor;
    private final LoginMemberArgumentResolved loginMemberArgumentResolved;
    private final LoginCheckerInterceptor loginCheckerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/v1/members/**")
                .excludePathPatterns("/v1/transcriptions/*")
                .excludePathPatterns("/v1/transcriptions")
                .excludePathPatterns("/v1/recognized-sentences")
                .excludePathPatterns("/v1/speeches/words")
                .excludePathPatterns("/v1/translate")
                .excludePathPatterns("/v1/tags");

        registry.addInterceptor(loginCheckerInterceptor)
                .addPathPatterns("/v1/recognized-sentences")
                .addPathPatterns("/v1/transcriptions");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
        resolvers.add(loginMemberArgumentResolved);
    }
}
