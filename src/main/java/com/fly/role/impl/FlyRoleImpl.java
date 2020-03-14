package com.fly.role.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fly.basic.domain.PageData;
import com.fly.basic.impl.CommonDao;
import com.fly.role.api.IFlyRole;
import com.fly.role.domain.FlyRole;
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
public class FlyRoleImpl implements IFlyRole{

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public PageData<FlyRole> getFlyRoleList(FlyRole role, Integer pageNum, Integer pageSize) {
        // TODO Auto-generated method stub
        SqlConditionUtil sql = new SqlConditionUtil();
        SQLManageUtil manageUtil = new SQLManageUtil();
        if (role !=null && StringUtil.isNotBlank(role.getRole_name())){ 
            sql.like("role_name", role.getRole_name());
        }
        PageData<FlyRole> pageData = manageUtil.getDbListByPage(FlyRole.class, sql.getMap(), pageNum, pageSize, "update_time", "desc");
        return pageData;
    }



    @Override
    public FlyRole getRoleByRoleid(Integer role_id) {
        // TODO Auto-generated method stub
        return CommonDao.getInstance().find(FlyRole.class, role_id);
    }



    @Override
    public void updateRole(FlyRole role) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().update(role);
    }



    @Override
    public void insertRole(FlyRole role) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().insert(role);
    }



    @Override
    public void delRole(String ids) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().deleteByIds(FlyRole.class, ids);

    }


   
}
