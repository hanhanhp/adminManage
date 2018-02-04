package com.link.api.service;

import com.jfinal.plugin.activerecord.Page;
import com.link.api.service.base.BaseServiceI;
import com.link.common.util.ResultJson;
import com.link.model.Content;

import java.util.List;

/**
 * Created by linkzz on 2017-07-06.
 */
public interface ContentServiceI extends BaseServiceI {

    public ResultJson save(Content model);

    public Content getContentById(String id);

    public List<Content> findContentList(String cid,boolean pic,int limit, String order);

    public Page<Content> findPage(String cid,int pages,int rows);

    public List<Content> good(int limit);
}
