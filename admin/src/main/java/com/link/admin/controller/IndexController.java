package com.link.admin.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.LogKit;
import com.link.admin.controller.validator.LoginValidator;
import com.link.admin.controller.vo.TreeMenu;
import com.link.api.service.MenuServiceI;
import com.link.api.service.UserServiceI;
import com.link.common.plugin.shiro.ShiroKit;
import com.link.common.util.Constant;
import com.link.common.util.ResultJson;
import com.link.core.MenuServiceImpl;
import com.link.core.UserServiceImpl;
import com.link.model.Menu;
import com.link.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * Created by linkzz on 2017-05-26.
 */
public class IndexController extends Controller{
    MenuServiceI menuService = enhance(MenuServiceImpl.class);
    UserServiceI userService = enhance(UserServiceImpl.class);
    public void index(){
        if (getPara() != null){
            renderError(404);
        }else if (ShiroKit.isAuthenticated()){
            User user = getSessionAttr("user");
            List<Menu> list = menuService.sidebar(user.getId(),null);
            TreeMenu treeMenu = new TreeMenu(list);
            List<TreeMenu> treeMenuList = treeMenu.buildTree();
            setAttr("menus",treeMenuList);
            setAttr("user",getSessionAttr("user"));
            render("_view/common/_sidebar.html");
            return;
        }else {
            render("login.html");
            return;
        }
    }

    @Clear
    public void home(){
        render("_view/common/_home.html");
    }

    @Clear
    @Before(LoginValidator.class)
    public void doLogin(){
        if (getSessionAttr("user") == null){
            String userName = getPara("userName");
            String password = getPara("password");
            LogKit.info("用户名："+userName +" 密码："+ HashKit.md5(password));
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(userName,HashKit.md5(password));

            ResultJson resultJson = new ResultJson();
            try {
                subject.login(token);
            } catch (UnknownAccountException aue) {
                LogKit.info("账号不存在！");
                resultJson.setMsg("账号不存在！");
                resultJson.setStatus(Constant.RESULT_LOGIN_ACC_NOT_EXISTENT);
                renderJson(resultJson);
                return;
            } catch (DisabledAccountException dae){
                LogKit.info("账号未启用！");
                resultJson.setMsg("账号未启用！");
                resultJson.setStatus(Constant.RESULT_LOGIN_DISABLE);
                renderJson(resultJson);
                return;
            } catch (IncorrectCredentialsException ice){
                LogKit.info("密码错误！");
                resultJson.setMsg("密码错误！");
                resultJson.setStatus(Constant.RESULT_LOGIN_ERROR_PWD);
                renderJson(resultJson);
                return;
            } catch (RuntimeException e) {
                LogKit.info("未知错误！，请联系管理员！");
                resultJson.setMsg("未知错误！，请联系管理员！");
                resultJson.setStatus(Constant.RESULT_LOGIN_NUKOWN);
                renderJson(resultJson);
                return;
            }

            User user = userService.getUserByUserName(userName);
            setSessionAttr("user",user);
            resultJson.setMsg("登录成功！");
            resultJson.setStatus(Constant.RESULT_SUCCESS);
            renderJson(resultJson);
            return;
        }else {
            LogKit.info("当前session信息："+ ShiroKit.getSession().getHost());
            LogKit.info("当前session用户信息："+((User)ShiroKit.getSession().getAttribute("user")).getUsername());
            render("/");
        }
    }

    @Clear
    public void logout(){
        LogKit.info("退出登录系统！");
        Subject subject = ShiroKit.getSubject();
        if (subject.isAuthenticated()){
            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
            render("login.html");
        }
    }
}
