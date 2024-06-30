package com.example.memic.recognizedSentence.application;

import com.example.memic.member.domain.Member;
import com.example.memic.recognizedSentence.domain.RecognizedSentence;
import com.example.memic.recognizedSentence.domain.RecognizedSentenceRepository;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceCountResponse;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceRequest;
import com.example.memic.recognizedSentence.dto.RecognizedSentenceResponse;
import com.example.memic.transcription.domain.TranscriptionSentence;
import com.example.memic.transcription.domain.TranscriptionSentenceRepository;
import com.example.memic.transcription.infrastructure.WhisperApiClient;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RecognizedSentenceService {

    private final WhisperApiClient whisperApiClient;
    private final TranscriptionSentenceRepository transcriptionSentenceRepository;
    private final RecognizedSentenceRepository recognizedSentenceRepository;

    public RecognizedSentenceService(
            WhisperApiClient whisperApiClient,
            TranscriptionSentenceRepository transcriptionSentenceRepository,
            RecognizedSentenceRepository recognizedSentenceRepository
    ) {
        this.whisperApiClient = whisperApiClient;
        this.transcriptionSentenceRepository = transcriptionSentenceRepository;
        this.recognizedSentenceRepository = recognizedSentenceRepository;
    }

    @Transactional
    public RecognizedSentenceResponse transcribe(MultipartFile speechFile, RecognizedSentenceRequest request, Member speaker) {
        TranscriptionSentence transcriptionSentence = transcriptionSentenceRepository.getById(request.id());
        String transcribedSpeech = whisperApiClient.transcribeSpeech(speechFile);

        RecognizedSentence recognizedSentence = new RecognizedSentence(
                transcribedSpeech,
                transcriptionSentence,
                speaker
        );
        RecognizedSentence savedRecognizedSentence = recognizedSentenceRepository.save(recognizedSentence);

        return RecognizedSentenceResponse.of(savedRecognizedSentence, transcriptionSentence);
    }

    @Transactional(readOnly = true)
    public List<RecognizedSentenceCountResponse> getRecognizedSentenceCounts(Integer year, Month month, Member member) {
        Map<Integer, List<RecognizedSentence>> sentencesBySpokenMonth = recognizedSentenceRepository.findBySpeaker(member)
                                                                                                    .stream()
                                                                                                    .filter(sentence -> sentence.hasSpokenAt(year, month))
                                                                                                    .collect(Collectors.groupingBy(sentence -> sentence.getSpokenAt()
                                                                                                                                                       .getDayOfMonth()));
        return sentencesBySpokenMonth.entrySet()
                                     .stream()
                                     .map(entry -> new RecognizedSentenceCountResponse(entry.getKey(), entry.getValue().size()))
                                     .toList();
    }
}
