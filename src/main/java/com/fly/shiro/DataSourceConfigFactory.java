package com.fly.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DataSourceConfigFactory
{  
    private static DruidDataSource druidDataSource;
    
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DruidDataSource druid(){
        druidDataSource = new DruidDataSource();
        return druidDataSource;
      
    }

    public static DruidDataSource getDruidDataSource() {
        return druidDataSource;
    }

    
    public static DruidDataSource getDruidByDruidDataSource(String url, String username, String password){
        DruidDataSource druid = null;
        try {
            druid = (DruidDataSource) druidDataSource.clone();
            druid.setUrl(url);
            druid.setUsername(username);
            druid.setPassword(password);
        }
        catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        return druid;
    }
   
}
