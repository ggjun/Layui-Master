package com.fly.sharemanage.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fly.basic.api.ICommonDao;
import com.fly.basic.domain.PageData;
import com.fly.basic.domain.Record;
import com.fly.basic.impl.CommonDao;
import com.fly.sharemanage.api.IShare;
import com.fly.sharemanage.domain.ShareContent;
import com.fly.util.DataSourceConfig;
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
public class ShareImpl implements IShare{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    
    @Override
    public PageData<ShareContent> getShareContentList(ShareContent content, Integer pageNum, Integer pageSize) {
        // TODO Auto-generated method stub
        SqlConditionUtil sql = new SqlConditionUtil();
        SQLManageUtil manageUtil = new SQLManageUtil();
        if (StringUtil.isNotBlank(content.getUsername())){
            sql.like("username", content.getUsername());
        }
        if (StringUtil.isNotBlank(content.getStr("startTime"))){
            sql.ge("publictime", content.getStr("startTime"));
        }
        if (StringUtil.isNotBlank(content.getStr("endTime"))){
            sql.le("publictime", content.getStr("endTime"));
        }
       
        PageData<ShareContent> pageData = manageUtil.getDbListByPage(ShareContent.class, sql.getMap(), pageNum, pageSize, "publictime", "desc");
        return pageData;
    }


    @Override
    public void delShareContent(String ids) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().deleteByIds(ShareContent.class, ids);
    }


    @Override
    public void updateShareContent(ShareContent content) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().update(content);
    }


    @Override
    public void insertShareContent(ShareContent content) {
        // TODO Auto-generated method stub
        CommonDao.getInstance().insert(content);
    }

   
}
