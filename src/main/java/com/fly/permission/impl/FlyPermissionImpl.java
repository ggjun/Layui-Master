package com.fly.permission.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fly.basic.domain.PageData;
import com.fly.basic.impl.CommonDao;
import com.fly.permission.api.IFlyPermission;
import com.fly.permission.domain.FlyPermission;
import com.fly.util.SQLManageUtil;
import com.fly.util.SqlConditionUtil;
import com.fly.util.StringUtil;


/**
 * @Title: AdminUserServiceImpl
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/21 11:04
 */
@Service
public class FlyPermissionImpl implements IFlyPermission{

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public PageData<FlyPermission> getFlyPermissionList(FlyPermission permission, Integer pageNum, Integer pageSize) {
        // TODO Auto-generated method stub
        SqlConditionUtil sql = new SqlConditionUtil();
        SQLManageUtil manageUtil = new SQLManageUtil();
        if (StringUtil.isNotBlank(permission.getName())){
            sql.like("a.name", permission.getName());
        }
        sql.setLeftJoinTable("fly_permission b", "a.pid", "b.id");
        sql.setSelectFields("a.*,b.name as pname");
        PageData<FlyPermission> pageData = manageUtil.getDbListByPage(FlyPermission.class, sql.getMap(), pageNum, pageSize, "create_time", "desc");
        return pageData;
    }


    @Override
    public PageData<FlyPermission> getFlyPermissionListByPid(FlyPermission permission, Integer pageNum, Integer pageSize) {
        // TODO Auto-generated method stub
        SqlConditionUtil sql = new SqlConditionUtil();
        SQLManageUtil manageUtil = new SQLManageUtil();
        if (StringUtil.isNotBlank(permission.getPid())){
            sql.eq("pid", String.valueOf(permission.getPid()));
        }
        sql.setSelectFields("id,name,url,popup");
        PageData<FlyPermission> pageData = manageUtil.getDbListByPage(FlyPermission.class, sql.getMap(), pageNum, pageSize, "create_time", "desc");
        return pageData;
    }

    @Override
    public List<String> getPermissionNamesListByIds(String permissions) {
        // TODO Auto-generated method stub
        String sql = "select name from fly_permission where id in ?1";
        return CommonDao.getInstance().findList(sql, String.class, permissions);
    }



    @Override
    public void updatePermission(FlyPermission permission) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().update(permission);
    }



    @Override
    public void insertPermission(FlyPermission permission) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().insert(permission);
    }



    @Override
    public void delPermission(String ids) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().deleteByIds(FlyPermission.class, ids);
    }




    @Override
    public FlyPermission getPermissionById(String permissionid) {
        // TODO Auto-generated method stub
        return CommonDao.getInstance().find(FlyPermission.class, permissionid);
    }



   
}
