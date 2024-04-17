package com.example.memic.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.example.memic.member.exception.InvalidMemberException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    @Test
    void 이메일은_형식에_맞아야한다() {
        //given
        final var email = "1234";
        final var password = "1234";

        //when
        final ThrowingCallable throwingCallable = () -> new Member(email, password);

        //then
        assertThatThrownBy(throwingCallable).isInstanceOf(InvalidMemberException.class);
    }
}