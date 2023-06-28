package com.abc.xqpan.controller;

import cn.hutool.json.JSONString;
import com.abc.xqpan.entity.FileChunkResult;
import com.abc.xqpan.entity.Result;
import com.abc.xqpan.entity.FileChunk;
import com.abc.xqpan.service.FileService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private RedisTemplate<String, JSONString> redisTemplate;

    @Resource
    private FileService fileService;

    /**
     * 上传文件
     * @param fileChunk
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(HttpServletRequest httpServletRequest,
                         FileChunk fileChunk){
        Boolean flag = fileService.upload(httpServletRequest, fileChunk);
        if(flag){
            return Result.success("上传成功");
        }
        else{
            return Result.error("上传出错");
        }
    }


    /**
     * 检查分片是否存在
     * @param httpServletRequest
     * @param fileChunk
     * @return
     */
    @GetMapping("/upload")
    public Result<FileChunkResult> checkChunkExit(HttpServletRequest httpServletRequest,
                                         FileChunk fileChunk){
        FileChunkResult fileChunkResult;
        try{
            fileChunkResult = fileService.checkChunkExist(fileChunk);
            return Result.success(fileChunkResult);
        } catch (Exception e){
            return Result.error(e.toString());
        }
    }

}
