package com.link.admin.controller;

import com.jfinal.core.Controller;
import com.link.api.service.ChannelServiceI;
import com.link.api.service.ContentServiceI;
import com.link.common.kit.DateKit;
import com.link.common.util.Constant;
import com.link.common.util.JqGrid;
import com.link.core.ChannelServiceImpl;
import com.link.core.ContentServiceImpl;
import com.link.model.Channel;
import com.link.model.Content;
import com.link.model.User;

import java.util.Date;
import java.util.List;

/**
 * Created by linkzz on 2017-07-06.
 */
public class ContentController extends Controller {
    ContentServiceI contentService = enhance(ContentServiceImpl.class);
    ChannelServiceI channelService = enhance(ChannelServiceImpl.class);
    /**
     * @Description: 显示页面
     * @author: linkzz
     * @data: 2017-05-06 19:36
     */
    public void index(){
        render("content_index.html");
    }

    /**
     * @Description: 封装dataGrid数据集合
     * @author: linkzz
     * @data: 2017-05-06 19:35
     */
    public void dataGrid(){
        Content model = getModel(Content.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        renderJson(contentService.dataGrid(jqGrid,model,"t_content"));
    }

    /**
     * @Description: 增删改一起做
     * @author: linkzz
     * @data: 2017-05-08 11:48
     */
    public void saveOrUpdate(){
        Content model = getModel(Content.class,"",true);
        JqGrid jqGrid = getBean(JqGrid.class,"",true);
        if (jqGrid.getOper().equals(Constant.ADD)){
            User user = getSessionAttr("user");
            model.setCreatetime(DateKit.toStr(new Date(),DateKit.timeStampPattern));
            model.setAuthorId(user.getId());
            model.setAuthorName(user.getUsername());
        }else {
            model.setModifytime(DateKit.toStr(new Date(),DateKit.timeStampPattern));
        }
        renderJson(contentService.saveOrUpdate(model,model.getId(),"t_content",jqGrid));
    }

    public void add(){
        List<Channel> channels = channelService.findChannelList();
        setAttr("channels",channels);
        render("add.html");
    }

    public void save(){
        Content model = getModel(Content.class,"model",true);
        User user = getSessionAttr("user");
        model.setAuthorId(user.getId());
        model.setAuthorName(user.getUsername());
        renderJson(contentService.save(model));
    }

    public void edit(){
        String id = getPara("id");
        if (id != null && !"".equals(id)){
            List<Channel> channels = channelService.findChannelList();
            setAttr("channels",channels);
            Content content = contentService.getContentById(id);
            setAttr("content",content);
            render("edit.html");
        }

    }
}
