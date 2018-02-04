package com.link.api.common;

import com.jfinal.config.*;
import com.jfinal.template.Engine;
import com.link.api.controller.ApiController;
import com.link.api.controller.UserController;
import com.link.api.controller.OrderController;
import com.nmtx.doc.core.api.jfinal.JFinalApiDocConfig;

/**
 * Created by linkzz on 2017-05-31.
 */
public class ApiConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants me) {
        loadPropertyFile("jfinal.properties");
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", ApiController.class);
        me.add("/user", UserController.class);
        me.add("/order", OrderController.class);
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        // TODO Auto-generated method stub

    }

    @Override
    public void configInterceptor(Interceptors me) {
        //接口遵守restful接口规范
        //me.addGlobalActionInterceptor(new Restful());
    }

    @Override
    public void configHandler(Handlers me) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        new JFinalApiDocConfig("jdoc.properties").setClearSuffix("Controller").start();
        /*JFinal.start("src/main/webapp", 8080, "/");*/
    }
}
