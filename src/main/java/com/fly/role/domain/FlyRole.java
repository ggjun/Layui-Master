package com.fly.role.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fly.basic.domain.Record;



/**
 * @Title: AdminRoleDTO
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/12/3 18:51
 */
@Entity
@Table(name="fly_role",catalog="id")
public class FlyRole extends Record {
    /**
     * 
     */
    private static final long serialVersionUID = 3661822089941131199L;

    private Integer id;

    /**
     * 角色名称
     */
    private String role_name;

    /**
     * 角色描述
     */
    private String role_desc;

    /**
     * 权限
     */
    private String permissions;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 更新时间
     */
    private String update_time;

    /**
     * 1：有效
     0：无效
     */
    private Integer role_status;

    public Integer getId() {
        return super.getInt("id");
    }

    public void setId(Integer id) {
        super.put("id", id);
    }

    public String getRole_name() {
        return super.getStr("role_name");
    }

    public void setRole_name(String role_name) {
        super.put("role_name", role_name);
    }

    public String getRole_desc() {
        return super.getStr("role_desc");
    }

    public void setRole_desc(String role_desc) {
        super.put("role_desc", role_desc);
    }

    public String getPermissions() {
        return super.getStr("permissions");
    }

    public void setPermissions(String permissions) {
        super.put("permissions", permissions);
    }

    public String getCreate_time() {
        return super.getStr("create_time");
    }

    public void setCreate_time(String create_time) {
        super.put("create_time", create_time);
    }

    public String getUpdate_time() {
        return super.getStr("update_time");
    }

    public void setUpdate_time(String update_time) {
        super.put("update_time", update_time);
    }

    public Integer getRole_status() {
        return super.getInt("role_status");
    }

    public void setRole_status(Integer role_status) {
        super.put("role_status", role_status);
    }
    
    
}
