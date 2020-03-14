package com.fly.basic.util;

import com.fly.util.SpringContextUtil;

public class ContainerFactory
{
    
    public static <T> T getContainInfo(Class<T> cls) {
        T obj = null;
        obj = SpringContextUtil.getBean(cls);
        if (obj == null){
            try {
                obj = cls.newInstance();
            }
            catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return obj;
    }
}
