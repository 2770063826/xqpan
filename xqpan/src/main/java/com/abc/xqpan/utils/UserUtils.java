package com.abc.xqpan.utils;

import javax.servlet.http.HttpServletRequest;

public class UserUtils {

    /**
     * 获取用户ID
     * @param httpServletRequest
     * @return
     */
    public static Long getUserId(HttpServletRequest httpServletRequest){
        return Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
    }
}
