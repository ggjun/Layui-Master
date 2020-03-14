package com.fly.sharemanage.api;

import com.fly.basic.domain.PageData;
import com.fly.sharemanage.domain.ShareContent;

/**
 * @Title: AdminUserService
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/21 11:04
 */
public interface IShare {


    PageData<ShareContent> getShareContentList(ShareContent content, Integer pageNum, Integer pageSize);

    void delShareContent(String ids);

    void updateShareContent(ShareContent content);

    void insertShareContent(ShareContent content);

  
}
