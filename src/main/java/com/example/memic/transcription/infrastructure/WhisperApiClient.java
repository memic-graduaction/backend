package com.example.memic.transcription.infrastructure;

import com.example.memic.speech.domain.Speech;
import com.example.memic.transcription.domain.Transcription;
import com.example.memic.transcription.exception.ImageToByteException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
public class WhisperApiClient {

    private final RestTemplate restTemplate;

    public WhisperApiClient(
            @Value("${transcription.url}") String openApiUrl,
            @Value("${transcription.apiKey}") String apiKey
    ) {
        restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(openApiUrl))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public Speech transcribeSpeech(MultipartFile file) {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = createRequestSpeechEntity(file);
        ResponseEntity<String> response = restTemplate.postForEntity("", requestEntity, String.class);

        String output = response.getBody().toString();
        String parsed = processSpeech(output);
        return new Speech(parsed);
    }

    private HttpEntity<MultiValueMap<String, Object>> createRequestSpeechEntity(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            Resource fileResource = new InputStreamResource(file.getInputStream()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }

                @Override
                public long contentLength() {
                    return file.getSize(); 
                }
            };

            body.add("file", fileResource);
            body.add("model", "whisper-1");
            body.add("response_format", "json");
        } catch (IOException e) {
            throw new ImageToByteException(e);
        }

        return new HttpEntity<>(body, headers);
    }

    private String processSpeech(String transcription) {
        int transcriptionLength = transcription.length();
        String parsed = transcription.substring(13, transcriptionLength - 3);
        System.out.println(parsed);

        return parsed;
    }

    public Transcription transcribe(String url, String filePath) {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = creatRequestEntity(filePath);
        ResponseEntity<String> response = restTemplate.postForEntity("", requestEntity, String.class);

        String output = response.getBody().toString();
        String parsed = processTranscription(output);
        Map<LocalTime, String> sentences = parseLogText(parsed);

        return new Transcription(url, sentences);
    }

    private HttpEntity<MultiValueMap<String, Object>> creatRequestEntity(String filePath) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(filePath));
        body.add("model", "whisper-1");
        body.add("response_format", "srt");

        return new HttpEntity<>(body);
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
        return time.replaceAll(",\\d+$", "");
    }

    public Map<LocalTime, String> parseLogText(String text) {
        Map<LocalTime, String> logMap = new HashMap<>();
        String[] lines = text.split("\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
}
