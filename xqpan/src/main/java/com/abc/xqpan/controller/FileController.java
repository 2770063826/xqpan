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
@Slf4j
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private RedisTemplate<String, JSONString> redisTemplate;

    @Resource
    private FileService fileService;

    @Value("${File.tempPath}")
    private String tempPath;

    @Value("${Chunk.size}")
    private String defaultChunkSize;

    /**
     * 上传文件
     * @param uploadForm
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(HttpServletRequest httpServletRequest,
                         UploadForm uploadForm){
        // 检查文件是否存在
        if(ObjectUtil.isNull(uploadForm.getFile())){
            log.error("上传错误，文件不存在");
            throw new MyException("上传错误，文件不存在");
        }
        // 查看临时目录是否存在文件，不存在就创建
        String fileResultPath = tempPath + File.separator + uploadForm.getFileUUId();
        FileUtils.createFile(fileResultPath);
        // 若是单文件上传
        if(uploadForm.getChunkTotal() == 1){
            String fileResultName = fileResultPath + File.separator + uploadForm.getFile().getOriginalFilename();
            if(!FileUtils.uploadOneFile(fileResultName, uploadForm.getFile())){
                return Result.error("文件上传失败");
            }
            return Result.success("上传成功");
        }
        // 分片上传
        String fileResultName = fileResultPath + File.separator + uploadForm.getChunkIndex();
        boolean flag = FileUtils.uploadByRandomAccessFile(fileResultName, uploadForm, Long.parseLong(defaultChunkSize));
        if (!flag) {
            return Result.error("文件上传失败");
        }
         // 保存分片上传信息
//        fileService.saveFileChunk(tempPath);
        // 检查是否为最后一个分片，若是最后一个就合并
        if(uploadForm.getChunkIndex() == uploadForm.getChunkTotal() - 1){

        }

        return Result.success("上传成功");
    }
}
