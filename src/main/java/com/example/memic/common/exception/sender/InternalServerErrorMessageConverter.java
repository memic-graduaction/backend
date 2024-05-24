package com.example.memic.common.exception.sender;

public interface InternalServerErrorMessageConverter {

    String convert(Exception e);
}
