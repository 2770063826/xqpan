package com.abc.xqpan.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadForm {

    private MultipartFile file;

    private String fileUUId;

    private Long filePId;

    private String fileName;

    private String fileMd5;

    private String fileType;

    // 目前是第几个分片
    private int chunkIndex;

    // 总共多少个分片
    private int chunkTotal;

    // 分片大小
    private Float chunkSize;

    // 当前分片大小
    private Float currentChunkSize;

}
