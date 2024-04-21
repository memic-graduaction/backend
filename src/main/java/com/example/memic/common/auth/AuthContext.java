package com.example.memic.common.auth;

import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AuthContext {

    private Long memberId;

    public void setMemberId(final Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        Objects.requireNonNull(memberId, "유저 정보가 초기화되지 않았습니다.");
        return memberId;
    }
}
