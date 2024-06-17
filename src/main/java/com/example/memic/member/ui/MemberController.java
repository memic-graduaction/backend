package com.example.memic.member.ui;

import com.example.memic.common.auth.Authorization;
import com.example.memic.member.application.MemberService;
import com.example.memic.member.domain.Member;
import com.example.memic.member.dto.MemberSignInRequest;
import com.example.memic.member.dto.MemberSignInResponse;
import com.example.memic.member.dto.MemberSignUpRequest;
import com.example.memic.member.dto.MemberSignUpResponse;
import com.example.memic.member.dto.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/sign-up")
    public ResponseEntity<MemberSignUpResponse> signUp(
            @RequestBody MemberSignUpRequest request
    ) {
        MemberSignUpResponse response = memberService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/members/sign-in")
    public ResponseEntity<MemberSignInResponse> signIn(
            @RequestBody MemberSignInRequest request
    ) {
        MemberSignInResponse response = memberService.signIn(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/passwords")
    public ResponseEntity<Void> passwordUpdate(
            @RequestBody UpdatePasswordRequest request,
            @Authorization Member member
    ) {
        memberService.updatePassword(request, member);
        return ResponseEntity.ok().build();
    }
}
