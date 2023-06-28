package com.abc.xqpan.entity;

import lombok.Data;

@Data
public class Result<T> {

    // 状态码，错误为201，正确为200
    private Boolean success;
    // 错误信息，无错则无
    private String message;
    // 正确后的返回数据
    private T data;

    public static <T> Result<T> success(T object){
        Result<T> r = new Result<T>();
        r.data = object;
        r.success = true;
        return r;
    }

    public static <T> Result<T> error(String msg){
        Result<T> r = new Result<T>();
        r.message = msg;
        r.success = false;
        return r;
    }
}
