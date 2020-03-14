package com.fly.role.api;

import com.fly.basic.domain.PageData;
import com.fly.role.domain.FlyRole;

public interface IFlyRole
{


    PageData<FlyRole> getFlyRoleList(FlyRole role, Integer pageNum, Integer pageSize);

    FlyRole getRoleByRoleid(Integer role_id);

    void updateRole(FlyRole role);

    void insertRole(FlyRole role);

    void delRole(String ids);

}
