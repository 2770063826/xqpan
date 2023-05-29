package com.abc.xqpan.controller;

import com.abc.xqpan.entity.Result;
import com.abc.xqpan.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody User user){
        return Result.success("ok");
    }
}
