package com.example.memic.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.stereotype.Service;

@Service
public class YoutubeMp4ExtractService {

    public void extractVideo(String url) throws Exception {
        String outputFormat = "/Users/jung-yunho/youtube-mp3/%(id)s.mp3";
        String[] command = {"yt-dlp", "--extract-audio", "--audio-format", "mp3",
                "--audio-quality", "0", "--output", outputFormat, url};

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
        }

        int exitCode = process.waitFor();
        System.out.println("Exited with code: " + exitCode);
    }
}
