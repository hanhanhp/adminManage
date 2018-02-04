package com.link.admin.controller;

import com.jfinal.core.Controller;
import com.link.api.service.LogServiceI;
import com.link.common.util.JqGrid;
import com.link.core.LogServiceImpl;
import com.link.model.Log;

/**
 * Created by linkzz on 2017-06-14.
 */
public class LogController extends Controller {
    LogServiceI logService = enhance(LogServiceImpl.class);

    /**
     * @Description: 显示页面
     * @author: linkzz
     * @data: 2017-06-02 11:22
     */
    public void index(){
        render("log_index.html");
    }

    /**
     * @Description: 封装数据表格
     * @author: linkzz
     * @data: 2017-05-25 22:29
     */
    public void dataGrid(){
        Log model = getModel(Log.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        renderJson(logService.dataGrid(jqGrid,model,"t_log"));
    }
}
