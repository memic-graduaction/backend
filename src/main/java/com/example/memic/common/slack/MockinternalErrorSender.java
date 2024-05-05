package com.example.memic.common.slack;

import java.io.IOException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Profile("!prod")
@Component
public class MockinternalErrorSender implements InternalErrorSender{


    @Override
    public void execute(final ContentCachingRequestWrapper cachingRequest, final Exception e) throws IOException {
    }
}
