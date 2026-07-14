package com.harshit.razorpay.common.exception;

import lombok.Getter;

@Getter
public class BusinessRuleViolatationException extends RuntimeException {
    private final String errorCode;

    public BusinessRuleViolatationException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
