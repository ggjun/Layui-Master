package com.fly.basic.util;

import com.fly.basic.impl.CommonDao;

public class ConfigUtil
{
    public static String getConfigValueByName(String name){
        String sql = "select configvalue from fly_config where configname=?1";
        return CommonDao.getInstance().queryString(sql,name);
    }
}
