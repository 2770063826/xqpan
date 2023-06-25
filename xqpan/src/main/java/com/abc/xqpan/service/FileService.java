package com.abc.xqpan.service;

import com.abc.xqpan.entity.UploadForm;

import javax.servlet.http.HttpServletRequest;

public interface FileService {
    void save(String tempPath);

    Boolean upload(HttpServletRequest httpServletRequest, UploadForm uploadForm);
}
