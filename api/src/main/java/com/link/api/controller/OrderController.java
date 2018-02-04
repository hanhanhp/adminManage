package com.link.api.controller;

import com.jfinal.core.Controller;
import com.link.api.common.MessageResp;

/**
 * 订单模块
 * @author: linkzz
 *         2017-06-18 23:11
 */
public class OrderController extends Controller {
    /**
     * 订单详情
     * @title 订单详情
     * @param id|订单id|String|必填
     * @respBody {"code":"0000","data":"","message":"查询成功"}
     */
    public void show(){
        MessageResp<String> resp = new MessageResp<String>();
        resp.setData("");
        resp.setCode("100000");
        resp.setMessage("查询成功");
        renderJson(resp);
    }

    /**
     * 保存订单
     * @title 保存订单
     * @param orderName|订单名称|String|必填
     * @respParam id|订单编号|String|必填
     * @respBody {"code":"0000","data":"","message":"新增成功"}
     */
    public void save(){
        MessageResp<String> resp = new MessageResp<String>();
        resp.setData("");
        resp.setCode("100000");
        resp.setMessage("新增成功");
        renderJson(resp);
    }

    /**
     * 更新订单
     * @title 更新订单
     * @param id|订单编号|String|必填
     * @respParam ordername|订单名称|String|必填
     * @respBody {"code":"0000","data":"","message":"更新成功"}
     */
    public void update(){
        MessageResp<String> resp = new MessageResp<String>();
        resp.setData("");
        resp.setCode("100000");
        resp.setMessage("更新成功");
        renderJson(resp);
    }

    /**
     * 删除订单
     * @title 删除订单
     * @param id|订单编号|String|必填
     * @respParam orderName|订单名称|String|必填
     * @respBody {"code":"0000","data":"","message":"删除成功"}
     */
    public void delete(){
        MessageResp<String> resp = new MessageResp<String>();
        resp.setData("");
        resp.setCode("100000");
        resp.setMessage("删除成功");
        renderJson(resp);
    }

}
