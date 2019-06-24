package com.zd.core.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultBean<T> {

    private String code;
    private String msg;
    private T data;

}
