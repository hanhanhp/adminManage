package com.link.api.service;

import com.link.api.service.base.BaseServiceI;
import com.link.common.util.ResultJson;
import com.link.model.Role;

import java.util.List;

/**
 * Created by linkzz on 2017-06-05.
 */
public interface RoleServiceI extends BaseServiceI {
    public List<Role> findRole();
    public Role findRoleById(String roleId);
    public List<String> findUserRole(String userId);
    public ResultJson saveUserRole(String userId,List<String> roleIds);
}
