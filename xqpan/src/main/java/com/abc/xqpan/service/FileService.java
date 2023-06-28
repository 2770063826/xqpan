package com.abc.xqpan.service;

import com.abc.xqpan.entity.FileChunk;
import com.abc.xqpan.entity.FileChunkResult;

import javax.servlet.http.HttpServletRequest;

public interface FileService {

    Boolean upload(HttpServletRequest httpServletRequest, FileChunk fileChunk);

    FileChunkResult checkChunkExist(FileChunk fileChunk);
}
