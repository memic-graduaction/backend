package com.example.memic.transcription.service;

import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.domain.TranscriptionRepository;
import com.example.memic.transcription.dto.TranscriptionCreateRequest;
import com.example.memic.transcription.dto.TranscriptionResponse;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TranscriptionService {

    private final YoutubeMp4Extractor extractor;
    private final TranscriptionRepository transcriptionRepository;
    private final String openAiApiUrl;
    private final String apiKey;

    public TranscriptionService(
            @Value("${transcription.url}") String openAiApiUrl,
            @Value("${transcription.apiKey}") String apiKey,
            YoutubeMp4Extractor extractor,
            TranscriptionRepository transcriptionRepository
    ) {
        this.openAiApiUrl = openAiApiUrl;
        this.apiKey = apiKey;
        this.extractor = extractor;
        this.transcriptionRepository = transcriptionRepository;
    }

    @Transactional
    public TranscriptionResponse transcribe(TranscriptionCreateRequest request) {

        String filePath = extractor.extractVideo(request.url());

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MultiValueMap<String, Object>> requestEntity = creatRequestEntity(filePath);
        // 비디오 추출  -> 비디오 전송 -> 스크립트 반환을 하면서 디비에 저장(스크립트를 파싱해서 보여줘야하기때문에 비동기전송은 어려워보임)
        // 스프링에서 가능한건 요청을 보내고, 응답이 오기 전까지 쓰레드를 블락하는 게 아니고, 다른 행동을 할 수 있다.
        ResponseEntity<String> response = restTemplate.postForEntity(openAiApiUrl, requestEntity, String.class);

        String output = response.getBody().toString();
        String parsed = processTranscription(output);
        Map<LocalTime, String> sentences = parseLogText(parsed);

        Transcription transcription = new Transcription(request.url(), sentences);
        transcriptionRepository.save(transcription);
        
        return TranscriptionResponse.fromEntity(transcription);
    }


    public Map<LocalTime, String> parseLogText(String text) {
        Map<LocalTime, String> logMap = new HashMap<>();
        String[] lines = text.split("\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm:ss");

        for (String line : lines) {
            String[] parts = line.split("]", 2);
            if (parts.length == 2) {
                String timePart = parts[0].substring(1);
                LocalTime time = LocalTime.parse(timePart, formatter);
                String message = parts[1].trim();
                logMap.put(time, message);
            }
        }
        return logMap;
    }

    public String processTranscription(String transcription) {
        String[] blocks = transcription.split("\n\n");
        List<String> processedLines = new ArrayList<>();

        for (String block : blocks) {
            String[] lines = block.split("\n");
            if (lines.length >= 3) {
                String timeRange = lines[1];
                String text = lines[2];
                String startTime = timeRange.split(" --> ")[0];
                String formattedStartTime = formatTime(startTime);
                String processedLine = "[" + formattedStartTime + "]" + text;
                processedLines.add(processedLine);
            }
        }

        return String.join("\n", processedLines);
    }

    private String formatTime(String time) {
        return time.replaceFirst("^0", "").replaceAll(",\\d+$", "");
    }

    private HttpEntity<MultiValueMap<String, Object>> creatRequestEntity(String filePath) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(filePath));
        body.add("model", "whisper-1");
        body.add("response_format", "srt");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return requestEntity;
    }

    @Transactional(readOnly = true)
    public TranscriptionResponse getTranscription(Long id) {
        Transcription transcription = transcriptionRepository.getById(id);
        return TranscriptionResponse.fromEntity(transcription);
    }
}
