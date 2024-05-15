package com.example.memic.transcription.dto;

public record TranscriptionCreateRequest(
        String url
) {

    private static final String SHARING_URL_PARAM = "?si";

    @Override
    public String url() {
        String urlTrimmed =  url.trim();
        if (urlTrimmed.contains(SHARING_URL_PARAM)) {
            return urlTrimmed.substring(0, urlTrimmed.indexOf("?"));
        }
        return urlTrimmed;
    }
}
