package com.link.cbrc.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;
import com.link.common.kit.ConvertKit;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by linkzz on 2017-09-13.
 */
public class IPInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        Controller controller = invocation.getController();
        HttpServletRequest request = controller.getRequest();
        long ip = ConvertKit.ipToLong(ConvertKit.getRemoteHost(request));
        long startip = ConvertKit.ipToLong("127.0.0.1");
        long endip = ConvertKit.ipToLong("127.0.0.1");
        LogKit.info("ip="+ip+" startip="+startip + " endip="+endip);
        if (ip >= startip && ip <= endip){
            invocation.invoke();
        }else {
            controller.renderError(403);
        }
    }
}
