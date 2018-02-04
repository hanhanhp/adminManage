import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.link.webMagic.ChsiWebMagic;
import us.codecraft.webmagic.model.OOSpider;

public class ChsiTest {
    public static void main(String[] args){
        String url = "jdbc:mysql://localhost:3306/linkup?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
        String username = "root";
        String password = "123456";
        DruidPlugin druidPlugin = new DruidPlugin(url,username,password);
        druidPlugin.start();
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.addMapping("t_chsi",ChsiWebMagic.class);
        arp.start();

        ChsiWebMagic chsi = new ChsiWebMagic();
        chsi.login("muyi0623@163.com","22112991");

        OOSpider.create(chsi.getSite(),ChsiWebMagic.class).addUrl("https://my.chsi.com.cn/archive/gdjy/xj/show.action").run();
    }
}
