package com.link.admin.controller;

import com.jfinal.core.Controller;
import com.link.api.service.RoleServiceI;
import com.link.api.service.UserServiceI;
import com.link.common.kit.DateKit;
import com.link.common.util.Constant;
import com.link.common.util.JqGrid;
import com.link.common.util.ResultJson;
import com.link.core.RoleServiceImpl;
import com.link.core.UserServiceImpl;
import com.link.model.Role;
import com.link.model.User;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by linkzz on 2017-06-05.
 */
public class RoleController extends Controller {
    RoleServiceI roleService = enhance(RoleServiceImpl.class);
    UserServiceI userService = enhance(UserServiceImpl.class);
    /**
     * @Description: 显示页面
     * @author: linkzz
     * @data: 2017-06-02 11:22
     */
    public void index(){
        render("role_index.html");
    }

    /**
     * @Description: 封装数据表格
     * @author: linkzz
     * @data: 2017-05-25 22:29
     */
    public void dataGrid(){
        Role model = getModel(Role.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        renderJson(roleService.dataGrid(jqGrid,model,"t_role"));
    }

    /**
     * @Description: 增删改一起做
     * @author: linkzz
     * @data: 2017-06-02 11:23
     */
    public void saveOrUpdate(){
        Role model = getModel(Role.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        if (jqGrid.getOper().equals(Constant.ADD)){
            model.setCreatedAt(DateKit.toStr(new Date(),DateKit.timeStampPattern));
        }else if (jqGrid.getOper().equals(Constant.EDIT)){
            model.setUpdatedAt(DateKit.toStr(new Date(),DateKit.timeStampPattern));
        }else {
            model.setDeletedAt(DateKit.toStr(new Date(),DateKit.timeStampPattern));
        }
        renderJson(roleService.saveOrUpdate(model,model.getId(),"t_role",jqGrid));
    }

    /**
     * @Description: 查询分配的角色
     * @author: linkzz
     * @data: 2017-06-07 16:45
    */
    public void assign_role(){
        String userId = getPara("userId");
        User user = userService.getUser(userId);
        List<Role> list = roleService.findRole();
        List<String> listUserRole = roleService.findUserRole(userId);
        setAttr("user",user);
        setAttr("list",list);
        setAttr("userRoles",listUserRole);
        render("assign_role.html");
    }

    /**
     * @Description: 分配角色
     * @author: linkzz
     * @data: 2017-06-08 10:14
    */
    public void saveRoleUser(){
        String userId = getPara("userId");
        String[] listRoleId = getParaValues("listRoleId");
        ResultJson resultJson = roleService.saveUserRole(userId, Arrays.asList(listRoleId));
        renderJson(resultJson);
    }
}
