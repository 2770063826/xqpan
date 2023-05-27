package com.abc.xqpan.utils;

import com.abc.xqpan.common.exceptor.MyException;
import com.abc.xqpan.entity.UploadForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Slf4j
public class FileUtils {

    /**
     * 创建文件
     * @param fileResultPath
     */
    public static void createFile(String fileResultPath){
        File savePath = new File(fileResultPath);
        if (!savePath.exists()) {
            boolean flag = savePath.mkdirs();
            if (!flag) {
                log.error("保存目录创建失败");
                throw new MyException("保存目录创建失败");
            }
        }
    }


    /**
     * 上传单个文件
     * @param resultFileName
     * @param file
     * @return
     */
    public static Boolean uploadOneFile(String resultFileName, MultipartFile file){
        File saveFile = new File(resultFileName);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            log.error("文件上传失败：" + e);
            return false;
        }
        return true;
    }

    /**
     * 分片上传大文件
     * @param resultFileName
     * @param uploadForm
     * @param defaultChunkSize
     * @return
     */
    public static Boolean uploadByRandomAccessFile(String resultFileName, UploadForm uploadForm, Long defaultChunkSize){
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resultFileName, "rw")) {
            // 分片大小必须和前端匹配，否则上传会导致文件损坏
            long chunkSize = uploadForm.getFile().getSize() == 0L ? defaultChunkSize : uploadForm.getFile().getSize();
            // 偏移量
            long offset = chunkSize * (uploadForm.getChunkIndex() - 1);
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            // 写入
            randomAccessFile.write(uploadForm.getFile().getBytes());
        } catch (IOException e) {
            log.error("文件上传失败：" + e);
            return false;
        }
        return true;
    }
}
