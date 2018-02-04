package com.link.webMagic;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.druid.DruidPlugin;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

//TargetUrl的意思是只有以下格式的URL才会被抽取出生成model对象
//这里对正则做了一点改动，'.'默认是不需要转义的，而'*'则会自动被替换成'.*'，因为这样描述URL看着舒服一点...
//继承jfinal中的Model
//实现AfterExtractor接口可以在填充属性后进行其他操作
@TargetUrl("http://www.jfinal.com/share/*")
public class JfinalWebMagic extends Model<JfinalWebMagic> implements AfterExtractor {
    //用ExtractBy注解的字段会被自动抽取并填充

    //默认是xpath语法
    @ExtractBy(value = "h1.jf-article-title",type = ExtractBy.Type.Css)
    private String title;

    //可以定义抽取语法为Css、Regex等
    @ExtractBy(value = "div.jf-article-content", type = ExtractBy.Type.Css)
    private String content;


    @Override
    public void afterProcess(Page page) {
        //jfinal 的属性其实是一个map而不是字段，没关系，填充进去就是了
        this.set("id", StrKit.getRandomUUID());
        this.set("title",title);
        this.set("content",content);
        save();
    }

    public static void main(String[] args){
        String url = "jdbc:mysql://localhost:3306/linkup?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
        String username = "root";
        String password = "123456";
        DruidPlugin druidPlugin = new DruidPlugin(url,username,password);
        druidPlugin.start();
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.addMapping("t_blog",JfinalWebMagic.class);
        arp.start();
        OOSpider.create(Site.me().setRetryTimes(3).setSleepTime(0).setTimeOut(3000),JfinalWebMagic.class).addUrl("http://www.jfinal.com/share/").run();
    }
}
