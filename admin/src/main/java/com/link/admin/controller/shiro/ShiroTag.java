package com.link.admin.controller.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by linkzz on 2017-03-16.
 */
public class ShiroTag {
    public Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    /**
     * 判断该用户是否为游客身份
     * @return
     */
    public boolean isGuest(){
        return getSubject() == null || getSubject().getPrincipal() == null;
    }

    /**
     * 判断是否是用户身份登录
     * @return
     */
    public boolean isUser(){
        return getSubject() != null && getSubject().getPrincipal() != null;
    }

    /**
     * 是否进行过认证过
     * @return
     */
    public boolean isAuthenticated(){
        return getSubject() != null && getSubject().isAuthenticated();
    }

    /**
     * 是否没有认证过
     * @return
     */
    public boolean isNotAuthenticated(){
        return !isAuthenticated();
    }

    public String principal(Map map){
        String strValue = null;
        if (getSubject() != null){
            Object principal;
            String type = map != null ? (String)map.get("type") : null;
            if (type == null){
                principal = getSubject().getPrincipal();
            }else {
                principal = getPrincipalFromClassName(type);
            }
            String property = map != null ? (String)map.get("property") : null;
            if (principal != null){
                if (property == null){
                    strValue = principal.toString();
                }else {
                    strValue = getPrincipalProperty(principal,property);
                }
            }
        }
        if (strValue != null){
            return strValue;
        }else {
            return null;
        }
    }

    /**
     * 判断是否有这个角色
     * @param roleName
     * @return
     */
    public boolean hasRole(String roleName){
        return getSubject() != null && getSubject().hasRole(roleName);
    }

    /**
     * 判断缺乏这个角色
     * @param roleName
     * @return
     */
    public boolean lacksRole(String roleName){
        boolean hasRole = getSubject() != null && getSubject().hasRole(roleName);
        return !hasRole;
    }

    /**
     * 是否有多个角色
     * @param roleNames
     * @return
     */
    public boolean hasAnyRole(String roleNames){
        boolean hasAnyRole = false;
        Subject subject = getSubject();
        if (subject != null){
            for (String role : roleNames.split(",")){
                if (subject.hasRole(role.trim())){
                    hasAnyRole = true;
                    break;
                }
            }
        }
        return hasAnyRole;
    }

    /**
     * 判断该用户是否有个权限
     * @param p
     * @return
     */
    public boolean hasPermission(String p){
        return getSubject() != null && getSubject().isPermitted(p);
    }

    private String getPrincipalProperty(Object principal, String property) {
        String strValue = null;
        try {
            BeanInfo bi = Introspector.getBeanInfo(principal.getClass());
            boolean foundProperty = false;
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()){
                if (pd.getName().equals(property)){
                    Object value = pd.getReadMethod().invoke(principal,(Object[]) null);
                    strValue = String.valueOf(value);
                    foundProperty = true;
                    break;
                }
            }
            if (!foundProperty) {
                final String message = "Property [" + property
                        + "] not found in principal of type ["
                        + principal.getClass().getName() + "]";

                throw new RuntimeException(message);
            }
        } catch (IntrospectionException e) {
            final String message = "Error reading property [" + property
                    + "] from principal of type ["
                    + principal.getClass().getName() + "]";

            throw new RuntimeException(message, e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return strValue;
    }

    private Object getPrincipalFromClassName(String type) {
        Object principal = null;
        try {
            Class cls = Class.forName(type);
            principal = getSubject().getPrincipals().oneByType(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  principal;
    }
}
