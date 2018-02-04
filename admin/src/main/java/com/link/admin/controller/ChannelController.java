package com.link.admin.controller;

import com.jfinal.core.Controller;
import com.link.api.service.ChannelServiceI;
import com.link.common.util.DataGrid;
import com.link.common.util.JqGrid;
import com.link.core.ChannelServiceImpl;
import com.link.model.Channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linkzz on 2017-07-06.
 */
public class ChannelController extends Controller {
    ChannelServiceI channelService = enhance(ChannelServiceImpl.class);
    /**
     * @Description: 显示页面
     * @author: linkzz
     * @data: 2017-06-02 11:22
     */
    public void index(){
        render("channel_index.html");
    }

    /**
     * @Description: 封装数据表格
     * @author: linkzz
     * @data: 2017-05-25 22:29
     */
    public void dataGrid(){
        Channel model = getModel(Channel.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        DataGrid dataGrid = channelService.treeDataGrid(jqGrid,model,"t_channel");
        renderJson(dataGrid);
    }

    /**
     * @Description: 增删改一起做
     * @author: linkzz
     * @data: 2017-06-02 11:23
     */
    public void saveOrUpdate(){
        Channel model = getModel(Channel.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        model.setLoaded("true");
        renderJson(channelService.saveOrUpdate(model,model.getId(),"t_channel",jqGrid));
    }

    public void treeChannel(){
        List<Channel> list = channelService.treeChannel();
        Map<String,String> map = new HashMap<String, String>();
        if(list != null && list.size() > 0){
            for (Channel channel : list){
                map.put(channel.getId(),channel.getName());
            }
        }
        renderJson(map);
    }
}
