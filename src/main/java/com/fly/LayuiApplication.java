package com.fly;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LayuiApplication extends SpringBootServletInitializer {
    /*Spring Boot会自动根据jar包的依赖来自动配置项目，例如当你项目下面有HSQLDB的依赖，Spring Boot会自动创建默认的内存数据库的数据源DataSource，
        但我们使用自定义配置，想自己创建DataSource时就必须注释掉DataSourceAutoConfiguration。*/
   
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(LayuiApplication.class, args);
       
    }
    
    @Override//为了打包springboot项目
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
