package com.zd.core.bean;

import com.zd.concast.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultBean<T> {

    private String code;
    private String msg;
    private T data;

    public ResultBean<T> response(ResponseCode responseCode) {
        this.setCode(responseCode.getCode());
        this.setMsg(responseCode.getMsg());
        return this;
    }

    public static <T> ResultBean<T> success() {
        return success(null);
    }

    public static <T> ResultBean<T> unauthorized() {
        return ResultBean.<T>builder().build().response(ResponseCode.UNAUTHORIZED);
    }

    public static <T> ResultBean<T> forbidden() {
        return ResultBean.<T>builder().build().response(ResponseCode.FORBIDDEN);
    }

    public static <T> ResultBean<T> success(T data) {
        return ResultBean.<T>builder().data(data).build().response(ResponseCode.SUCCESS);
    }
}
