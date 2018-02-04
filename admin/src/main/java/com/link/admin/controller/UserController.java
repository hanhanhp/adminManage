package com.link.admin.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.link.api.service.UserServiceI;
import com.link.common.kit.DateKit;
import com.link.common.util.Constant;
import com.link.common.util.JqGrid;
import com.link.common.util.ResultJson;
import com.link.core.UserServiceImpl;
import com.link.model.User;

import java.util.Date;

/**
 * @ClassName: UserController
 * @Description: 用户管理控制器
 * @author: linkzz
 * @data: 2017-05-06 19:37
*/
public class UserController extends Controller {
    UserServiceI userService = enhance(UserServiceImpl.class);
    /**
     * @Description: 显示页面
     * @author: linkzz
     * @data: 2017-05-06 19:36
    */
    public void index(){
        render("user_index.html");
    }

    /**
     * @Description: 封装dataGrid数据集合
     * @author: linkzz
     * @data: 2017-05-06 19:35
    */
    public void dataGrid(){
        User user = getModel(User.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        renderJson(userService.dataGrid(jqGrid,user,"t_user"));
    }

    /**
     * @Description: 增删改一起做
     * @author: linkzz
     * @data: 2017-05-08 11:48
    */
    public void saveOrUpdate(){
        User user = getModel(User.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        if (jqGrid.getOper().equals(Constant.ADD)){
            user.setPassword(HashKit.md5("111111"));
            user.setCreateAt(DateKit.toStr(new Date(),DateKit.timeStampPattern));
        }else if (jqGrid.getOper().equals(Constant.EDIT)){
            user.setUpdateAt(DateKit.toStr(new Date(),DateKit.timeStampPattern));
        }else {
            user.setDeleteAt(DateKit.toStr(new Date(),DateKit.timeStampPattern));
        }
        renderJson(userService.saveOrUpdate(user,user.getId(),"t_user",jqGrid));
    }

    /**
     * @Description: 用户个人资料
     * @author: linkzz
     * @data: 2017-05-09 11:11
    */
    public void getUser(){
        String id = getPara("id");
        User user = userService.getUser(id);
        setAttr("user",user);
        render("profile.html");
    }

    /**
     * @Description: 用户详情
     * @author: linkzz
     * @data: 2017-05-09 11:11
    */
    public void detail(){
        String id = getPara("id");
        User user = userService.getUser(id);
        setAttr("user",user);
        render("userDetail.html");
    }

    /**
     * @Description: 修改密码
     * @author: linkzz
     * @data: 2017-05-09 11:12
    */
    public void modifyPassword(){
        User user = getSessionAttr("user");
        setAttr("user",user);
        render("modifyPassword.html");
    }

    /**
     * @Description: 保存修改密码
     * @author: linkzz
     * @data: 2017-09-12 9:41
    */
    public void updatepwd(){
        String oldpwd = getPara("user.oldpassword");
        String newpwd = getPara("user.newpassword");
        String repwd = getPara("user.repassword");
        ResultJson resultJson = new ResultJson();
        User user = getSessionAttr("user");
        if (HashKit.md5(oldpwd).equals(user.getPassword())){
            if (repwd.equals(newpwd)){
                user.setPassword(HashKit.md5(newpwd));
                try {
                    user.update();
                    resultJson.setMsg("密码修改成功！");
                    resultJson.setStatus(Constant.RESULT_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    resultJson.setMsg("密码修改失败！数据库操作失败！");
                    resultJson.setStatus(Constant.RESULT_SQL_ERROR);
                }

            }else {
                resultJson.setMsg("原密码错误！");
                resultJson.setStatus(Constant.RESULT_LOGIN_ERROR_PWD);
            }
        }
        renderJson(resultJson);
    }
}
