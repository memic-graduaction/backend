package com.example.memic.member.application;

import com.example.memic.common.auth.JwtTokenProvider;
import com.example.memic.member.domain.Member;
import com.example.memic.member.domain.MemberRepository;
import com.example.memic.member.dto.MemberSignInRequest;
import com.example.memic.member.dto.MemberSignInResponse;
import com.example.memic.member.dto.MemberSignUpRequest;
import com.example.memic.member.dto.MemberSignUpResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public MemberService(final JwtTokenProvider jwtTokenProvider,
                         final MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public MemberSignUpResponse signUp(final MemberSignUpRequest request) {
        memberRepository.findByEmail(request.email())
                        .ifPresent(member -> {throw new EntityExistsException("해당 이메일로 가입한 계정이 있습니다.");});

        Member member = new Member(request.email(), request.password());
        Member savedMember = memberRepository.save(member);
        String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId());
        return new MemberSignUpResponse(savedMember.getId(), accessToken);
    }

    public MemberSignInResponse signIn(final MemberSignInRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일에 대한 계정 정보가 없습니다."));

        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        return new MemberSignInResponse(member.getId(), accessToken);
    }
}
