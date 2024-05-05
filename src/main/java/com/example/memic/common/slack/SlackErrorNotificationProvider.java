package com.example.memic.common.slack;


import com.slack.api.model.block.LayoutBlock;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SlackErrorNotificationProvider {

    private final static int MAX_ERROR_LENGTH = 500;
    private final SlackHelper slackHelper;
    private final String channelId;

    public SlackErrorNotificationProvider(
            final SlackHelper slackHelper,
            @Value("${slack.webhook.id}") final String channelId
    ) {
        this.slackHelper = slackHelper;
        this.channelId = channelId;
    }

    public String getErrorStack(Throwable throwable) {
        final String exceptionAsString = Arrays.toString(throwable.getStackTrace());
        final int cutLength = Math.min(exceptionAsString.length(), MAX_ERROR_LENGTH);
        return exceptionAsString.substring(0, cutLength);
    }

    @Async
    public void sendNotification(List<LayoutBlock> layoutBlocks) {
        slackHelper.sendNotification(channelId, layoutBlocks);
    }
}
