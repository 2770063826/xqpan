package com.abc.xqpan.controller;

import com.abc.xqpan.common.ResponseMsg;
import com.abc.xqpan.entity.Result;
import com.abc.xqpan.entity.User;
import com.abc.xqpan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody User user){
        String flag = userService.login(user);
        if(!flag.equals(ResponseMsg.LOGIN_WRONG)){
            return Result.success(flag);
        }
        else{
            return Result.error(ResponseMsg.LOGIN_ERROR);
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
        Boolean flag = userService.logout();
        if(flag){
            return Result.success(ResponseMsg.LOGOUT_SUCCESS);
        }
        else{
            return Result.error(ResponseMsg.LOGOUT_WRONG);
        }
    }

}
