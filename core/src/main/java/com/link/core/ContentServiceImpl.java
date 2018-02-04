package com.link.core;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.link.api.service.ContentServiceI;
import com.link.common.kit.DateKit;
import com.link.common.util.Constant;
import com.link.common.util.ResultJson;
import com.link.core.base.BaseServiceImpl;
import com.link.model.Content;

import java.util.Date;
import java.util.List;

/**
 * Created by linkzz on 2017-07-06.
 */
public class ContentServiceImpl extends BaseServiceImpl implements ContentServiceI {
    @Override
    public ResultJson save(Content model) {
        ResultJson resultJson = new ResultJson();
        if (model.getBold() == null){
            model.setBold("off");
        }
        if (model.getStatus() == null){
            model.setStatus("off");
        }
        if (model.getHits() == null){
            model.setHits("off");
        }
        if (model.getGood() == null){
            model.setGood("off");
        }
        if (model.getItalic() == null){
            model.setItalic("off");
        }
        try {
            if ("".equals(model.getId()) || model.getId() == null){
                model.setId(StrKit.getRandomUUID());
                model.setCreatetime(DateKit.toStr(new Date(),DateKit.timeStampPattern));
                model.save();
            } else {
                model.setModifytime(DateKit.toStr(new Date(),DateKit.timeStampPattern));
                model.update();
            }
            resultJson.setStatus(Constant.RESULT_SUCCESS);
            resultJson.setMsg("信息保存成功！");
        } catch (Exception e) {
            resultJson.setStatus(Constant.RESULT_SQL_ERROR);
            resultJson.setMsg("信息保存失败，数据库操作异常！");
            e.printStackTrace();
        }
        return resultJson;
    }

    @Override
    public Content getContentById(String id) {
        return Content.dao.findById(id);
    }

    @Override
    public List<Content> findContentList(String cid,boolean pic,int limit, String order) {
        Page<Content> page = null;
        if (pic == false){
            //list = Content.dao.find("select * from t_content t where t.cid = ? order by t.createtime desc",cid);
            page = Content.dao.paginate(1,limit,"select *","from t_content t where t.cid = ? order by t.createtime desc",cid);
        }else {
            //list = Content.dao.find("select * from t_content t where t.img is not null");
            page = Content.dao.paginate(1,limit,"select *","from t_content t where t.cid = ? and t.img is not null order by t.createtime desc",cid);
        }

        return page.getList();
    }

    @Override
    public Page<Content> findPage(String cid,int pages,int rows) {
        String select = "select *";
        String sqlexcptSelect = "from t_content t where t.status = 'on' and t.cid = ?";
        String order = " order by t.createtime desc";
        Page<Content> page = Content.dao.paginate(pages,rows,select,sqlexcptSelect + order,cid);
        return page;
    }

    @Override
    public List<Content> good(int limit) {
        String select = "select *";
        String sqlexcptSelect = "from t_content t where t.good = 'on'";
        String order = " order by t.createtime desc";
        Page<Content> page = Content.dao.paginate(1,limit,select,sqlexcptSelect + order);
        return page.getList();
    }
}
