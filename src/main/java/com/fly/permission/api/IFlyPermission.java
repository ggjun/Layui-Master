package com.fly.permission.api;

import java.util.List;

import com.fly.basic.domain.PageData;
import com.fly.permission.domain.FlyPermission;

public interface IFlyPermission
{


    PageData<FlyPermission> getFlyPermissionList(FlyPermission permission, Integer pageNum, Integer pageSize);

    List<String> getPermissionNamesListByIds(String permissions);

    void updatePermission(FlyPermission permission);

    void insertPermission(FlyPermission permission);

    void delPermission(String ids);

    FlyPermission getPermissionById(String permissionid);
    
    PageData<FlyPermission> getFlyPermissionListByPid(FlyPermission permission, Integer pageNum, Integer pageSize);

}
