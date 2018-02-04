package com.link.admin.common;

import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.LogInterceptor;
import com.jfinal.json.JFinalJson;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.link.common.kit.ConvertKit;
import com.link.common.kit.DateKit;
import com.link.model.Log;
import com.link.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by linkzz on 2017-06-12.
 */
public class ExceptionAndLogInterceptor extends LogInterceptor {
    private Pattern pattern;
    public ExceptionAndLogInterceptor(String regex){
        this(regex,true);
    }

    public ExceptionAndLogInterceptor(String regex,boolean caseSensitive){
        if(StrKit.isBlank(regex)) {
            throw new IllegalArgumentException("regex can not be blank.");
        } else {
            this.pattern = caseSensitive?Pattern.compile(regex):Pattern.compile(regex, 2);
        }
    }
    public void intercept(Invocation invocation){
        invocation.invoke();
        if(this.pattern.matcher(invocation.getActionKey()).matches()) {
            Controller controller = invocation.getController();
            HttpServletRequest request = controller.getRequest();

            User user = controller.getSessionAttr("user");
            LogKit.info("当前用户："+user.getUsername());
            Log log = new Log();
            log.setId(StrKit.getRandomUUID());
            log.setActionkey(invocation.getActionKey());
            log.setOpertime(DateKit.toStr(new Date(),DateKit.timeStampPattern));
            log.setUserid(user.getId());
            log.setUsername(user.getUsername());
            log.setIp(ConvertKit.getRemoteHost(request));
            log.setParams(JFinalJson.getJson().toJson(request.getParameterMap()));
            log.setResult(JFinalJson.getJson().toJson(controller.getRender()));
            log.save();
        }
    }
}
