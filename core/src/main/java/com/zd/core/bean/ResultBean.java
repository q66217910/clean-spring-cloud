package com.zd.core.bean;

import lombok.Data;

@Data
public class ResultBean<T> {

    private String code;
    private String msg;
    private T data;

}
