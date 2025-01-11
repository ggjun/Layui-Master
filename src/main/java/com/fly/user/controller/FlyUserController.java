package com.fly.user.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fly.basic.domain.LoginDTO;
import com.fly.basic.domain.PageData;
import com.fly.basic.domain.Record;
import com.fly.permission.domain.FlyPermission;
import com.fly.user.api.IFlyUser;
import com.fly.user.domain.FlyUser;
import com.fly.util.DigestUtils;
import com.fly.util.EpointDateUtil;
import com.fly.util.StringUtil;



/**
 * @Title: UserController
 * @Description: 系统用户管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Controller
@RequestMapping("flyuser")
public class FlyUserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IFlyUser userService;
   

    /**
     *
     * 功能描述: 登入系统
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 15:47
     */
    @RequestMapping("/login")
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request, LoginDTO loginDTO, HttpSession session){
        Map<String,Object> data = new HashMap();
        // 使用 shiro 进行登录
        Subject subject = SecurityUtils.getSubject();

        String userName = loginDTO.getUsername().trim();
        String password = loginDTO.getPassword().trim();
        String rememberMe = loginDTO.getRememberMe();
        /*String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");*/
        
        String host = request.getRemoteAddr();

        //获取token
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password,host);

        // 设置 remenmberMe 的功能
        if (rememberMe != null && rememberMe.equals("on")) {
            token.setRememberMe(true);
        }

        try {//调用AuthorizingRealm继承类的doGetAuthenticationInfo方法进行身份认证
            subject.login(token);
            // 登录成功
            FlyUser user = (FlyUser) subject.getPrincipal();

            session.setAttribute("user", user.getSys_user_name());
            data.put("code",1);
            data.put("url","/index");
        } catch (UnknownAccountException e) {
            data.put("code",0);
            data.put("message",userName+"账号不存在");
            logger.error(userName+"账号不存在");
            return data;
        }catch (DisabledAccountException e){
            data.put("code",0);
            data.put("message",userName+"账号异常");
            logger.error(userName+"账号异常");
            return data;
        }
        catch (AuthenticationException e){
            data.put("code",0);
            data.put("message",userName+"密码错误");
            logger.error(userName+"密码错误");
            return data;
        }

        return data;
    }

    /**
     *
     * 功能描述: 修改密码
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 17:26
     */
    @RequestMapping("setPwd")
    @ResponseBody
    public Map<String,Object> setP(String pwd, String isPwd){
        logger.info("进行密码重置");
        Map<String,Object> data = new HashMap();
        if(!pwd.equals(isPwd)){
            data.put("code",0);
            data.put("message","两次输入的密码不一致!");
            logger.error("两次输入的密码不一致!");
            return data;
        }
        //获取当前登陆的用户信息
        FlyUser user = (FlyUser) SecurityUtils.getSubject().getPrincipal();
        userService.updatePwd(user,pwd);
        data.put("code",1);
        data.put("msg","修改密码成功！");
        logger.info("用户修改密码成功！");
        return data;
    }

    /**
     *
     * 功能描述: 跳到系统用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/userManage")
    public String userManage() {
        return "flyuser/userManage";
    }

    /**
     *
     * 功能描述: 分页查询用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
   
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    @ResponseBody
    public PageData<FlyUser> getUserList(@RequestBody FlyUser user) {

        PageData<FlyUser> pageData = new PageData<FlyUser>();
        Integer pageNum = 0;
        Integer pageSize = 0;
        try {
            if (StringUtil.isNotBlank(user.getInt("pageNum"))) {
                pageNum = user.getInt("pageNum");
            }
            if (StringUtil.isNotBlank(user.getInt("pageSize"))) {
                pageSize = user.getInt("pageSize");
            }
            pageData = userService.getFlyUserList(user, pageNum, pageSize);
            for (FlyUser flyuser : pageData.getList()) {
                if (1 == flyuser.getUser_status()) {
                    flyuser.put("user_status", "有效");
                }
                else {
                    flyuser.put("user_status", "失效");
                }
              
            }
         }
        catch (Exception e) {
            logger.error("用户列表查询异常！", e);
        }
        return pageData;
    }


    /**
    *
    * 功能描述: 新增或修改记录
    *
    * @param:
    * @return:
    * @auther: youqing
    * @date: 2018/11/21 11:10
    */
    @RequestMapping(value = "/setUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> setUser(@RequestBody String params) {
        FlyUser user = JSONObject.parseObject(params, FlyUser.class);
        if (user.containsKey("file")){
            user = (FlyUser) RemoveColumn(user, "file");
        }
        Map<String, String> data = new HashMap<String, String>();
        try {
            if (user != null) {
                if (StringUtil.isNotBlank(user.getId())) {//编辑操作
                    String password = DigestUtils.Md5(user.getSys_user_name(),user.getSys_user_pwd());
                    user.setSys_user_pwd(password);
                    userService.updateUser(user);
                    data.put("code", "1");
                    data.put("msg", "保存成功！");
                }
                else {
                    //判断用户是否已存在
                    FlyUser flyuser = userService.getUserByUsername(user.getSys_user_name());
                    if (flyuser != null){
                        data.put("code","0");
                        data.put("msg","用户名已存在！");
                        return data;
                    }
                    
                    String phone = user.getUser_phone();
                    if(phone.length() != 11){
                        data.put("code","0");
                        data.put("msg","手机号位数不对！");
                        return data;
                    }
                    
                    String password = null;
                    if(user.getSys_user_pwd() == null){
                        password = DigestUtils.Md5(user.getSys_user_name(),"123456");
                        user.setSys_user_pwd(password);
                    }else{
                        password = DigestUtils.Md5(user.getSys_user_name(),user.getSys_user_pwd());
                        user.setSys_user_pwd(password);
                    }
                    
                    //新增操作
                    user.setReg_time(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    user = (FlyUser) RemoveColumn(user, "id");
                    userService.insertUser(user);
                    data.put("code", "1");
                    data.put("msg", "添加成功！");
                }

            }
        }
        catch (Exception e) {
            data.put("code", "0");
            data.put("msg", "操作失败！");
            return data;
        }

        return data;

    }
    
    /**
    *
    * 功能描述: 删除
    *
    * @param:
    * @return:
    * @auther: youqing
    * @date: 2018/11/21 11:10
    */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    @ResponseBody
    public String delUser(@RequestParam("ids") String ids) {
        String data = null;
        try {
            userService.delUser(ids);
            data = "删除成功！";

        }
        catch (Exception e) {
            logger.error("删除数据异常！", e);
        }
        return data;
    }

    
    
    
    public Record RemoveColumn(Record record, String column) {
        if (record.containsKey(column)) {
            record.remove(column);
        }
        return record;

    }

   

}
