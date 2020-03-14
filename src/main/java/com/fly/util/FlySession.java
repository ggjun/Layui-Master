package com.fly.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.fly.user.domain.FlyUser;

public class FlySession
{
    private static Subject subject = SecurityUtils.getSubject();
    private static FlyUser user = (FlyUser) subject.getPrincipal();
    public static String getUserName(){
        return user.getSys_user_name();
    }
    
    public static int getUserId(){
        return user.getId();
    }
    
    public static FlyUser getUser(){
        return user;
    }
}
