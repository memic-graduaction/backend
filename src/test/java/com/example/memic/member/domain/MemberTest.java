package com.example.memic.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.example.memic.member.exception.InvalidMemberException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    @Test
    void 이메일은_형식에_안맞으면_예외를_던진다() {
        //given
        final var email = "1234";
        final var password = "1234";

        //when
        final ThrowingCallable throwingCallable = () -> new Member(email, password);

        //then
        assertThatThrownBy(throwingCallable).isInstanceOf(InvalidMemberException.class);
    }

    @Test
    void 이메일은_형식에_맞아야_한다() {
        //given
        final var email = "test@naver.com";
        final var password = "test";

        //when
        final ThrowingSupplier<Member> supplier = () -> new Member(email, password);

        //then
        assertDoesNotThrow(supplier);
    }
}