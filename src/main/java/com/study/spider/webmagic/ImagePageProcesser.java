package com.study.spider.webmagic;


import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class ImagePageProcesser implements PageProcessor{

	private Site site = Site.me().setCharset("utf-8").setRetryTimes(5).setSleepTime(2000);
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		String imageStr = page.getRawText();
        page.putField("imageStr", imageStr);
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new ImagePageProcesser()).
        addUrl("http://qyxy.baic.gov.cn/CheckCodeCaptcha?currentTimeMillis=1480041261503")
        .addPipeline(new ImageToLocalPipeline("F:/webmagic"))
        .setDownloader(new ImageDownloader())
        		.thread(1)
                .start();
	}
}
