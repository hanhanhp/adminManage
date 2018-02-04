package com.link.api.service.base;

import com.jfinal.plugin.activerecord.Model;
import com.link.common.util.DataGrid;
import com.link.common.util.JqGrid;
import com.link.common.util.ResultJson;
import com.link.model.Channel;

import java.util.List;

/**
 * Created by linkzz on 2017-05-26.
 */
public interface BaseServiceI {
    /**
     * @Description: 封装数据表格
     * @author: linkzz
     * @data: 2017-05-07 0:10
     */
    public DataGrid dataGrid(JqGrid jqGrid, Model menu, String table);

    /**
     * @Description: 封装treeGrid数据
     * @author: linkzz
     * @data: 2017-06-06 9:27
    */
    public DataGrid treeDataGrid(JqGrid jqGrid, Model t, String table);

    /**
     * @Description: 增删改一起做
     * @author: linkzz
     * @data: 2017-06-02 13:08
     */
    public ResultJson saveOrUpdate(Model model, String id, String table, JqGrid jqGrid);

    public List<Channel> treeChannel();
}
