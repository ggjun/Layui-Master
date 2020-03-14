package com.fly.basic.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.fly.basic.api.FileService;
import com.fly.basic.api.ICommonDao;
import com.fly.basic.domain.FrameAttachInfo;
import com.fly.basic.domain.ReturnValue;
import com.fly.basic.util.ConfigUtil;
import com.fly.basic.util.ReturnCodeAndMsgEnum;
import com.fly.util.EpointDateUtil;
import com.fly.util.StringUtil;

@Service
public class FileServiceImp implements FileService
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
   
    @Override
    public ReturnValue uploadFile(MultipartFile zipFile, String cliengguid,  String cliengtag) {
        // TODO Auto-generated method stub
        String targetFilePath = ConfigUtil.getConfigValueByName("FileAddress");
        String randomname = UUID.randomUUID().toString().replace("-", "");
        String today = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
        
        String filename =  zipFile.getOriginalFilename();
        int unixSep = filename.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = filename.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if (pos != -1)  {
         // Any sort of path separator found...
            filename = filename.substring(pos + 1);
        }
        String url = targetFilePath + File.separator + today + File.separator + randomname + File.separator + filename;
        File targetFile = new File(url);
        //判断是否存在
        if (!targetFile.getParentFile().exists()) {
        //创建父目录文件
            targetFile.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        String data = null;
        try {
            fileOutputStream = new FileOutputStream(targetFile);
            int size = IOUtils.copy(zipFile.getInputStream(), fileOutputStream);
            //文件上传成功保存为frameattachinfo对象
            FrameAttachInfo attachinfo = new FrameAttachInfo();
            attachinfo.setAttachguid(UUID.randomUUID().toString());
            attachinfo.setAttachfilename(filename);
            attachinfo.setContenttype(filename.substring(filename.lastIndexOf('.')));
            attachinfo.setAttachlength(size);
            attachinfo.setCliengtag(cliengtag);
            attachinfo.setCliengguid(cliengguid);
            attachinfo.setFilepath(url.replaceAll("\\\\", "\\\\\\\\"));
            attachinfo.setUploaddatetime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            CommonDao.getInstance().insert(attachinfo);
           
            //保存attachguid返回前台
            data = attachinfo.getAttachguid();
        } catch (IOException e) {
            return new ReturnValue<>(-1, null);
        } finally {
           try {
               fileOutputStream.close();
            } catch (IOException e) {
                logger.error("文件上传报错！", e);
            }
        }
        return new ReturnValue<>(ReturnCodeAndMsgEnum.Success, data);

    }

    @Override
    public ReturnValue deleteFileByAttachguid(String attachguid) {
        // TODO Auto-generated method stub
        String sql = "select attachguid,filepath from frame_attachinfo where attachguid=?1";
        FrameAttachInfo attachinfo = CommonDao.getInstance().find(FrameAttachInfo.class, attachguid);
        if (attachinfo != null && StringUtil.isNotBlank(attachinfo.getFilepath())){
            //删除文件
            File deleteFile = new File(attachinfo.getFilepath());
            String parentpath = deleteFile.getParentFile().getPath();
            if (deleteFile.exists() && deleteFile.isFile()) {
                //删除文件
                deleteFile.delete();
                //删除文件夹
                File parentFile = new File(parentpath);
                parentFile.delete();
                CommonDao.getInstance().delete(attachinfo);
            }
            return new ReturnValue<>(ReturnCodeAndMsgEnum.Success, null);
        }
        else{
            return new ReturnValue<>(ReturnCodeAndMsgEnum.SYSTEM_ERROR, null);
        }
     
       
    }

    @Override
    public ReturnValue deleteFileByCliengguid(String cliengguid) {
        // TODO Auto-generated method stub
        String sql = "select attachguid,filepath from frame_attachinfo where cliengguid=?1";
        List<FrameAttachInfo> list = CommonDao.getInstance().findList(sql, FrameAttachInfo.class, cliengguid);
        if (! list.isEmpty()){
            for (FrameAttachInfo attachinfo : list){
                //删除文件
                File deleteFile = new File(attachinfo.getFilepath());
                String parentpath = deleteFile.getParentFile().getPath();
                if (deleteFile.exists() && deleteFile.isFile()) {
                    //删除文件
                    deleteFile.delete();
                    //删除文件夹
                    File parentFile = new File(parentpath);
                    parentFile.delete();
                    CommonDao.getInstance().delete(attachinfo);
                }
            }
            return new ReturnValue<>(ReturnCodeAndMsgEnum.Success, "1");//存在附件
        }
        return new ReturnValue<>(ReturnCodeAndMsgEnum.Success, "0");//不存在附件
    }

    @Override
    public ReturnValue getAttachguidsByCliengguid(String cliengguid) {
        // TODO Auto-generated method stub
        String sql = "select * from frame_attachinfo where cliengguid=?1 order by uploaddatetime";
        List<FrameAttachInfo> list = CommonDao.getInstance().findList(sql, FrameAttachInfo.class, cliengguid);
        if (! list.isEmpty()){
            JSONObject dataJson = new JSONObject();
            List<JSONObject> cliengguidlist = new ArrayList<JSONObject>();
            JSONObject obj = null;
            for (FrameAttachInfo attachinfo : list){
                 obj = new JSONObject();
                 obj.put("attachguid", attachinfo.getAttachguid());
                 obj.put("filename", attachinfo.getAttachfilename());
                 cliengguidlist.add(obj);
            }
            dataJson.put("list", cliengguidlist);
            return new ReturnValue<>(ReturnCodeAndMsgEnum.Success, dataJson);
        }
        return new ReturnValue<>(ReturnCodeAndMsgEnum.No_Data, null);//不存在附件
    }

    @Override
    public ReturnValue updateCliengguid(String picliengguid, String picsecond) {
        // TODO Auto-generated method stub
        String sql = "update frame_attachinfo set cliengguid=?1 where cliengguid=?2";
        CommonDao.getInstance().execute(sql, picsecond, picliengguid);
        return new ReturnValue<>(ReturnCodeAndMsgEnum.Success, null);
    }

}
