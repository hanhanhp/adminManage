package com.link.cbrc.common;

import com.jfinal.config.Routes;
import com.link.cbrc.controller.BlockController;

/**
 * Created by linkzz on 2017-08-30.
 */
public class CbrcRoutes extends Routes {
    @Override
    public void config() {
        add("/block", BlockController.class,"/");
    }
}
