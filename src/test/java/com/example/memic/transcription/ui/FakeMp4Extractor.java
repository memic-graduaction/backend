package com.example.memic.transcription.ui;

import com.example.memic.transcription.infrastructure.Mp4Extractor;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("!prod && !dev")
public class FakeMp4Extractor implements Mp4Extractor {

    @Override
    public String extractVideo(String url) {

        String resourceName = "speech.mp3";
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceURL = classLoader.getResource(resourceName);
        String filePath = URLDecoder.decode(resourceURL.getFile(), StandardCharsets.UTF_8);
        File file = new File(filePath);
        return file.getAbsolutePath();
    }
}
