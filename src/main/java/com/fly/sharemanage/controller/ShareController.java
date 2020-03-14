package com.fly.sharemanage.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fly.basic.domain.PageData;
import com.fly.basic.domain.Record;
import com.fly.sharemanage.api.IShare;
import com.fly.sharemanage.domain.ShareContent;
import com.fly.util.FlySession;
import com.fly.util.StringUtil;

/**
 * @Title: UserController
 * @Description: 系统用户管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Controller
@RequestMapping("share")
public class ShareController
{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IShare shareService;
   

    /**
     *
     * 功能描述: 跳到动态列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/shareManage")
    public String shareManage() {
        return "sharecontent/sharecontent_manage";
    }

    /**
    *
    * 功能描述: 分页查询动态列表
    *
    * @param:
    * @return:
    * @auther: youqing
    * @date: 2018/11/21 11:10
    */
    @RequestMapping(value = "/getShareContentList", method = RequestMethod.POST)
    @ResponseBody
    public PageData<ShareContent> getShareContentList(@RequestBody ShareContent content) {

        PageData<ShareContent> pageData = new PageData<ShareContent>();
        Integer pageNum = 0;
        Integer pageSize = 0;
        try {
           
            if (StringUtil.isNotBlank(content.getInt("pageNum"))) {
                pageNum = content.getInt("pageNum");
            }
            if (StringUtil.isNotBlank(content.getInt("pageSize"))) {
                pageSize = content.getInt("pageSize");
            }
            pageData = shareService.getShareContentList(content, pageNum, pageSize);
            for (ShareContent share : pageData.getList()) {
                if ("1".equals(share.getIf_first())) {
                    share.setIf_first("是");
                }
                else {
                    share.setIf_first("否");
                }
            }

        }
        catch (Exception e) {
            logger.error("动态列表查询异常！", e);
        }
        return pageData;
    }

    /**
    *
    * 功能描述: 新增或修改记录
    *
    * @param:
    * @return:
    * @auther: youqing
    * @date: 2018/11/21 11:10
    */
    @RequestMapping(value = "/setShareContent", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> setShareContent(@RequestBody String params) {
        ShareContent content = JSONObject.parseObject(params, ShareContent.class);
        if (!content.containsKey("if_first")){
            content.put("if_first","0");
        }
        //移除多余的key
        content = (ShareContent) RemoveColumn(content, "file");
        Map<String, String> data = new HashMap<String, String>();
        try {
            if (content != null) {
                if (StringUtil.isNotBlank(content.getId())) {//编辑操作
                    shareService.updateShareContent(content);
                    data.put("code", "1");
                    data.put("msg", "保存成功！");
                }
                else {//新增操作
                    content.setPublictime(new Date());
                    content.setUsername(FlySession.getUserName());
                    content.setUserid(String.valueOf(FlySession.getUserId()));
                    content = (ShareContent) RemoveColumn(content, "id");
                    shareService.insertShareContent(content);
                    data.put("code", "1");
                    data.put("msg", "添加成功！");
                }

            }
        }
        catch (Exception e) {
            data.put("code", "0");
            data.put("msg", "操作失败！");
            return data;
        }

        return data;

    }

    public Record RemoveColumn(Record record, String column) {
        if (record.containsKey(column)) {
            record.remove(column);
        }
        return record;

    }

   

    /**
    *
    * 功能描述: 删除
    *
    * @param:
    * @return:
    * @auther: youqing
    * @date: 2018/11/21 11:10
    */
    @RequestMapping(value = "/delShareContent", method = RequestMethod.POST)
    @ResponseBody
    public String delShareContent(@RequestParam("ids") String ids) {
        String data = null;
        try {
            shareService.delShareContent(ids);
            data = "删除成功！";

        }
        catch (Exception e) {
            logger.error("删除数据异常！", e);
        }
        return data;
    }

}
