package com.abc.xqpan.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONString;
import com.abc.xqpan.common.exceptor.MyException;
import com.abc.xqpan.entity.Result;
import com.abc.xqpan.entity.UploadForm;
import com.abc.xqpan.service.FileService;
import com.abc.xqpan.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private RedisTemplate<String, JSONString> redisTemplate;

    @Resource
    private FileService fileService;

    /**
     * 上传文件
     * @param uploadForm
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(HttpServletRequest httpServletRequest,
                         UploadForm uploadForm){
        Boolean flag = fileService.upload(httpServletRequest, uploadForm);
        if(flag){
            return Result.success("上传成功");
        }
        else{
            return Result.error("上传出错");
        }
    }
}
