package com.abc.xqpan.controller;

import com.abc.xqpan.common.ResponseError;
import com.abc.xqpan.entity.Result;
import com.abc.xqpan.entity.User;
import com.abc.xqpan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody User user){
        String flag = userService.login(user);
        if(!flag.equals(ResponseError.LOGIN_WRONG)){
            return Result.success(flag);
        }
        else{
            return Result.error(ResponseError.LOGIN_ERROR);
        }
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user){
        return Result.success("ok");
    }


    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public Result<String> register(){
        String flag = userService.logout();
        if(!flag.equals(ResponseError.LOGOUT_WRONG)){
            return Result.success(flag);
        }
        else{
            return Result.error(ResponseError.LOGOUT_ERROR);
        }
    }

}
