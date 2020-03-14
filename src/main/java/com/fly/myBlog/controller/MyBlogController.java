package com.fly.myBlog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title: UserController
 * @Description: 个人博客
 * @author: ggjun
 * @version: 1.0
 */
@Controller
@RequestMapping("myblog")
public class MyBlogController
{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     * 功能描述: 
     *
     * @param:
     * @return:
     * @auther: ggjun
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/index")
    public String index() {
        return "myBlog/index";
    }

    @RequestMapping("/content")
    public String content() {
        return "myBlog/content";
    }

}
