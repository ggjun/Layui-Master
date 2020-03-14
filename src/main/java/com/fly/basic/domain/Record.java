package com.fly.basic.domain;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

import com.fly.util.ConvertUtil;



public class Record extends HashMap<String,Object> implements Serializable{
    private static final long serialVersionUID = 905784513600884082L;
    
    
    public Double getDouble(String column) {
        return (Double) ConvertUtil.convertDataType(Double.class, this.get(column));
    }

    public Integer getInt(String column) {
        return (Integer) ConvertUtil.convertDataType(Integer.class, this.get(column));
    }

    public Time getTime(String column) {
        return (Time) this.get(column);
    }

    public Float getFloat(String column) {
        return (Float) ConvertUtil.convertDataType(Float.class, this.get(column));
    }

    public Number getNumber(String column) {
        return (Number) this.get(column);
    }

    public Date getDate(String column) {
        return (Date) ConvertUtil.convertDataType(Date.class, this.get(column));
    }
    public Record remove(String... columns) {
        if (columns != null) {
            String[] arg1 = columns;
            int arg2 = columns.length;
            int arg3;
            int arg9999 = arg3 = 0;

            for (int arg10000 = arg2; arg9999 < arg10000; arg10000 = arg2) {
                String a = arg1[arg3++];
                arg9999 = arg3;
                this.remove(a);
            }
        }

        return this;
    }
    public String getStr(String column) {
        return (String) ConvertUtil.convertDataType(String.class, this.get(column));
    }
    
}
