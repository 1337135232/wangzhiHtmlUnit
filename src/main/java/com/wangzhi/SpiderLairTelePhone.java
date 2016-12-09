package com.wangzhi;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SpiderLairTelePhone {
	private static long sleep_time_per_page = 3000;
	private static int errorCount = 2;
	private static String code = "utf-8";
	private static String url = "http://www.baidu.com";
	private static String regex = "[^\u4e00-\u9fa5]";
	public static String listRegex_BaiDu = "data-tools='\\{\"title\":\"(.*?)\",\"url\":\"(.*?)\"\\}'><a";
	
	public static HashMap<String,String> lairWord = new HashMap<String,String>() ;
	static {             
		lairWord.put("金融理财 ", "");
		lairWord.put("借款", "");
		lairWord.put("贷款", "");
		lairWord.put("放款", "");
		lairWord.put("急用钱", "");
		lairWord.put("分期", "");
		lairWord.put("推销", "");
		lairWord.put("骚扰", "");
		lairWord.put("诈骗", "");
		lairWord.put("骗子", "");
		lairWord.put("传销", "");
		lairWord.put("淘宝刷单", "");
		lairWord.put("套现", "");
	}
	
	public static void main(String[] args) {
		String pageContent = parserPageContent("15170748297", 0, "", 1);
		String content = pageContent.replaceAll(regex, "");
		System.out.println(lairWord);
		for(String word:lairWord.keySet()){
			if(content.contains(word)){
//				System.out.println(word);
				System.out.println("this user is a lair");
				return;
			}
		}
//		ArrayList<String> paramsList = RegexParser.searchStr(pageContent, listRegex_BaiDu, 2, "@#");
//		for(String params:paramsList){
//			System.out.println(params.split("@#")[0]);
//		}
	}
	
	/**
	 * 通过百度关建词，及传入的页数（页数*10）
	 * 
	 * @param keyword
	 * @param size
	 * @return
	 */
	public static String parserPageContent(String companyName,int size,String ip,int port){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
//			String cookie = UrlRequest.getWebsiteCookieByProxy(url,ip,port);
			String cookie = UrlRequest.getWebsiteCookie(url);
			String url = "https://www.baidu.com/s?wd="+ URLEncoder.encode(companyName, "utf-8") + "&pn=" + size;
			String pageContent = UrlRequest.getContent(url, code, cookie, httpClient, sleep_time_per_page, errorCount);
			if(pageContent.contains("<head><title>404 Not Found</title></head>")){
				return "";
			}
			return pageContent;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
	
}
