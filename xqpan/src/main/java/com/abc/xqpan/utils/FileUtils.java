package com.abc.xqpan.utils;

import com.abc.xqpan.common.MyConstants;
import com.abc.xqpan.common.ResponseMsg;
import com.abc.xqpan.common.exceptor.MyException;
import com.abc.xqpan.entity.FileChunk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.*;


@Component
@Slf4j
public class FileUtils {


    /**
     * 在工具类中注入
     */
    @Autowired
    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        FileUtils.redisTemplate = redisTemplate;
    }


    private static final String tempPath = "D:\\Springboot\\Springboot项目\\小青云盘\\xqpan\\src\\main\\resources\\temp\\";

    /**
     * 合并分片
     * @param identifier
     * @param filename
     * @return
     */
    public static File mergeChunks(String identifier, String filename) throws MyException{
        // 获取分块所在的目录
        String chunkFileFolderPath = getChunkFileFolderPath(identifier);
        // 获取文件的绝对路径
        String filePath = getFilePath(identifier, filename);
        File chunkFileFolder = new File(chunkFileFolderPath);
        File mergeFile = new File(filePath);
        File[] chunks = chunkFileFolder.listFiles();
        // 排序
        Arrays.stream(chunks).sorted(Comparator.comparing(item -> Integer.valueOf(item.getName())));
        try {
            // 随机输入/输出流
            RandomAccessFile randomAccessFileWriter = new RandomAccessFile(mergeFile, "rw");
            byte[] bytes = new byte[1024];
            for (File chunk : chunks) {
                RandomAccessFile randomAccessFileReader = new RandomAccessFile(chunk, "r");
                int len;
                while ((len = randomAccessFileReader.read(bytes)) != -1) {
                    randomAccessFileWriter.write(bytes, 0, len);
                }
                randomAccessFileReader.close();
            }
            randomAccessFileWriter.close();
        } catch (Exception e) {
            throw new MyException(ResponseMsg.MERGE_FILE_FAILED);
        }
        return mergeFile;
    }


    /**
     * 得到分块文件所属的目录
     * @param identifier
     * @return
     */
    public static String getChunkFileFolderPath(String identifier){
        return getFileFolderPath(identifier) + "chunks" + File.separator;
    }


    /**
     * 得到文件所属的目录
     * @param identifier
     * @return
     */
    public static String getFileFolderPath(String identifier) {
        return tempPath + identifier.substring(0, 1) + File.separator +
                identifier.substring(1, 2) + File.separator +
                identifier + File.separator;
    }


    /**
     * 在redis中保存分片并返回分片序号
     * @param fileChunk
     * @return
     */
    public static Integer saveRedis(FileChunk fileChunk) {
        Set<Integer> uploaded = (Set<Integer>) redisTemplate.opsForHash().get(MyConstants.UPLOAD_FILE + fileChunk.getIdentifier(), "uploaded");
        if (uploaded == null) {
            uploaded = new HashSet<>(Arrays.asList(fileChunk.getChunkNumber()));
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("uploaded", uploaded);
            objectObjectHashMap.put("totalChunks", fileChunk.getTotalChunks());
            objectObjectHashMap.put("totalSize", fileChunk.getTotalSize());
            objectObjectHashMap.put("path", getFileRelativelyPath(fileChunk.getIdentifier(), fileChunk.getFilename()));
            redisTemplate.opsForHash().putAll(MyConstants.UPLOAD_FILE + fileChunk.getIdentifier(), objectObjectHashMap);
        } else {
            uploaded.add(fileChunk.getChunkNumber());
            redisTemplate.opsForHash().put(MyConstants.UPLOAD_FILE + fileChunk.getIdentifier(), "uploaded", uploaded);
        }

        return uploaded.size();
    }


    /**
     * 获取文件的相对路径
     * @param identifier
     * @param filename
     * @return
     */
    public static String getFileRelativelyPath(String identifier, String filename) {
        // 去掉文件名后缀
        String ext = filename.substring(filename.lastIndexOf("."));
        return "/" + identifier.substring(0, 1) + "/" +
                identifier.substring(1, 2) + "/" +
                identifier + "/" + identifier
                + ext;
    }


    /**
     * 得到文件的绝对路径
     * @param identifier
     * @param filename
     * @return
     */
    public static String getFilePath(String identifier, String filename) {
        String ext = filename.substring(filename.lastIndexOf("."));
        return getFileFolderPath(identifier) + identifier + ext;
    }


}
