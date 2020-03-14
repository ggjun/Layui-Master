package com.fly.layuiSimpleBlog.controller;

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
@RequestMapping("blog")
public class BlogController
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
        return "layuiSimpleBlog/index";
    }

    /**
    *
    * 功能描述: 
    *
    * @param:
    * @return:
    * @auther: ggjun
    * @date: 2018/11/21 13:50
    */
    @RequestMapping("/about")
    public String blogabout() {
        return "layuiSimpleBlog/about";
    }

    /**
    *
    * 功能描述:
    *
    * @param:
    * @return:
    * @auther: ggjun
    * @date: 2018/11/21 13:50
    */
    @RequestMapping("/album")
    public String album() {
        return "layuiSimpleBlog/album";
    }

    /**
    *
    * 功能描述: 
    *
    * @param:
    * @return:
    * @auther: ggjun
    * @date: 2018/11/21 13:50
    */
    @RequestMapping("/details")
    public String blogdetails() {
        return "layuiSimpleBlog/details";
    }
    
    @RequestMapping("/leacots")
    public String leacots() {
        return "layuiSimpleBlog/leacots";
    }
    
    @RequestMapping("/whisper")
    public String whisper() {
        return "layuiSimpleBlog/whisper";
    }

}
