package com.link.cbrc.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.link.api.service.ChannelServiceI;
import com.link.api.service.ContentServiceI;
import com.link.common.kit.ConvertKit;
import com.link.common.kit.StrKit;
import com.link.core.ChannelServiceImpl;
import com.link.core.ContentServiceImpl;
import com.link.model.Channel;
import com.link.model.Content;

import java.util.List;

/**
 * Created by linkzz on 2017-08-30.
 */
public class IndexController extends Controller {
    ChannelServiceI channelService = enhance(ChannelServiceImpl.class);
    ContentServiceI contentService = enhance(ContentServiceImpl.class);

    /**
     * @Description: 首页显示
     * @author: linkzz
     * @data: 2017-08-30 16:05
    */
    public void index(){
        List<Content> newspic = contentService.findContentList("c1b10bb1e6b144d498d4ff9e2dcfd4c1",true,5,"asc");
        setAttr("newpics",newspic);
        render("/_view/layout.html");
    }

    /**
     * @Description: 列表页显示
     * @author: linkzz
     * @data: 2017-08-30 16:05
    */
    public void list(){
        int pages = getParaToInt(0);
        String cid = getPara(1);
        int rows = 10;
        Channel channel = channelService.getChannal(cid);
        Page<Content> page = contentService.findPage(cid,pages,rows);
        setAttr("channelname",channel.getName());
        setAttr("channelId",channel.getId());
        setAttr("page",page);
        render("/_view/list.html");
    }

    /**
     * @Description: 详情页显示
     * @author: linkzz
     * @data: 2017-08-30 16:05
    */
    public void detail(){
        Content content = contentService.getContentById(getPara());
        long ip = ConvertKit.ipToLong(ConvertKit.getRemoteHost(getRequest()));
        if (StrKit.notBlank(content.getStartip()) && StrKit.notBlank(content.getEndip())){
            long startip = ConvertKit.ipToLong(content.getStartip());
            long endip = ConvertKit.ipToLong(content.getEndip());
            if (ip >= startip && ip <= endip){
                Channel channel = channelService.getChannal(content.getCid());
                setAttr("content",content);
                setAttr("channelname",channel.getName());
                render("/_view/detail.html");
            }else {
                renderError(403);
            }
        }else {
            Channel channel = channelService.getChannal(content.getCid());
            setAttr("content",content);
            setAttr("channelname",channel.getName());
            render("/_view/detail.html");
        }
    }
}
