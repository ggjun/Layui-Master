package com.fly.sharemanage.domain;

import java.util.Date;

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
@Table(name="share_content",catalog="id")

public class ShareContent extends Record {
   
    /**
     * 
     */
    private static final long serialVersionUID = 5322725450552349888L;

    private Integer id;

    private String userid;

    private String username;
    
    private String content;

    private String imgcontentguid;

    private Date publictime;

    private Integer if_first;
    
    private String startTime;

    private String endTime;

    public Integer getId() {
        return super.getInt("id");
    }

    public void setId(Integer id) {
        super.put("id", id);
       
    }

    public String getUserid() {
       return super.getStr("userid");
    }

    public void setUserid(String userid) {
       super.put("userid", userid);
    }

    public String getUsername() {
        return super.getStr("username");
    }

    public void setUsername(String username) {
        super.put("username", username);
    }

    public String getContent() {
        return super.getStr("content");
    }

    public void setContent(String content) {
        super.put("content", content);
    }

    public String getImgcontentguid() {
        return super.getStr("imgcontentguid");
    }

    public void setImgcontentguid(String imgcontentguid) {
        super.put("imgcontentguid", imgcontentguid);
    }

    public Date getPublictime() {
        
      return super.getDate("publictime");
    }

    public void setPublictime(Date publictime) {
        super.put("publictime", publictime);
    }

    public String getIf_first() {
        return super.getStr("if_first");
    }

    public void setIf_first(String if_first) {
        super.put("if_first", if_first);
    }

    
}
