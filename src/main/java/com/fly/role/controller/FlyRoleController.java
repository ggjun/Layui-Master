package com.fly.role.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.fly.permission.api.IFlyPermission;
import com.fly.role.api.IFlyRole;
import com.fly.role.domain.FlyRole;
import com.fly.util.EpointDateUtil;
import com.fly.util.StringUtil;



/**
 * @Title: UserController
 * @Description: 系统用户管理
 * @author: ggjun
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Controller
@RequestMapping("flyrole")
public class FlyRoleController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

  
    @Autowired
    private IFlyRole roleService;
    @Autowired
    private IFlyPermission permissionService;
   
    /**
     *
     * 功能描述: 跳到系统用户列表
     *
     * @param:
     * @return:
     * @auther: ggjun
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/roleManage")
    public String roleManage() {
        return "flyrole/roleManage";
    }

    /**
     *
     * 功能描述: 分页查询角色列表
     *
     * @param:
     * @return:
     * @auther: ggjun
     * @date: 2018/11/21 11:10
     */
   
    @RequestMapping(value = "/getRoleList", method = RequestMethod.POST)
    @ResponseBody
    public PageData<FlyRole> getRoleList(@RequestBody FlyRole role) {

        PageData<FlyRole> pageData = new PageData<FlyRole>();
        Integer pageNum = 0;
        Integer pageSize = 0;
        try {
            if (StringUtil.isNotBlank(role.getInt("pageNum"))) {
                pageNum = role.getInt("pageNum");
            }
            if (StringUtil.isNotBlank(role.getInt("pageSize"))) {
                pageSize = role.getInt("pageSize");
            }
            pageData = roleService.getFlyRoleList(role, pageNum, pageSize);
            for (FlyRole flyrole : pageData.getList()) {
                if (1 == flyrole.getRole_status()) {
                    flyrole.put("role_status", "有效");
                }
                else {
                    flyrole.put("role_status", "失效");
                }
                if (StringUtil.isNotBlank(flyrole.getPermissions())){
                    List<String> list = permissionService.getPermissionNamesListByIds(flyrole.getPermissions());
                    flyrole.put("permissionscontent", list.toString());
                }
               
            }
         }
        catch (Exception e) {
            logger.error("角色列表查询异常！", e);
        }
        return pageData;
    }
    
    
    /**
    *
    * 功能描述: 获取角色菜单列表
    *
    * @param:
    * @return:
    * @auther: ggj
    * @date: 2018/11/30 11:35
    */
   @RequestMapping("getRoles")
   @ResponseBody
   public List<FlyRole> getRoles(){
       logger.info("获取角色菜单列表");
       return roleService.getFlyRoleList(null, 0, 0).getList();
   }

    
    /**
    *
    * 功能描述: 新增或修改记录
    *
    * @param:
    * @return:
    * @auther: ggjun
    * @date: 2018/11/21 11:10
    */
    @RequestMapping(value = "/setRole", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> setRole(@RequestBody String params) {
        FlyRole role = JSONObject.parseObject(params, FlyRole.class);
        
        Map<String, String> data = new HashMap<String, String>();
        try {
            if (role != null) {
                if (StringUtil.isNotBlank(role.getId())) {//编辑操作
                    role.setUpdate_time(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    roleService.updateRole(role);
                    data.put("code", "1");
                    data.put("msg", "保存成功！");
                }
                else {//新增操作
                    role.setCreate_time(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    role.setUpdate_time(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    role = (FlyRole) RemoveColumn(role, "id");
                    roleService.insertRole(role);
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
    * @auther: ggjun
    * @date: 2018/11/21 11:10
    */
    @RequestMapping(value = "/delRole", method = RequestMethod.POST)
    @ResponseBody
    public String delRole(@RequestParam("ids") String ids) {
        String data = null;
        try {
            roleService.delRole(ids);
            data = "删除成功！";

        }
        catch (Exception e) {
            logger.error("删除数据异常！", e);
        }
        return data;
    }

    public Record RemoveColumn(Record record, String column) {
        if (record.containsKey(column)) {
            record.remove(column);
        }
        return record;

    }

}
