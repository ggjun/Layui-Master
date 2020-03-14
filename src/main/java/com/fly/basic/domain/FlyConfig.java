package com.fly.basic.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="fly_config",catalog="id")
public class FlyConfig extends Record{
    private String id;
    
    private String configname;
    
    private String configvalue;
    
    private String note;
    
    private String operatedate;
    
    private String operateusername;

    public String getId() {
        return super.getStr("id");
    }

    public void setId(String id) {
        super.put("id", id);
    }

    public String getConfigname() {
        return super.getStr("configname");
    }

    public void setConfigname(String configname) {
        super.put("configname", configname);
    }

    public String getConfigvalue() {
        return super.getStr("configvalue");
    }

    public void setConfigvalue(String configvalue) {
        super.put("configvalue", configvalue);
    }

    public String getNote() {
        return super.getStr("note");
    }

    public void setNote(String note) {
        super.put("note", note);
    }

    public String getOperatedate() {
        return super.getStr("operatedate");
    }

    public void setOperatedate(String operatedate) {
        super.put("operatedate", operatedate);
    }

    public String getOperateusername() {
        return super.getStr("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.put("operateusername", operateusername);
    }
    
    

}
