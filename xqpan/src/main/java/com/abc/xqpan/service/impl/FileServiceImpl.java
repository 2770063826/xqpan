package com.abc.xqpan.service.impl;

import com.abc.xqpan.common.ResponseMsg;
import com.abc.xqpan.common.exceptor.MyException;
import com.abc.xqpan.entity.FileChunk;
import com.abc.xqpan.entity.FileChunkResult;
import com.abc.xqpan.service.FileService;
import com.abc.xqpan.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Set;


@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private RedisTemplate<String, JSObject> redisTemplate;

    @Transactional
    @Override
    public Boolean upload(HttpServletRequest httpServletRequest, FileChunk fileChunk) throws MyException{

        // 创建文件的临时文件夹
        String fileChunkTempPath = FileUtils.getChunkFileFolderPath(fileChunk.getIdentifier());
        File fileChunkTemp = new File(fileChunkTempPath);
        if (!fileChunkTemp.exists()) {
            boolean mkdirs = fileChunkTemp.mkdirs();
            if (!mkdirs) {
                log.error(ResponseMsg.CREATE_TEMP_FILE_FAILED);
                return false;
            }
        }
        //写入分片
        try {
            // 获取文件输入流
            InputStream inputStream = fileChunk.getFile().getInputStream();
            // 创建分片文件夹
            FileOutputStream outputStream = new FileOutputStream(new File(fileChunkTempPath + fileChunk.getChunkNumber()));
            // 将输入流的内容复制到文件夹中
            IOUtils.copy(inputStream, outputStream);
            // 将该分片写入redis
            Integer nowSize = FileUtils.saveRedis(fileChunk);
            // 当分片是最后一片时，合并分片
            if (nowSize.equals(fileChunk.getTotalChunks())) {
                File mergeFile = FileUtils.mergeChunks(fileChunk.getIdentifier(), fileChunk.getFilename());
                if (mergeFile == null) {
                    throw new MyException(ResponseMsg.MERGE_FILE_FAILED);
                }
            }
        } catch (Exception e){
            throw new MyException(e.getMessage());
        }
        return true;
    }

    @Override
    public FileChunkResult checkChunkExist(FileChunk fileChunk) throws MyException{
        //1.检查文件是否已上传过
        //1.1)检查在磁盘中是否存在
        String fileFolderPath = FileUtils.getFileFolderPath(fileChunk.getIdentifier());
        String filePath = FileUtils.getFilePath(fileChunk.getIdentifier(), fileChunk.getFilename());
        File file = new File(filePath);
        boolean exists = file.exists();
        //1.2)检查Redis中是否存在,并且所有分片已经上传完成。
        Set<Integer> uploaded = (Set<Integer>) redisTemplate.opsForHash().get(fileChunk.getIdentifier(), "uploaded");
        if (uploaded != null && uploaded.size() == fileChunk.getTotalChunks() && exists) {
            return new FileChunkResult(true);
        }
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            boolean mkdirs = fileFolder.mkdirs();
            log.info("准备工作,创建文件夹,fileFolderPath:{},mkdirs:{}", fileFolderPath, mkdirs);
        }
        return new FileChunkResult(false, uploaded);
    }
}
