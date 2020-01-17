package com.zd.concast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS("0", "SUCCESS");

    private String code;
    private String msg;
}
