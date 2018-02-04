package com.link.cbrc.common;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Assign;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.stat.Scope;
import com.link.model.Content;

import java.io.Writer;
import java.util.List;

/**
 * Created by linkzz on 2017-08-25.
 */
public class ArticleListDirective extends Directive {
    Expr[] exprArray=null;

    @Override
    public void setExprList(ExprList exprList) {
        super.setExprList(exprList);
        this.exprArray = exprList.getExprArray();
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        //存放各个参数
        Kv kv=Kv.create();
        for (int i=0;i<exprArray.length;i++) {
            //循环参数列表，并把参数放到 Kv 里面
            Assign obj= (Assign)exprArray[i];
            //System.out.println(obj.id+"   "+obj.right.eval(scope));
            kv.set(obj.getId().toLowerCase(), obj.eval(scope));
        }
        //获取到的参数名和参数值 ，都在KV里面，该干啥干啥吧，这里省略了
        //根据上面得到的参数，自行组合SQL查询
        String select = "select * ";
        String sqlExceptSelect = "FROM t_content t where t.status = 'on' and t.cid = ?";
        Page<Content> page = Content.dao.paginate(kv.getInt("pagenumber"),kv.getInt("pagesize"),select,sqlExceptSelect,kv.getStr("cid"));
        List list=page.getList();

        for (int i=0;i<list.size();i++) {
            scope.set("article",list.get(i));
            stat.exec(env, scope, writer);//执行自定义标签中包围的 html
        }
    }

    public boolean hasEnd(){
        return true;
    }
}
