package com.link.front.common;

import com.jfinal.kit.Kv;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Assign;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.stat.Scope;
import com.link.model.Channel;

import java.io.Writer;
import java.util.List;

/**
 * Created by linkzz on 2017-09-01.
 */
public class ChannelListDirective extends Directive {
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
        List<Channel> list = Channel.dao.find("SELECT * FROM t_channel t WHERE t.type = 'true' AND t.hide = 'true' ORDER BY t.sorter");

        for (int i=0;i<list.size();i++) {
            scope.set("channel",list.get(i));
            stat.exec(env, scope, writer);//执行自定义标签中包围的 html
        }
    }

    public boolean hasEnd(){
        return true;
    }
}
