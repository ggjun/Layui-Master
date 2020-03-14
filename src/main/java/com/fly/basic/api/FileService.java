package com.fly.basic.api;

import org.springframework.web.multipart.MultipartFile;

import com.fly.basic.domain.ReturnValue;

public interface FileService
{
    public ReturnValue uploadFile(MultipartFile zipFile, String cliengguid, String cliengtag);

    public ReturnValue deleteFileByAttachguid(String attachguid);

    public ReturnValue deleteFileByCliengguid(String cliengguid);

    public ReturnValue getAttachguidsByCliengguid(String cliengguid);

    public ReturnValue updateCliengguid(String picliengguid, String picsecond);
}
