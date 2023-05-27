package com.abc.xqpan.common.exceptor;

import com.abc.xqpan.entity.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 * @ControllerAdvice配合@ExceptionHandler用于捕获Controller中抛出的指定类型的异常，从而达到不同类型的异常区别处理的目的。
 */

@ResponseBody
@ControllerAdvice
//@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {

    // @ExceptionHandler(xxx.class)：表示需要捕获的异常类名
    @ExceptionHandler(MyException.class)
    public Result<String> MyExceptionHandler(MyException e){
        return Result.error(e.getMessage());
    }


}