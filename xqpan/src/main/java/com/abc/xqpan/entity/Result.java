package com.abc.xqpan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    // 是否返回正确
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
