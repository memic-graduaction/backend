package com.example.memic.member.dto;

public record MemberSignUpRequest(
        String email,
        String password
) {
}
