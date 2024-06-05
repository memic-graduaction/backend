package com.example.memic.common.exception.sender;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
public class ProdInternalServerErrorMessageConverter implements InternalServerErrorMessageConverter {

    private final SlackProvider slackProvider;

    public ProdInternalServerErrorMessageConverter(final SlackProvider slackProvider) {
        this.slackProvider = slackProvider;
    }

    @Override
    public String convert(Exception e) {
        slackProvider.sendErrorMessageToChannel(e);
        return "서버 에러 발생";
    }
}
