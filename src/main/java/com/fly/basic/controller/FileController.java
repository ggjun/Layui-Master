package com.fly.basic.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.fly.basic.api.FileService;
import com.fly.basic.api.ICommonDao;
import com.fly.basic.domain.FrameAttachInfo;
import com.fly.basic.domain.ReturnValue;
import com.fly.basic.impl.CommonDao;
import com.fly.basic.util.DownloadFileUtil;
import com.fly.util.StringUtil;

/**
 * @Title: LoginController
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 11:39
 */
@RestController
@RequestMapping("filecontroller")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FileService fileService;

    /**
     * 上传图片
     * @param IdForm
     * @return RestResult<Object>
     */
    @RequestMapping("/upload")
    public ReturnValue uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("cliengguid") String cliengguid, @RequestParam("cliengtag") String cliengtag) {
        return fileService.uploadFile(file, cliengguid, cliengtag);
    }
    
    
    @RequestMapping("/uploadinedit")
    public ReturnValue Uploadinedit(@RequestParam("file") MultipartFile file, @RequestParam("cliengguid") String cliengguid, @RequestParam("cliengtag") String cliengtag) {
        ReturnValue rvalue = fileService.uploadFile(file, cliengguid, cliengtag);
        JSONObject data = new JSONObject();
        if (rvalue.getCode() == 1){
            rvalue.setCode(0);
            data.put("src", "http://localhost:8102/filecontroller/readAttach?attachguid=" + rvalue.getData().toString());
            data.put("title", "图片名称");
            rvalue.setData(data);
        }
        return rvalue;
    }
    

    
    /**
     * 
     * @param IdForm
     * @return RestResult<Object>
     */
    @RequestMapping("/deleteByAttachguid")
    public ReturnValue deleteFileByAttachguid(@RequestParam("attachguid") String attachguid) {
        return fileService.deleteFileByAttachguid(attachguid);
    }

    /**
     * 
     * @param IdForm
     * @return RestResult<Object>
     */
    @RequestMapping("/deleteByCliengguid")
    public ReturnValue deleteFileByCliengguid(@RequestParam("cliengguid") String cliengguid) {
        return fileService.deleteFileByCliengguid(cliengguid);
    }
    
    @RequestMapping("/updateCliengguid")
    public ReturnValue updateCliengguid(@RequestParam("picliengguid") String picliengguid, @RequestParam("picsecond") String picsecond) {
        return fileService.updateCliengguid(picliengguid, picsecond);
    }
    
    
    
    /**
     * 下载附件
     * @return 返回excel模板
     */
    @RequestMapping(value = "/readAttach", method = RequestMethod.GET, produces ="application/json;charset=UTF-8")
    @ResponseBody
    public Object readAttach(HttpServletRequest request){
        String attachguid = request.getParameter("attachguid");
        if (StringUtil.isNotBlank(attachguid)){
            FrameAttachInfo attachinfo = CommonDao.getInstance().find(FrameAttachInfo.class, attachguid);
            ResponseEntity<InputStreamResource> response = null;
            response = DownloadFileUtil.download(attachinfo.getFilepath(), attachinfo.getAttachfilename(), attachinfo.getContenttype());
            return response;
        }
       
        return null;
    }

    @RequestMapping("/getAttachguidsByCliengguid")
    public ReturnValue getAttachguidsByCliengguid(@RequestParam("cliengguid") String cliengguid) {
        return fileService.getAttachguidsByCliengguid(cliengguid);
    }

}
