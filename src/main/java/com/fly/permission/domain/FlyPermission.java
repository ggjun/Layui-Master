package com.fly.permission.domain;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.fly.basic.domain.Record;


/**
 * @Title: PermissionDTO
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/30 11:22
 */
@Entity
@Table(name="fly_permission",catalog="id")
public class FlyPermission extends Record {
    private Integer id;

    private String name;

    private Integer pid;

    private String descpt;

    private String url;

    private String create_time;

    private String update_time;

    private Integer del_flag;
    
    private String popup;

    public Integer getId() {
        return super.getInt("id");
    }

    public void setId(Integer id) {
       super.put("id", id);
    }

    public String getName() {
        return super.getStr("name");
    }

    public void setName(String name) {
        super.put("name", name);
    }

    public Integer getPid() {
        return super.getInt("pid");
    }

    public void setPid(Integer pid) {
        super.put("pid", pid);
    }

    public String getDescpt() {
        return super.getStr("descpt");
    }

    public void setDescpt(String descpt) {
        super.put("descpt", descpt);
    }

    public String getUrl() {
        return super.getStr("url");
    }

    public void setUrl(String url) {
        super.put("url", url);
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

    public Integer getDel_flag() {
        return super.getInt("del_flag");
    }

    public void setDel_flag(Integer del_flag) {
        super.put("del_flag", del_flag);
    }

    public String getPopup() {
        return super.getStr("popup");
    }

    public void setPopup(String popup) {
        super.put("popup", popup);
    }

   

}
