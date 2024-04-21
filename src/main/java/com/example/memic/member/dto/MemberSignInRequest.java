package com.example.memic.member.dto;

public record MemberSignInRequest(
        String email,
        String password
) {
}
