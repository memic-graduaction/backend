package com.example.memic.statistics.dto;

import java.util.List;

public record StatisticResponse(
        List<Integer> visitedDays,
        Integer records,
        Integer convert
) {

    public static StatisticResponse of(List<Integer> visitedDays, Integer records, Integer convert) {
        return new StatisticResponse(visitedDays, records, convert);
    }
}
