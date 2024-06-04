package com.example.memic.statistics.ui;

import com.example.memic.common.auth.Authorization;
import com.example.memic.member.domain.Member;
import com.example.memic.statistics.application.StatisticService;
import com.example.memic.statistics.dto.StatisticResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class StatisticsController {

    private final StatisticService statisticService;

    public StatisticsController(final StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticResponse> getStatistic(
            @RequestParam Integer month,
            @Authorization Member member
    ) {
        StatisticResponse response = statisticService.getStatistic(month, member);
        return ResponseEntity.ok(response);
    }
}
