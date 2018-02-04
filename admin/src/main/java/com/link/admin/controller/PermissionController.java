package com.link.admin.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.link.api.service.PermissionServiceI;
import com.link.api.service.RoleServiceI;
import com.link.common.util.DataGrid;
import com.link.common.util.JqGrid;
import com.link.common.util.ResultJson;
import com.link.core.PermissionServiceImpl;
import com.link.core.RoleServiceImpl;
import com.link.model.Permission;
import com.link.model.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by linkzz on 2017-06-08.
 */
public class PermissionController extends Controller {
    PermissionServiceI permissionService = enhance(PermissionServiceImpl.class);
    RoleServiceI roleService = enhance(RoleServiceImpl.class);
    /**
     * @Description: 显示页面
     * @author: linkzz
     * @data: 2017-06-08 22:14
    */
    public void index(){
        render("permission_index.html");
    }

    /**
     * @Description: 封装数据表格
     * @author: linkzz
     * @data: 2017-06-08 22:17
    */
    public void dataGrid(){
        Permission model = getModel(Permission.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        DataGrid dataGrid = permissionService.treeDataGrid(jqGrid,model,"T_PERMISSION");
        renderJson(dataGrid);
    }

    /**
     * @Description: 增删改一起做
     * @author: linkzz
     * @data: 2017-06-08 22:22
    */
    public void saveOrUpdate(){
        Permission model = getModel(Permission.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        model.setLoaded("true");
        renderJson(permissionService.saveOrUpdate(model,model.getId(),"T_PERMISSION",jqGrid));
    }

    /**
     * @Description: 查询角色分配权限
     * @author: linkzz
     * @data: 2017-06-08 14:58
     */
    public void assignPermission(){
        String roleId = getPara("roleId");
        Role role = roleService.findRoleById(roleId);
        List<Record> list = permissionService.findPermission();
        List<String> rolePermission = permissionService.findRolePermission(roleId);
        List<Permission> permissions = new ArrayList<>();
        for (Record record : list){
            Permission permission = new Permission();
            permission.setId(record.get("id"));
            permission.setName(record.get("name"));
            permission.setIntro(record.getStr("level"));
            permissions.add(permission);
        }
        setAttr("role",role);
        setAttr("list",permissions);
        setAttr("rolePermission",rolePermission);
        render("assign_permission.html");
    }

    /**
     * @Description: 保存角色分配的权限
     * @author: linkzz
     * @data: 2017-06-08 17:20
     */
    public void saveAssignPermission(){
        String roleId = getPara("roleId");
        String [] listModelId = getParaValues("listModelId");
        ResultJson resultJson = permissionService.saveRolePermission(roleId, Arrays.asList(listModelId));
        renderJson(resultJson);
    }
}
