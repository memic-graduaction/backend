package com.example.memic.common.exception.sender;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = "!prod")
@Component
public class DevInternalServerErrorMessageConverter implements InternalServerErrorMessageConverter {

    @Override
    public String convert(Exception e) {
        return e.getClass().getSimpleName() + ": " + e.getLocalizedMessage();
    }
}
