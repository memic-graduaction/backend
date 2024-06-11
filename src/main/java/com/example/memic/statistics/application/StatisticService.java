package com.example.memic.statistics.application;

import com.example.memic.member.domain.Member;
import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import com.example.memic.recognizedSentence.repository.RecognizedSentenceRepository;
import com.example.memic.statistics.dto.StatisticResponse;
import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.domain.TranscriptionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public StatisticResponse getStatistic(final Integer month, final Member member) {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(month);

        List<RecognizedSentence> recentRecognizedSentences = recognizedSentenceRepository.findBySpokenAtAfterAndSpeaker(monthAgo, member);
        List<Transcription> recentTranscriptions = transcriptionRepository.findByTranscribedAtAfterAndMembers_Member(monthAgo, member);

        Set<LocalDate> uniqueDates = new HashSet<>();

        recentRecognizedSentences.stream()
                                 .map(recognizedSentence -> recognizedSentence.getSpokenAt().toLocalDate())
                                 .forEach(uniqueDates::add);

        recentTranscriptions.stream()
                            .map(transcription -> transcription.getTranscribedAt().toLocalDate())
                            .forEach(uniqueDates::add);

        return StatisticResponse.of(uniqueDates.size(), recentRecognizedSentences.size(), recentTranscriptions.size());
    }
}
