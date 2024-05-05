package com.example.memic.common.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.LayoutBlock;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SlackHelper {
    private final MethodsClient methodsClient;

    public SlackHelper(
            @Value("${slack.apiKey}") final String apiKey
    ) {
        this.methodsClient = Slack.getInstance().methods(apiKey);
    }

    public void sendNotification(final String channelId, final List<LayoutBlock> layoutBlocks) {
        ChatPostMessageRequest chatPostMessageRequest =
                ChatPostMessageRequest.builder()
                                      .channel(channelId)
                                      .text("######## text message#######")
                                      .blocks(layoutBlocks)
                                      .build();
        try {
            methodsClient.chatPostMessage(chatPostMessageRequest);
        } catch (SlackApiException | IOException e) {
            log.error(e.toString());
        }
    }
}