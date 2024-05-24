package com.example.memic.common.exception.sender;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("prod")
public class SlackProvider {

    private static final String PROD_ERROR_MESSAGE_TITLE = "🤯 *500 에러 발생*";
    private static final String ATTACHMENTS_ERROR_COLOR = "#eb4034";
    private final String token;
    private final String channelProductError;
    private final SlackMessageConverter slackMessageConverter;

    public SlackProvider(
            final @Value(value = "${slack.token}") String token,
            final @Value(value = "${slack.channel.monitor}") String channelProductError,
            final SlackMessageConverter slackMessageConverter
    ) {
        this.token = token;
        this.channelProductError = channelProductError;
        this.slackMessageConverter = slackMessageConverter;
    }

    public void sendErrorMessageToChannel(Exception exception) {
        try {
            List<LayoutBlock> layoutBlocks = slackMessageConverter.createProdErrorMessage(exception);
            List<Attachment> attachments = slackMessageConverter.createAttachments(ATTACHMENTS_ERROR_COLOR,
                    layoutBlocks);
            Slack.getInstance().methods(token).chatPostMessage(request ->
                    request.channel(channelProductError)
                           .attachments(attachments)
                           .text(PROD_ERROR_MESSAGE_TITLE));
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
