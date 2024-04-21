package com.example.memic.member.ui;

import com.example.memic.member.application.MemberService;
import com.example.memic.member.dto.MemberSignInRequest;
import com.example.memic.member.dto.MemberSignInResponse;
import com.example.memic.member.dto.MemberSignUpRequest;
import com.example.memic.member.dto.MemberSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<MemberSignUpResponse> signUp(
            @RequestBody MemberSignUpRequest request
    ) {
        MemberSignUpResponse response = memberService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<MemberSignInResponse> signIn(
            @RequestBody MemberSignInRequest request
    ) {
        MemberSignInResponse response = memberService.signIn(request);
        return ResponseEntity.ok(response);
    }
}
