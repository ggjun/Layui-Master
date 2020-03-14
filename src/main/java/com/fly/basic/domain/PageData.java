package com.fly.basic.domain;


import java.io.Serializable;
import java.util.List;

public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 5500295728912075836L;
    private List<T> list;
    private int rowCount;
    private Integer code=200;
    public PageData() {
    }

    public PageData(List<T> list, int rowCount) {
        this.list = list;
        this.rowCount = rowCount;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public List<T> getList() {
        return list;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    
}