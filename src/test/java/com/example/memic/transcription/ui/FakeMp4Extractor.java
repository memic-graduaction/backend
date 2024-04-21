package com.example.memic.transcription.ui;

import com.example.memic.transcription.infrastructure.Mp4Extractor;
import java.io.File;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class FakeMp4Extractor implements Mp4Extractor {

    @Override
    public String extractVideo(String url) {
        String resourceName = "speech.mp3";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        return file.getAbsolutePath();
    }
}
