package com.fly.basic.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import com.fly.basic.api.IFlyconfig;
import com.fly.basic.domain.FlyConfig;
import com.fly.basic.domain.PageData;
import com.fly.util.EpointDateUtil;
import com.fly.util.StringUtil;


/**
 * @Title: UserController
 * @Description: 系统用户管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Controller
@RequestMapping("flyconfig")
public class FlyConfigController
{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFlyconfig configService;
    /**
     *
     * 功能描述: 跳到系统参数列表
     *
     * @param:
     * @return:
     * @auther: jankin
     * @date: 
     */
    @RequestMapping("/flyconfig")
    public String permissionManage() {
        return "flyconfig/flyconfig";
    }

    /**
     *
     * 功能描述: 分页查询参数列表
     *
     * @param:
     * @return:
     * @auther: jankin
     * @date: 
     */

    @RequestMapping(value = "/configList", method = RequestMethod.POST)
    @ResponseBody
    public PageData<FlyConfig> configList(@RequestBody FlyConfig config) {

        PageData<FlyConfig> pageData = new PageData<FlyConfig>();
        Integer pageNum = 0;
        Integer pageSize = 0;
        try {
            if (StringUtil.isNotBlank(config.getInt("pageNum"))) {
                pageNum = config.getInt("pageNum");
            }
            if (StringUtil.isNotBlank(config.getInt("pageSize"))) {
                pageSize = config.getInt("pageSize");
            }
            pageData = configService.getFlyConfigList(config, pageNum, pageSize);
           

        }
        catch (Exception e) {
            logger.error("权限列表查询异常！", e);
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
    @RequestMapping(value = "/setConfig", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> setConfig(@RequestBody String params) {
        FlyConfig config = JSONObject.parseObject(params, FlyConfig.class);
        Map<String, String> data = new HashMap<String, String>();
        try {
            if (config != null) {
                if (StringUtil.isNotBlank(config.getId())) {//编辑操作
                    config.setOperatedate(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    configService.updateConfig(config);
                    data.put("code", "1");
                    data.put("msg", "保存成功！");
                }
                else {//新增操作
                    config.setOperatedate(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    config.setId(UUID.randomUUID().toString());
                    configService.insertConfig(config);
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

    /**
    *
    * 功能描述: 删除
    *
    * @param:
    * @return:
    * @auther: youqing
    * @date: 2018/11/21 11:10
    */
    @RequestMapping(value = "/delConfig", method = RequestMethod.POST)
    @ResponseBody
    public String delConfig(@RequestParam("ids") String ids) {
        String data = null;
        try {
            configService.delConfig(ids);
            data = "删除成功！";

        }
        catch (Exception e) {
            logger.error("删除数据异常！", e);
        }
        return data;
    }

}
