package com.fly.user.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fly.basic.domain.PageData;
import com.fly.basic.impl.CommonDao;
import com.fly.user.api.IFlyUser;
import com.fly.user.domain.FlyUser;
import com.fly.util.DigestUtils;
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
public class FlyUserImpl implements IFlyUser{

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public PageData<FlyUser> getFlyUserList(FlyUser user, Integer pageNum, Integer pageSize) {
        // TODO Auto-generated method stub
        SqlConditionUtil sql = new SqlConditionUtil();
        SQLManageUtil manageUtil = new SQLManageUtil();
        if (StringUtil.isNotBlank(user.getSys_user_name())){
            sql.like("sys_user_name", user.getSys_user_name());
        }
        if (StringUtil.isNotBlank(user.getUser_phone())){
            sql.like("user_phone", user.getUser_phone());
        }
        if (StringUtil.isNotBlank(user.getStr("startTime"))){
            sql.ge("reg_time", user.getStr("startTime"));
        }
        if (StringUtil.isNotBlank(user.getStr("endTime"))){
            sql.le("reg_time", user.getStr("endTime"));
        }
        sql.setLeftJoinTable("fly_role b", "role_id", "b.id");
        sql.setSelectFields("a.*,b.role_name");
        PageData<FlyUser> pageData = manageUtil.getDbListByPage(FlyUser.class, sql.getMap(), pageNum, pageSize, "reg_time", "desc");

        return pageData;
    }



    @Override
    public void updateUser(FlyUser user) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().update(user);
    }



    @Override
    public void insertUser(FlyUser user) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().insert(user);
    }



    @Override
    public FlyUser getUserByUsername(String sys_user_name) {
        // TODO Auto-generated method stub
        String sql = "select * from fly_user where sys_user_name = ?1";
        return CommonDao.getInstance().find(sql, FlyUser.class, sys_user_name);
    }



    @Override
    public void delUser(String ids) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().deleteByIds(FlyUser.class, ids);
    }



    @Override
    public void updatePwd(FlyUser user, String pwd) {
        // TODO Auto-generated method stub
        pwd = DigestUtils.Md5(user.getSys_user_name(),pwd);
        user.setSys_user_pwd(pwd);
        CommonDao.getInstance().update(user);
    }


   
}
