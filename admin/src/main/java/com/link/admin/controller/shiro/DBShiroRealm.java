package com.link.admin.controller.shiro;

import com.link.model.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;

/**
 * Created by linkzz on 2017-03-02.
 */
public class DBShiroRealm extends AuthorizingRealm {
    /**
     * 授权查询回调函数，进行鉴权但缓存中无用户的授权信息时调用
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        //String username = ((TUser)principalCollection.fromRealm(getName()).iterator().next()).getUsername();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //TUser user = TUser.dao.findFirst("select * from t_user t where t.username = ? and t.delete_at is null",username);
        if (user != null){
            SimpleAuthorizationInfo authorinfo = new SimpleAuthorizationInfo();
            //遍历角色
            List<Role> roles = null;
            String sql = "SELECT t.* FROM t_role t inner join t_user_role r on r.role_id = t.id where r.user_id = ?";
            roles = (List<Role>) Role.dao.findFirst(sql,user.getId());
            List<String> roleNames = null; //角色集合
            List<String> permissionsUrls = null; //权限集合
            if(roles != null && roles.size() > 0){
                for (Role role: roles){
                    roleNames.add(role.getName());
                    String permissionSql = "SELECT t.* FROM t_permission t inner join t_role_permission r on t.id = r.permission_id where r.role_id = ?";
                    List<Permission> permissions = null;
                    permissions = (List<Permission>) Permission.dao.findFirst(permissionSql,role.getId());
                    if (permissions != null && permissions.size() > 0){
                        for (Permission permission : permissions){
                            permissionsUrls.add(permission.getUrl());
                        }
                    }
                }
            }
            if (roleNames != null){
                authorinfo.addRoles(roleNames);
            }
            if (permissionsUrls != null){
                authorinfo.addStringPermissions(permissionsUrls);
            }
            return authorinfo;
        }else {
            throw new UnknownAccountException();
        }
    }

    /**
     * 认证回调函数，登录时调用
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        String username = userToken.getUsername();
        User user = null;
        user = User.dao.findFirst("select * from t_user t where t.username = ?",username);
        if (user != null){
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),getName());
            return info;
        }else {
            //如果用户不存在，抛出用户不存在异常
            throw new UnknownAccountException();
        }

    }
}
