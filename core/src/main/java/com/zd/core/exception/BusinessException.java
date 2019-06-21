package com.zd.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.CompletionException;

@Data
@AllArgsConstructor
public class BusinessException extends CompletionException {

    private String message;

}
