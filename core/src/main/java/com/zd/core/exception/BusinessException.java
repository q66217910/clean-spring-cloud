package com.zd.core.exception;

import lombok.AllArgsConstructor;

import java.util.concurrent.CompletionException;

@AllArgsConstructor
public class BusinessException extends CompletionException {

    private String message;

}
