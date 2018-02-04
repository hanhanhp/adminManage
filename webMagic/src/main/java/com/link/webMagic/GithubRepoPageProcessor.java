package com.link.webMagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class GithubRepoPageProcessor implements PageProcessor,AfterExtractor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(http://www\\.gslzcredit\\.gov\\.cn/[\\w\\-]+/[\\w\\-]+\\.html)").all());
        page.addTargetRequests(page.getHtml().links().regex("(http://www\\.gslzcredit\\.gov\\.cn/[\\w\\-]+\\.html)").all());
        //page.putField("author", page.getUrl().regex("http://www\\.gslzcredit\\.gov\\.cn/(\\w+)/.*").toString());
        page.putField("title", page.getHtml().xpath("//p[@class='MsoPlainText']/span/font/text()").toString());
        if (page.getResultItems().get("title")==null){
            //skip this page
            page.setSkip(true);
        }
        page.putField("name", page.getHtml().xpath("//table[@class='MsoNormalTable']/tidyText()"));
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void afterProcess(Page page) {

    }

    public static void main(String[] args) {

        Spider.create(new GithubRepoPageProcessor()).addUrl("http://www.gslzcredit.gov.cn/65/").addPipeline(new JsonFilePipeline("D:\\data\\webmagic")).thread(5).run();
    }

}
