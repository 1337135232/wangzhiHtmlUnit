package com.wangzhi;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.ParseException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SpiderBaidu {
	private static long sleep_time_per_page = 3000;
	private static int errorCount = 2;
	private static String code = "utf-8";
	private static String url = "http://www.baidu.com";
	public static void main(String[] args) {
		int num = 0;
		String filePath = "";
		if(args.length!=2){
			System.out.println("please input two params,like:100   /usr/webpage");
			return;
		}else{
			try {
				num = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("the first param is a num");
				return;
			}
			filePath = args[1];
		}
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HashMap<String, String> headParam = new HashMap<String, String>();
		for(int i=0;i<num;i++){
			try {
				Thread.sleep(3000);
				String page = UrlRequest.getContent(url, code, headParam, httpClient, sleep_time_per_page, errorCount);
				FileUtil.writeToFile(filePath, String.valueOf(i+1), page, false);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("over over over over");
	}
}
