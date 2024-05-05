package com.example.memic.common.slack;

import java.io.IOException;
import org.springframework.web.util.ContentCachingRequestWrapper;

public interface InternalErrorSender {

    void execute(ContentCachingRequestWrapper cachingRequest, Exception e) throws IOException;
}
