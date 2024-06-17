package com.example.memic.transcription.ui;

import com.example.memic.common.auth.Authorization;
import com.example.memic.member.domain.Member;
import com.example.memic.transcription.application.ScrapService;
import com.example.memic.transcription.dto.ScrapCreateRequest;
import com.example.memic.transcription.dto.ScrapCreatedResponse;
import com.example.memic.transcription.dto.ScrapResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/scraps")
@RestController
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<ScrapCreatedResponse> createScrap(
            @Authorization Member member,
            @RequestBody ScrapCreateRequest request
    ) {
        ScrapCreatedResponse response = scrapService.createScrap(member, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ScrapResponse>> getScraps(
            @Authorization Member member
    ) {
        List<ScrapResponse> response = scrapService.getScraps(member);
        return ResponseEntity.ok(response);
    }
}
