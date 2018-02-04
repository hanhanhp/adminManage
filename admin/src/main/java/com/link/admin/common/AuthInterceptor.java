package com.link.admin.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.link.api.service.PermissionServiceI;
import com.link.common.plugin.shiro.ShiroKit;
import com.link.common.util.Constant;
import com.link.common.util.ResultJson;
import com.link.core.PermissionServiceImpl;
import com.link.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * @ClassName: AuthInterceptor
 * @Description: 权限拦截器
 * @author: linkzz
 * @data: 2017-05-22 10:46
*/
public class AuthInterceptor implements Interceptor {
    PermissionServiceI permissionService = enhance(PermissionServiceImpl.class);
    /**
     * 获取全部 需要控制的权限
     */
    private static List<String> urls = null;
    @Override
    public void intercept(Invocation invocation) {
        Controller controller = invocation.getController();
        HttpServletRequest request = controller.getRequest();

        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(header);
        ResultJson resultJson = new ResultJson();
        resultJson.setStatus(Constant.RESULT_FORBIDDEN);
        resultJson.setMsg("禁止访问！如需访问请联系管理员！");
        if (ShiroKit.isAuthenticated()){
            if (urls == null) {
                User user = controller.getSessionAttr("user");
                urls = permissionService.urls(user.getId());
            }
            String url = invocation.getActionKey();
            try {
                if (!urls.contains(url)) {
                    if (isAjax){
                        controller.renderJson(resultJson);
                    }else {
                        controller.renderError(403);
                    }
                }
            } catch (Exception e) {
                if (isAjax){
                    controller.renderJson(resultJson);
                }else {
                    controller.renderError(403);
                }
            }
            invocation.invoke();
        }else {
            controller.render("/login.html");
        }
    }
}
