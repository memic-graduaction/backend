package com.example.memic.transcription.service;

import com.example.memic.transcription.exception.Mp4ExtractException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class YoutubeMp4Extractor {

    private final String outputFormat;
    private final String command;

    public YoutubeMp4Extractor(
            @Value("${extractor.outputFormat}") String outputFormat,
            @Value("${extractor.command}") String command
    ) {
        this.outputFormat = outputFormat;
        this.command = command;
    }

    public String extractVideo(String url) {
        String extension = "mp3";
        String filePath = outputFormat + UUID.randomUUID().toString() + "." + extension;

        String[] commandsAndArgs = {
                command,
                "--extract-audio",
                "--audio-format", extension,
                "--audio-quality", "0",
                "--output", filePath,
                url
        };

        ProcessBuilder processBuilder = new ProcessBuilder(commandsAndArgs);
        try {
            Process process = processBuilder.start();
            printProgressResult(process);
            return filePath;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new Mp4ExtractException("오디오를 추출하는 과정에서 에러가 발생했습니다.");
        }
    }

    private void printProgressResult(Process process) throws IOException {
        StringBuilder lines = new StringBuilder();

        extractStreams(process.getInputStream(), lines);

        lines.append("----error----").append(System.lineSeparator());
        extractStreams(process.getErrorStream(), lines);

        lines.append("----exit code----").append(System.lineSeparator());
        try {
            int exitCode = process.waitFor();
            lines.append(exitCode);
            validateExitCode(exitCode);
        } catch (InterruptedException e) {
            lines.append(e.getMessage());
            throw new Mp4ExtractException("오디오를 추출하는 과정에서 에러가 발생했습니다.");
        }
        System.out.println(lines.toString());
    }

    public void extractStreams(InputStream inputStream, StringBuilder lines) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.append(line);
            }
        }
    }

    private void validateExitCode(int exitCode) {
        if (exitCode != 0) {
            throw new Mp4ExtractException("오디오를 추출하는 과정에서 에러가 발생했습니다.");
        }
    }
}
