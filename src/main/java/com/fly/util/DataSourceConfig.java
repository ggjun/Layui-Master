package com.fly.util;

import java.io.Serializable;

import com.fly.shiro.DataSourceConfigFactory;

public class DataSourceConfig implements Cloneable, Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String url;
    private String username;
    private String password;
    
    public DataSourceConfig(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    public DataSourceConfig() {
        // TODO Auto-generated constructor stub
        this.url = DataSourceConfigFactory.getDruidDataSource().getUrl();
        this.username = DataSourceConfigFactory.getDruidDataSource().getUsername();
        this.password =  DataSourceConfigFactory.getDruidDataSource().getPassword();
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
