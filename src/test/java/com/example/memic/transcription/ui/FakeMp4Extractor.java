package com.example.memic.transcription.ui;

import com.example.memic.transcription.infrastructure.Mp4Extractor;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile({"local", "test"})
public class FakeMp4Extractor implements Mp4Extractor {

    @Override
    public String extractVideo(String url) {

        String resourceName = "speech.mp3";
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceURL = classLoader.getResource(resourceName);
        try {
            String filePath = URLDecoder.decode(resourceURL.getFile(), StandardCharsets.UTF_8.toString());
            File file = new File(filePath);
            return file.getAbsolutePath();
        }  catch (UnsupportedEncodingException e) {
            throw new UriDecodeException("URI 디코딩에 실패했습니다");
        }
    }
}
