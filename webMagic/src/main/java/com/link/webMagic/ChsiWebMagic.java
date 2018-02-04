package com.link.webMagic;

import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.Set;
@TargetUrl("https://my.chsi.com.cn/archive/gdjy/xj/show.action")
public class ChsiWebMagic extends Model<ChsiWebMagic> implements AfterExtractor {
    public Site site = Site.me().setDomain("account.chsi.com.cn").setRetryTimes(3).setSleepTime(0).setTimeOut(3000);
    //用来存储cookie信息
    private Set<Cookie> cookies;

    //用ExtractBy注解的字段会被自动抽取并填充

    //默认是xpath语法
    @ExtractBy(value = "//table[@class='mb-table']/tbody/tr[1]/td[1]/text()")
    private String username;

    //可以定义抽取语法为Css、Regex等
    @ExtractBy(value = "//table[@class='mb-table']/tbody/tr[1]/td[2]/text()")
    private String sex;


    @Override
    public void afterProcess(Page page) {
        //jfinal 的属性其实是一个map而不是字段，没关系，填充进去就是了
        LogKit.info(page.getHtml().toString());
        this.set("id", StrKit.getRandomUUID());
        this.set("username",username);
        this.set("sex",sex);
        save();
    }

    //使用 selenium 来模拟用户的登录获取cookie信息
    public void login(String username,String password) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://account.chsi.com.cn/passport/login?service=https%3A%2F%2Fmy.chsi.com.cn%2Farchive%2Fj_spring_cas_security_check");

        driver.findElement(By.name("username")).clear();

        //在******中填你的用户名
        driver.findElement(By.name("username")).sendKeys(username);

        driver.findElement(By.name("password")).clear();
        //在*******填你密码
        driver.findElement(By.name("password")).sendKeys(password);

        //模拟点击登录按钮
        driver.findElement(By.className("btn_login_my")).click();

        //获取cookie信息
        cookies = driver.manage().getCookies();
        driver.close();
    }

    public Site getSite() {
        //将获取到的cookie信息添加到webmagic中
        for (Cookie cookie : cookies) {
            site.addCookie(cookie.getName().toString(),cookie.getValue().toString());
        }

        return site.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1");
    }
}
