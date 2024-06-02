package com.example.memic.common.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class LoginContext {

    private Boolean isLoggedIn = false;
    private Long memberId;

    public void setMemberId(final Long memberId) {
        this.isLoggedIn = true;
        this.memberId = memberId;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public Long getMemberId() {
        return memberId;
    }
}
