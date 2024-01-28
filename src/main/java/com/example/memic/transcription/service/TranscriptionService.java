package com.example.memic.transcription.service;

import com.example.memic.transcription.ui.TranscriptionCreateRequest;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TranscriptionService {

    private final YoutubeMp4Extractor extractor;
    private final String openAiApiUrl;
    private final String apiKey;

    public TranscriptionService(
            @Value("transcription.url") String openAiApiUrl,
            @Value("transcription.apiKey") String apiKey,
            YoutubeMp4Extractor extractor
    ) {
        this.openAiApiUrl = openAiApiUrl;
        this.apiKey = apiKey;
        this.extractor = extractor;
    }

    public ResponseEntity<String> transcribe(TranscriptionCreateRequest request) {

        String filePath = extractor.extractVideo(request.url());

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MultiValueMap<String, Object>> requestEntity = creatRequestEntity(filePath);

        ResponseEntity<String> response = restTemplate.postForEntity(openAiApiUrl, requestEntity, String.class);

        String output = response.getBody().toString();
        String parsed = processTranscription(output);
        String outputText = "output.txt";
        saveStringToFile(parsed, outputText);

        return ResponseEntity.ok(parsed);
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


    public static String processTranscription(String transcription) {
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

    private static String formatTime(String time) {
        return time.replaceFirst("^0", "").replaceAll(",\\d+$", "");
    }



    public void saveStringToFile(String content, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
