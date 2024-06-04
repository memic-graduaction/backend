package com.example.memic.statistics.dto;

public record StatisticResponse(
        Integer visit,
        Integer records,
        Integer convert
) {

    public static StatisticResponse of(Integer visit, Integer records, Integer convert) {
        return new StatisticResponse(visit, records, convert);
    }
}
