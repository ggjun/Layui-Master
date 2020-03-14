package com.fly.permission.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
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
import com.fly.permission.domain.FlyPermission;
import com.fly.role.api.IFlyRole;
import com.fly.role.domain.FlyRole;
import com.fly.sharemanage.domain.ShareContent;
import com.fly.user.domain.FlyUser;
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
@RequestMapping("flypermission")
public class FlyPermissionController
{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFlyPermission permissionService;
    @Autowired
    private IFlyRole roleService;

    /**
     *
     * 功能描述: 跳到系统用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/permissionManage")
    public String permissionManage() {
        return "flypermission/permissionManage";
    }

    /**
     *
     * 功能描述: 分页查询用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */

    @RequestMapping(value = "/permissionList", method = RequestMethod.POST)
    @ResponseBody
    public PageData<FlyPermission> permissionList(@RequestBody FlyPermission permission) {

        PageData<FlyPermission> pageData = new PageData<FlyPermission>();
        Integer pageNum = 0;
        Integer pageSize = 0;
        try {
            if (StringUtil.isNotBlank(permission.getInt("pageNum"))) {
                pageNum = permission.getInt("pageNum");
            }
            if (StringUtil.isNotBlank(permission.getInt("pageSize"))) {
                pageSize = permission.getInt("pageSize");
            }
            pageData = permissionService.getFlyPermissionList(permission, pageNum, pageSize);
            for (FlyPermission per : pageData.getList()) {
                if ("1".equals(per.getPopup())) {
                    per.setPopup("是");
                }
                else {
                    per.setPopup("否");
                }
            }

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
    @RequestMapping(value = "/setPermission", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> setPermission(@RequestBody String params) {
        FlyPermission permission = JSONObject.parseObject(params, FlyPermission.class);
        if (!permission.containsKey("popup")){
            permission.put("popup","0");
        }
        //移除多余的key
        permission = (FlyPermission) RemoveColumn(permission, "file");
        Map<String, String> data = new HashMap<String, String>();
        try {
            if (permission != null) {
                if (StringUtil.isNotBlank(permission.getId())) {//编辑操作
                    permission.setUpdate_time(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    permissionService.updatePermission(permission);
                    data.put("code", "1");
                    data.put("msg", "保存成功！");
                }
                else {//新增操作
                    permission.setCreate_time(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    permission.setUpdate_time(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    permission = (FlyPermission) RemoveColumn(permission, "id");
                    permissionService.insertPermission(permission);
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
    @RequestMapping(value = "/delPermission", method = RequestMethod.POST)
    @ResponseBody
    public String delPermission(@RequestParam("ids") String ids) {
        String data = null;
        try {
            permissionService.delPermission(ids);
            String idss = ids.replaceAll("'","").replaceAll("\\s*", "");
            List<String> idlist = new ArrayList<String>(Arrays.asList(idss.split(",")));
            List<FlyRole> rolelist = roleService.getFlyRoleList(null, 0, 0).getList();
            List<String> permissionList = null;
            for (FlyRole role : rolelist){
                if (StringUtil.isNotBlank(role.getPermissions())){
                    permissionList = new ArrayList<String>(Arrays.asList(role.getPermissions().split(",")));
                    permissionList.removeAll(idlist);
                    role.setPermissions(StringUtil.join(permissionList, ","));
                    roleService.updateRole(role);
                }
            }
           
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

    /**
    *
    * 功能描述: 获取根权限菜单列表
    *
    * @param:
    * @return:
    * @auther: youqing
    * @date: 2018/11/30 11:35
    */
    @RequestMapping("parentPermissionList")
    @ResponseBody
    public List<FlyPermission> parentPermissionList() {
        logger.info("获取根权限菜单列表");
        FlyPermission flypermission = new FlyPermission();
        flypermission.setPid(0);
        return permissionService.getFlyPermissionListByPid(flypermission, 0, 0).getList();
    }

    /**
    *
    * 功能描述: 获取登陆用户的权限
    *
    * @param:
    * @return:
    * @auther: youqing
    * @date: 2018/12/4 9:48
    */
    @RequestMapping("getUserPerms")
    @ResponseBody
    public Map<String, Object> getUserPerms() {
        Map<String, Object> data = new HashMap<>();
        FlyUser user = (FlyUser) SecurityUtils.getSubject().getPrincipal();
        FlyRole role = roleService.getRoleByRoleid(user.getRole_id());
        if (role != null && StringUtil.isNotBlank(role.getPermissions())){
            String[] permissionsarry = role.getPermissions().split(",");
            List<FlyPermission> permissionList = new ArrayList <FlyPermission>();
            for (String permissionid : permissionsarry){
                FlyPermission permission = permissionService.getPermissionById(permissionid);
                FlyPermission tempermission = new FlyPermission();
                tempermission.setPid(permission.getId());
                List<FlyPermission> list = permissionService.getFlyPermissionListByPid(tempermission, 0, 0).getList();
                permission.put("childrens", list);
                permissionList.add(permission);
            }
            data.put("perm",permissionList);
        }
      
        return data;
    }

}
