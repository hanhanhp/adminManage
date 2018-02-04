package com.link.api.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.link.api.common.MessageResp;
import com.link.model.User;

/**
 * 用户模块
 *
 * @author linkzz
 *
 *         2017年3月27日
 */
public class UserController extends Controller {

    /**
     * 用于添加用户功能
     * @title 新增用户
     * @param username|用户名|String|必填
     * @param password|密码|String|必填
     * @respBody {"code":"100000","data":"","message":"新增成功"}
     */
    @Before(POST.class)
    public void add() {
        MessageResp<String> resp = new MessageResp<String>();
        resp.setData("");
        resp.setCode("100000");
        resp.setMessage("新增成功");
        renderJson(resp);
    }


    /**
     * 用于删除用户功能
     * @title 删除用户
     * @param id|用户id|Intger|必填
     * @respBody {"code":"100000","data":"","message":"新增成功"}
     */
    public void delete(){
        MessageResp<String> resp = new MessageResp<String>();
        resp.setData("");
        resp.setCode("100000");
        resp.setMessage("删除成功");
        renderJson(resp);
    }


    /**
     * 通过用户id查询用户功能
     * @title 查询ID查用户
     * @respParam username|用户名|String|必填
     * @respParam password|密码|String|必填
     * @respBody {"code":"100000","data":{"password":"123456","username":"13811111111"},"message":"删除成功"}
     */
    public void getUserById(){
        MessageResp<User> resp = new MessageResp<User>();
        User user = new User();
        user.put("username","13811111111");
        user.put("password","123456");
        resp.setData(user);
        resp.setCode("100000");
        resp.setMessage("获取成功");
        renderJson(resp);
    }

    /**
     * 查询用户列表
     * @title 查询用户列表
     * @respParam username|用户名|String|必填
     * @respParam password|密码|String|必填
     * @respBody {"code":"100000","data":{"password":"123456","username":"13811111111"},"message":"删除成功"}
     */
    public void getUserList(){
        MessageResp<User> resp = new MessageResp<User>();
        User user = new User();
        user.put("username","13811111111");
        user.put("password","123456");
        resp.setData(user);
        resp.setCode("100000");
        resp.setMessage("获取成功");
        renderJson(resp);
    }
}
