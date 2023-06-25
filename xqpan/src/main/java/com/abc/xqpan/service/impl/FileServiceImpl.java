package com.abc.xqpan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.abc.xqpan.entity.Result;
import com.abc.xqpan.entity.UploadForm;
import com.abc.xqpan.service.FileService;
import com.abc.xqpan.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${File.tempPath}")
    private String tempPath;

    @Value("${Chunk.size}")
    private String defaultChunkSize;


    @Override
    public void save(String tempPath) {


    }


    @Override
    public Boolean upload(HttpServletRequest httpServletRequest, UploadForm uploadForm) {

        // 检查文件是否存在
        if(ObjectUtil.isNull(uploadForm.getFile())){
            log.error("上传错误，文件不存在");
            return false;
        }
        // 创建临时目录
        String fileResultPath = tempPath + File.separator + uploadForm.getFileUUId();
        FileUtils.createFile(fileResultPath);
        // 若是单文件上传
        if(uploadForm.getChunkTotal() == 1){
            String fileResultName = fileResultPath + File.separator + uploadForm.getFile().getOriginalFilename();
            if(!FileUtils.uploadOneFile(fileResultName, uploadForm.getFile())){
                return false;
            }
            return true;
        }
        // 分片上传
        String fileResultName = fileResultPath + File.separator + uploadForm.getChunkIndex();
        boolean flag = FileUtils.uploadByRandomAccessFile(fileResultName, uploadForm, Long.parseLong(defaultChunkSize));
        if (!flag) {
            return false;
        }
        // 保存分片上传信息
//        fileService.saveFileChunk(tempPath);
        // 检查是否为最后一个分片，若是最后一个就合并
        if(uploadForm.getChunkIndex() == uploadForm.getChunkTotal() - 1){

        }

        return true;
    }
}
