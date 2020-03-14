package com.fly.basic.domain;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @Title: AdminUserDTO
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/12/3 12:13
 */
@Entity
@Table(name="frame_attachinfo",catalog="attachguid")
public class FrameAttachInfo extends Record {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String attachguid;
    private String attachfilename;
    private String contenttype;
    private int attachlength;
    private String cliengtag;
    private String cliengguid;
    private String filepath;
    private String uploaddatetime;
    
    public String getAttachguid() {
        return super.getStr("attachguid");
    }

    public void setAttachguid(String attachguid) {
        super.put("attachguid", attachguid);
    } 
    
    
    public String getAttachfilename() {
        return super.getStr("attachfilename");
    }

    public void setAttachfilename(String attachfilename) {
        super.put("attachfilename", attachfilename);
    }

    public String getContenttype() {
        return super.getStr("contenttype");
    }

    public void setContenttype(String contenttype) {
        super.put("contenttype", contenttype);
    }

    public int getAttachlength() {
        return super.getInt("attachlength");
    }

    public void setAttachlength(int attachlength) {
      super.put("attachlength", attachlength);
    }

    public String getCliengtag() {
        return super.getStr("cliengtag");
    }

    public void setCliengtag(String cliengtag) {
        super.put("cliengtag", cliengtag);
    }

    public String getCliengguid() {
        return super.getStr("cliengguid");
    }

    public void setCliengguid(String cliengguid) {
        super.put("cliengguid", cliengguid);
    }

    public String getFilepath() {
        return super.getStr("filepath");
    }

    public void setFilepath(String filepath) {
        super.put("filepath", filepath);
    }

    public String getUploaddatetime() {
        return super.getStr("uploaddatetime");
    }

    public void setUploaddatetime(String uploaddatetime) {
        super.put("uploaddatetime", uploaddatetime);
    } 
   

}
