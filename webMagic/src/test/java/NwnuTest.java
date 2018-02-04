import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.link.webMagic.NwnuWebMagic;
import us.codecraft.webmagic.model.OOSpider;

public class NwnuTest {

    public static void main(String[] args){
        String url = "jdbc:mysql://localhost:3306/linkup?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
        String username = "root";
        String password = "123456";
        DruidPlugin druidPlugin = new DruidPlugin(url,username,password);
        druidPlugin.start();
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.addMapping("t_blog",NwnuWebMagic.class);
        arp.start();

        NwnuWebMagic nwnu = new NwnuWebMagic();
        nwnu.login("********","*******");

        OOSpider.create(nwnu.getSite(),NwnuWebMagic.class).addUrl("http://www.jfinal.com/share/").run();
    }
}
