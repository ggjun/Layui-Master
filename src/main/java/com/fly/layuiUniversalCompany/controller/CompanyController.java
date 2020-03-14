package com.fly.layuiUniversalCompany.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title: UserController
 * @Description: 公司门户
 * @author: ggjun
 * @version: 1.0
 */
@Controller
@RequestMapping("company")
public class CompanyController
{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     * 功能描述: 跳到公司门户首页
     *
     * @param:
     * @return:
     * @auther: ggjun
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/index")
    public String index() {
        return "layuiUniversalCompany/index";
    }

    /**
    *
    * 功能描述: 跳到公司门户案例
    *
    * @param:
    * @return:
    * @auther: ggjun
    * @date: 2018/11/21 13:50
    */
    @RequestMapping("/case")
    public String Companycase() {
        return "layuiUniversalCompany/case";
    }

    /**
    *
    * 功能描述: 跳到公司门户服务
    *
    * @param:
    * @return:
    * @auther: ggjun
    * @date: 2018/11/21 13:50
    */
    @RequestMapping("/service")
    public String service() {
        return "layuiUniversalCompany/service";
    }

    /**
    *
    * 功能描述: 跳到公司门户关于
    *
    * @param:
    * @return:
    * @auther: ggjun
    * @date: 2018/11/21 13:50
    */
    @RequestMapping("/about")
    public String about() {
        return "layuiUniversalCompany/about";
    }
    
    @RequestMapping("/details")
    public String details() {
        return "layuiUniversalCompany/details";
    }

}
