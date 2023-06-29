package com.abc.xqpan.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileChunk {

    // 文件Id
    private Long id;

    // 文件父ID
    private Long pId;

    // 分块文件
    private MultipartFile file;

    // 文件md5
    private String identifier;

    // 当前分片序号
    private Integer chunkNumber;

    // 当前分片大小
    private Long currentChunkSize;

    // 文件总大小
    private Long totalSize;

    // 设定的每个每块大小
    private Long chunkSize;

    // 分块总数
    private Integer totalChunks;

    // 文件上传名
    private String filename;

    // 上传者ID
    private Long userId;

    // 文件访问路径
    private String fileUrl;

    // 文件类型
    private Integer fileType;

    // 状态
    private Integer status;
}
