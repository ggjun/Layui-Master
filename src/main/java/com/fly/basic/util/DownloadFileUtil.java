package com.fly.basic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class DownloadFileUtil
{
    public static final String separator = File.separator;
    /**
     * 下载样表
     * @param filePath 文件上级目录
     * @param fileName 文件名
     * @param newName  下载的展示文件名
     * @return 响应
     */
    public static ResponseEntity<InputStreamResource> download(String path, String fileName, String contenttype) {
        
        ResponseEntity<InputStreamResource> response = null;
        try {
           /* ClassPathResource classPathResource = new ClassPathResource(path);
            InputStream inputStream = classPathResource.getInputStream();*/
            File file = new File(path);//springboot会把文件打成jar包，所以这样的路径会带有!,一般找不到路径
            InputStream inputStream =  new FileInputStream(file);
            fileName = fileName.substring(0,fileName.lastIndexOf('.'));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition",
                    "attachment; filename="
                            + new String(fileName.getBytes("gbk"), "iso8859-1") + contenttype);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            InputStreamResource resource = new InputStreamResource(inputStream);
            response = ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
