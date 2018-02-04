package com.link.admin.common;

import com.jfinal.config.Routes;
import com.link.admin.controller.*;

/**
 * Created by linkzz on 2017-02-17.
 */
public class AdminRoutes extends Routes {
    @Override
    public void config() {
        setBaseViewPath("/_view");
        add("/menu", MenuController.class,"/menu");
        add("/user", UserController.class,"/user");
        add("/role", RoleController.class,"/role");
        add("/permission", PermissionController.class,"/permission");
        add("/log", LogController.class,"/log");
        add("/file",FileController.class,"/file");
        add("/channel",ChannelController.class,"/channel");
        add("/content",ContentController.class,"/content");

    }
}
