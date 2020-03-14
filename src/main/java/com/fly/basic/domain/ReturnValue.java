package com.fly.basic.domain;

import java.io.Serializable;

import com.fly.basic.util.ReturnCodeAndMsgEnum;

public class ReturnValue<T> implements Serializable {
    private static final long serialVersionUID = -1959544190118740608L;
    private int code;
    private String msg;
    private T data;

    public ReturnValue() {
        this.code = 1;
        this.msg = "";
        this.data = null;
    }

    public ReturnValue(int retCode, String msg, T data) {
        this.code = retCode;
        this.data = data;
        this.msg = msg;
    }

    public ReturnValue(int retCode, String msg) {
        this.code = retCode;
        this.msg = msg;
    }

    public ReturnValue(ReturnCodeAndMsgEnum codeAndMsg) {
        this(codeAndMsg.getCode(), codeAndMsg.getMsg(), null);
    }

    public ReturnValue(ReturnCodeAndMsgEnum codeAndMsg, T data) {
        this(codeAndMsg.getCode(), codeAndMsg.getMsg(), data);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReturnValue{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}