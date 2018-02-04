package com.link.admin.common;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.tx.TxByActionKeyRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.link.admin.controller.IndexController;
import com.link.admin.controller.shiro.ShiroTag;
import com.link.common.plugin.shiro.ShiroInterceptor;
import com.link.common.plugin.shiro.ShiroPlugin;
import com.link.common.util.Constant;
import com.link.model._MappingKit;

/**
 * Created by linkzz on 2017-05-25.
 */
public class AdminConfig extends JFinalConfig {
    private static Routes routes;

    public static void main(String[] args){
        JFinal.start("src/main/webapp",8088,"/");
    }
    @Override
    public void configConstant(Constants constants) {
        PropKit.use("config.properties");
        constants.setDevMode(PropKit.getBoolean("devMode",true));
        constants.setViewType(ViewType.JFINAL_TEMPLATE);
        constants.setError404View(Constant.error404path);
        constants.setError500View(Constant.error500path);
        constants.setError401View(Constant.error401path);
        constants.setError403View(Constant.error403path);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", IndexController.class);
        me.add(new AdminRoutes());
        this.routes = me;
    }

    @Override
    public void configEngine(Engine me) {
        me.addSharedObject("shiro",new ShiroTag());
        me.addSharedFunction("/_view/common/_layout.html");
    }

    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbc_url"),PropKit.get("jdbc_username"),PropKit.get("jdbc_password"));
        druidPlugin.setValidationQuery(PropKit.get("validationQuery"));
        druidPlugin.setDriverClass(PropKit.get("driverClassName"));
        druidPlugin.addFilter(new StatFilter());
        WallFilter wallFilter = new WallFilter();
        wallFilter.setDbType("mysql");
        druidPlugin.addFilter(wallFilter);
        me.add(druidPlugin);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath()+"/sqlTemplate");
        arp.addSqlTemplate("user.sql");
        arp.setShowSql(true);
        arp.setDevMode(false);
        /*arp.setCache(new EhCache());*/
        //no table mapping 错误，缺少oracle方言,教训呀
        arp.setDialect(new MysqlDialect());
        //忽略oracle数据库字段大小写 false 是大写, true是小写, 不写是区分大小写
        arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
        //所有映射在MappingKit文件中
        _MappingKit.mapping(arp);
        me.add(arp);
        //加载shiroPlugin插件
        ShiroPlugin shiroPlugin = new ShiroPlugin(this.routes);
        shiroPlugin.setUnauthorizedUrl(Constant.error401path);
        me.add(shiroPlugin);
        /*Cron4jPlugin cron4jPlugin = new Cron4jPlugin();
        cron4jPlugin.addTask("*//*1 * * * * ",new MyTask());
        me.add(cron4jPlugin);*/
    }

    @Override
    public void configInterceptor(Interceptors me) {
        //添加控制层全局拦截器
        me.addGlobalActionInterceptor(new AuthInterceptor());
        //启动Shiro拦截器
        me.add(new ShiroInterceptor());
        //根据ActionkeyRegex正则表达式进行事务处理
        me.add(new TxByActionKeyRegex(".*trans.*|.*save.*|"));
        //根据ActionkeyRegex正则表达式进行异常和日志拦截处理
        me.addGlobalActionInterceptor(new ExceptionAndLogInterceptor(".*trans.*|.*save.*"));
        //session拦截器，用于在View模板中取出session值
        me.addGlobalActionInterceptor(new SessionInViewInterceptor(true));
    }

    @Override
    public void configHandler(Handlers me) {
        DruidStatViewHandler dvh =  new DruidStatViewHandler("druid");
        me.add(dvh);
        me.add(new ContextPathHandler("ctx"));
    }
}
