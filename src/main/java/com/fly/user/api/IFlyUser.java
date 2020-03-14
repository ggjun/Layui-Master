package com.fly.user.api;

import com.fly.basic.domain.PageData;
import com.fly.user.domain.FlyUser;

public interface IFlyUser
{


    PageData<FlyUser> getFlyUserList(FlyUser user, Integer pageNum, Integer pageSize);

    void updateUser(FlyUser user);

    void insertUser(FlyUser user);

    FlyUser getUserByUsername(String sys_user_name);

    void delUser(String ids);

    void updatePwd(FlyUser user, String pwd);

}
