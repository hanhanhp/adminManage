package com.link.core;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.link.api.service.RoleServiceI;
import com.link.common.util.Constant;
import com.link.common.util.ResultJson;
import com.link.core.base.BaseServiceImpl;
import com.link.model.Role;
import com.link.model.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linkzz on 2017-06-05.
 */
public class RoleServiceImpl extends BaseServiceImpl implements RoleServiceI {
    @Override
    public List<Role> findRole() {
        List<Role> list = Role.dao.find("select * from t_role t");
        return list;
    }

    @Override
    public Role findRoleById(String roleId) {
        Role role = Role.dao.findById(roleId);
        return role;
    }

    @Override
    public List<String> findUserRole(String userId) {
        List<UserRole> listUserRole = UserRole.dao.find("SELECT * FROM T_USER_ROLE T WHERE T.USER_ID = ?",userId);
        List<String> list = new ArrayList<String>();
        for (UserRole userRole : listUserRole){
            list.add(userRole.getRoleId());
        }
        return list;
    }

    @Before(Tx.class)
    @Override
    public ResultJson saveUserRole(String userId, List<String> roleIds) {
        ResultJson resultJson = new ResultJson();
        try {
            List<UserRole> listUserRole = UserRole.dao.find("SELECT * FROM T_USER_ROLE T WHERE T.USER_ID = ?",userId);
            if (listUserRole != null && listUserRole.size() > 0){
                for (UserRole userRole : listUserRole){
                    userRole.delete();
                }
            }
            if (roleIds != null && roleIds.size() > 0){
                for (String roleId : roleIds){
                    UserRole userRole = new UserRole();
                    userRole.setId(StrKit.getRandomUUID());
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    userRole.save();
                }
            }
            resultJson.setStatus(Constant.RESULT_SUCCESS);
            resultJson.setMsg("角色分配成功！");
        } catch (Exception e) {
            resultJson.setStatus(Constant.RESULT_SQL_ERROR);
            resultJson.setMsg("数据库操作异常！");
            e.printStackTrace();
        }
        return resultJson;
    }
}
