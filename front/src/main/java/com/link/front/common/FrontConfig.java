package com.link.front.common;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.link.common.kit.StrKit;
import com.link.front.controller.IndexController;
import com.link.model._MappingKit;

/**
 * Created by linkzz on 2017-09-11.
 */
public class FrontConfig extends JFinalConfig {
    private static Routes routes;
    @Override
    public void configConstant(Constants constants) {
        PropKit.use("config.properties");
        constants.setDevMode(PropKit.getBoolean("devMode",true));
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", IndexController.class);
        me.add(new FrontRoutes());
        this.routes = me;
    }

    @Override
    public void configEngine(Engine me) {
        me.addSharedObject("host","http://localhost:8080");
        me.addSharedObject("sk",new StrKit());
        me.addDirective("articlelist", new ArticleListDirective());
        me.addDirective("channelList", new ChannelListDirective());
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
    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("ctx"));
    }
}
