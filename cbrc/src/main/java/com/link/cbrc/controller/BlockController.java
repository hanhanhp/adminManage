package com.link.cbrc.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Page;
import com.link.api.service.ChannelServiceI;
import com.link.api.service.ContentServiceI;
import com.link.core.ChannelServiceImpl;
import com.link.core.ContentServiceImpl;
import com.link.model.Channel;
import com.link.model.Content;

/**
 * Created by linkzz on 2017-09-12.
 */
public class BlockController extends Controller {
    ChannelServiceI channelService = enhance(ChannelServiceImpl.class);
    ContentServiceI contentService = enhance(ContentServiceImpl.class);
    /**
     * @Description: 专题活动
     * @author: linkzz
     * @data: 2017-09-11 20:01
     */
    public void index(){
        String block = getPara();
        LogKit.info("block:"+block);
        setAttr("blockid",block);
        render("/_view/block.html");
    }

    /**
     * @Description: 专题活动列表页
     * @author: linkzz
     * @data: 2017-09-11 20:01
     */
    public void list(){
        String block = getPara(0);      //那个专题
        int pages = getParaToInt(1);    //当前页
        String cid = getPara(2);        //频道id
        int rows = 10;
        Channel channel = channelService.getChannal(cid);
        Page<Content> page = contentService.findPage(cid,pages,rows);
        setAttr("channelname",channel.getName());
        setAttr("channelId",channel.getId());
        setAttr("page",page);
        render("/_view/block_list.html");
    }

    /**
     * @Description: 专题活动详情页
     * @author: linkzz
     * @data: 2017-09-11 20:01
     */
    public void detail(){
        Content content = contentService.getContentById(getPara());
        Channel channel = channelService.getChannal(content.getCid());
        setAttr("content",content);
        setAttr("channelname",channel.getName());
        render("/_view/block_detail.html");
    }
}
