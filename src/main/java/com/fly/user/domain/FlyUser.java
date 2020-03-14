package com.fly.user.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.fly.basic.domain.Record;


/**
 * @Title: AdminUserDTO
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/12/3 12:13
 */
@Entity
@Table(name="fly_user",catalog="id")
public class FlyUser extends Record {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String sys_user_name;

    private String sys_user_pwd;

    private Integer role_id;
    
    private String user_phone;
    
    private String reg_time;

    private Integer userStatus;
    
    private String piccliengguid;

    public Integer getId() {
        return super.getInt("id");
    }

    public void setId(Integer id) {
        super.put("id", id);
    }

    public String getSys_user_name() {
        return super.getStr("sys_user_name");
    }

    public void setSys_user_name(String sys_user_name) {
        super.put("sys_user_name", sys_user_name);
    }

    public String getSys_user_pwd() {
        return super.getStr("sys_user_pwd");
    }

    public void setSys_user_pwd(String sys_user_pwd) {
        super.put("sys_user_pwd", sys_user_pwd);
    }

    public Integer getRole_id() {
        return super.getInt("role_id");
    }

    public void setRole_id(Integer role_id) {
        super.put("role_id", role_id);
    }

    public String getUser_phone() {
        return super.getStr("user_phone");
    }

    public void setUser_phone(String user_phone) {
        super.put("user_phone", user_phone);
    }

    public String getReg_time() {
        return super.getStr("reg_time");
    }

    public void setReg_time(String reg_time) {
        super.put("reg_time", reg_time);
    }

    public Integer getUser_status() {
        return super.getInt("user_status");
    }

    public void setUser_status(Integer user_status) {
        super.put("user_status", user_status);
    }

    public String getPiccliengguid() {
        return super.getStr("piccliengguid");
    }

    public void setPiccliengguid(String piccliengguid) {
        super.put("piccliengguid", piccliengguid);
    }
    
    

}
