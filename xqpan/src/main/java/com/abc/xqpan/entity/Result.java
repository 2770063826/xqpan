package com.abc.xqpan.entity;

import lombok.Data;

@Data
public class Result<T> {

    // 状态码， 错误为201，正确为200
    private Integer code;
    // 错误信息，无错则无
    private String msg;
    // 正确后的返回数据
    private T data;

    public static <T> Result<T> success(T object){
        Result<T> r = new Result<T>();
        r.data = object;
        r.code = 200;
        return r;
    }

    public static <T> Result<T> error(String msg){
        Result<T> r = new Result<T>();
        r.msg = msg;
        r.code = 201;
        return r;
    }
}
