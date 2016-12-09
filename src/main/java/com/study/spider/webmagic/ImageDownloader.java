package com.study.spider.webmagic;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import us.codecraft.webmagic.downloader.HttpClientDownloader;

public class ImageDownloader extends HttpClientDownloader{

	@Override
	protected String getContent(String charset, HttpResponse httpResponse)
			throws IOException {
		// TODO Auto-generated method stub
		byte[] imageByte = EntityUtils.toByteArray(httpResponse.getEntity());
		return ImageBase64Utils.GetImageStr(imageByte);
	}

	
}
