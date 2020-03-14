package com.fly.basic.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fly.basic.api.IFlyconfig;
import com.fly.basic.domain.FlyConfig;
import com.fly.basic.domain.PageData;
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
public class FlyConfigImpl implements IFlyconfig{

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public PageData<FlyConfig> getFlyConfigList(FlyConfig config, Integer pageNum, Integer pageSize) {
        // TODO Auto-generated method stub
        SqlConditionUtil sql = new SqlConditionUtil();
        SQLManageUtil manageUtil = new SQLManageUtil();
        if (StringUtil.isNotBlank(config.getConfigname())){
            sql.like("configname", config.getConfigname());
        }
        if (StringUtil.isNotBlank(config.getNote())){
            sql.like("note", config.getNote());
        }
        PageData<FlyConfig> pageData = manageUtil.getDbListByPage(FlyConfig.class, sql.getMap(), pageNum, pageSize, "operatedate", "desc");
        return pageData;
    }

    @Override
    public void updateConfig(FlyConfig config) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().update(config);
    }



    @Override
    public void insertConfig(FlyConfig config) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().insert(config);
    }



    @Override
    public void delConfig(String ids) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().deleteByIds(FlyConfig.class, ids);
    }


   
}
