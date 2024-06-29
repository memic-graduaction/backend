package com.example.memic.statistics.application;

import com.example.memic.member.domain.Member;
import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import com.example.memic.recognizedSentence.domain.RecognizedSentenceRepository;
import com.example.memic.statistics.dto.StatisticResponse;
import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.domain.TranscriptionRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final TranscriptionRepository transcriptionRepository;
    private final RecognizedSentenceRepository recognizedSentenceRepository;

    @Transactional(readOnly = true)
    public StatisticResponse getStatistic(
            final Integer year,
            final Integer month,
            final Member member
    ) {
        List<RecognizedSentence> recentRecognizedSentences = recognizedSentenceRepository.findByYearMonthSpeaker(year, month, member);
        List<Transcription> recentTranscriptions = transcriptionRepository.findByYearMonthAndMember(year, month, member);

        Set<Integer> uniqueDates = new HashSet<>();

        recentRecognizedSentences.stream()
                                 .map(recognizedSentence -> recognizedSentence.getSpokenAt().toLocalDate().getDayOfMonth())
                                 .forEach(uniqueDates::add);

        recentTranscriptions.stream()
                            .map(transcription -> transcription.getTranscribedAt().toLocalDate().getDayOfMonth())
                            .forEach(uniqueDates::add);

        return StatisticResponse.of(uniqueDates.stream().toList(), recentRecognizedSentences.size(), recentTranscriptions.size());
    }
}
