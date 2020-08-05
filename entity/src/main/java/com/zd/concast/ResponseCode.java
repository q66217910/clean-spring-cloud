package com.zd.concast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS("0", "SUCCESS"),
    UNAUTHORIZED(String.valueOf(HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED.getReasonPhrase()),
    FORBIDDEN(String.valueOf(HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN.getReasonPhrase());

    private String code;
    private String msg;
}
