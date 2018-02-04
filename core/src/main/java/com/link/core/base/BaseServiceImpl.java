package com.link.core.base;

import com.jfinal.aop.Before;
import com.jfinal.json.FastJson;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.link.api.service.base.BaseServiceI;
import com.link.common.kit.TreeKit;
import com.link.common.util.*;
import com.link.model.Channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by linkzz on 2017-05-26.
 */
public class BaseServiceImpl implements BaseServiceI{
    /**
     * @Description: 通用的jqGrid增删改
     * @author: linkzz
     * @data: 2017-06-02 11:53
    */
    @Before(Tx.class)
    public ResultJson saveOrUpdate(Model t, String id, String table, JqGrid jqGrid) {
        ResultJson result = new ResultJson();
        try {
            String sql = "select * from "+table+" t where t.id=?";
            if (jqGrid.getOper().equals(Constant.DEL)){
                //批量删除
                List ids = Arrays.asList(id.split(","));
                if (ids != null &&ids.size() > 0){
                    for (int i = 0;i < ids.size();i++){
                       Record record = Db.findFirst(sql,ids.get(i));
                       Db.delete(table,record);
                    }
                }
            }else if (jqGrid.getOper().equals(Constant.EDIT)){
                Record record = Db.findFirst(sql,id);
                if (record != null){
                    t.update();
                }
            }else {
                t.set("id",StrKit.getRandomUUID());
                t.save();
            }
            result.setStatus(Constant.RESULT_SUCCESS);
            result.setMsg("操作成功！");
        } catch (Exception e) {
            result.setStatus(Constant.RESULT_SQL_ERROR);
            result.setMsg("数据库操作异常！");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Channel> treeChannel() {
        List<Channel> list = Channel.dao.find("select * from t_channel");
        return list;
    }

    /**
     * @Description: 通用的jqGrid查询
     * @author: linkzz
     * @data: 2017-06-02 11:53
    */
    public DataGrid dataGrid(JqGrid jqGrid, Model t, String table) {
        DataGrid dataGrid = new DataGrid();
        String select = "select * ";
        String sqlexcptSelect = "from " + table + " t";
        String sidx = "id";
        String sord = "desc";
        Page<Record> page = null;

        Criterion criterion = new Criterion();
        //判断排序字段
        if (!StrKit.isBlank(jqGrid.getSidx())){
            sidx = jqGrid.getSidx();
        }
        //判断排序方式
        if (!StrKit.isBlank(jqGrid.getSord())){
            sord = jqGrid.getSord();
        }
        //组装排序语句
        String order = " order by t." + sidx + " " + sord;
        String criteriaString = "";
        //判断搜索是否为空
        criteriaString = this.installcriteriaString(jqGrid,criterion);
        //判断搜索条件是否为空
        if (StrKit.isBlank(criteriaString)){
            //进行简单查询
            page = Db.paginate(jqGrid.getPage(),jqGrid.getRows(),select,sqlexcptSelect + order);
        }else {
            //进行高级查询
            List params = new ArrayList();
            params = this.installParams(jqGrid,params,criterion);
            page = Db.paginate(jqGrid.getPage(),jqGrid.getRows(),select,sqlexcptSelect + " where t." + criteriaString + order,params.toArray());
        }

        dataGrid.setRows(page.getList());
        dataGrid.setRecords(page.getTotalRow());
        dataGrid.setTotal(page.getTotalPage());
        dataGrid.setPage(jqGrid.getPage());
        return dataGrid;
    }

    /**
     * @Description: 通用的treejqGrid查询
     * @author: linkzz
     * @data: 2017-06-02 11:53
     */
    public DataGrid treeDataGrid(JqGrid jqGrid, Model t, String table) {
        DataGrid dataGrid = new DataGrid();
        String select = "select * ";
        String sqlexcptSelect = "FROM "+table+" T ";
        //String sqlTree = " START WITH T.ID IN (SELECT id FROM "+table+" M WHERE M.PID is null) CONNECT BY PRIOR ID = T.PID";
        String sidx = "id";
        String sord = "desc";
        Page<Record> page = null;

        Criterion criterion = new Criterion();
        //判断排序字段
        if (!StrKit.isBlank(jqGrid.getSidx())){
            sidx = jqGrid.getSidx();
        }
        //判断排序方式
        if (!StrKit.isBlank(jqGrid.getSord())){
            sord = jqGrid.getSord();
        }
        //组装排序语句
        String order = " order by t." + sidx + " " + sord;
        String criteriaString = "";
        //判断搜索是否为空
        criteriaString = this.installcriteriaString(jqGrid,criterion);
        //判断搜索条件是否为空
        if (StrKit.isBlank(criteriaString)){
            //进行简单查询
            page = Db.paginate(jqGrid.getPage(),jqGrid.getRows(),select,sqlexcptSelect);
        }else {
            //进行高级查询
            List params = new ArrayList();
            params = this.installParams(jqGrid,params,criterion);
            page = Db.paginate(jqGrid.getPage(),jqGrid.getRows(),select,sqlexcptSelect + " where t." + criteriaString,params.toArray());
        }
        TreeKit listkit = new TreeKit(page.getList());
        dataGrid.setRows(listkit.startSorting());
        dataGrid.setRecords(page.getTotalRow());
        dataGrid.setTotal(page.getTotalPage());
        dataGrid.setPage(jqGrid.getPage());
        return dataGrid;
    }

    /**
     * @Description: 组装查询条件
     * @author: linkzz
     * @data: 2017-06-06 18:00
    */
    private String installcriteriaString(JqGrid jqGrid,Criterion criterion){
        List<Criterion> criteria = Collections.emptyList();
        String criteriaString = "";
        //判断搜索是否为空
        if (Boolean.parseBoolean(jqGrid.get_search())){
            //判断是否为高级查询
            if (!StrKit.isBlank(jqGrid.getFilters())){
                // 组装高级查询条件
                FastJson fastJson = FastJson.getJson();
                Filters filters = fastJson.parse(jqGrid.getFilters(),Filters.class);
                criteria = criterion.generateSearchCriteriaFromFilters(filters);
                criteriaString = Criterion.convertToSql(criteria, filters.getGroupOp(),criteriaString);
            }else {
                // 单条件查询
                String searchField = jqGrid.getSearchField();
                String searchOper = jqGrid.getSearchOper();
                String searchString = jqGrid.getSearchString();
                // 通过searchField、searchOper、searchString生成通用的查询条件
                Criterion cn = criterion.generateSearchCriterion(searchField, searchString, searchOper);
                if (cn != null) {
                    criteria.add(cn);
                }
                criteriaString = Criterion.convertToSql(criteria, "AND",criteriaString);
            }
        }
        return criteriaString;
    }

    /**
     * @Description: 组装查询参数
     * @author: linkzz
     * @data: 2017-06-06 18:03
    */
    private List installParams(JqGrid jqGrid,List params,Criterion criterion){
        if (!StrKit.isBlank(jqGrid.getFilters())){
            //开始组装高级查询参数
            FastJson fastJson = FastJson.getJson();
            Filters filters = fastJson.parse(jqGrid.getFilters(),Filters.class);
            List<Rules> rules = filters.getRules();
            for (Rules rule : rules){
                String field = rule.getField();
                String op = rule.getOp();
                String data = rule.getData();
                params = criterion.getParams(op,field,data,params);
            }
        }else {
            //组装简单查询参数
            params = criterion.getParams(jqGrid.getSearchOper(),jqGrid.getSearchField(),jqGrid.getSearchString(),params);
        }
        return params;
    }
}
