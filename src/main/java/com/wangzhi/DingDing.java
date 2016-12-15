package com.wangzhi;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.http.ParseException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class DingDing implements Serializable{
	private static long sleep_time_per_page = 3000;
	private static int errorCount = 2;
	private static String code = "utf-8";
//	private static String url = "wss://webalfa.dingtalk.com/";
	private static String url = "https://im.dingtalk.com/";
	public static void main(String[] args) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HashMap<String, String> headParam = new HashMap<String, String> ();
//		headParam.put("Origin", "https://im.dingtalk.com");
//		headParam.put("Sec-WebSocket-Extensions", "permessage-deflate; client_max_window_bits");
//		headParam.put("Sec-WebSocket-Key", "gZN4zaeNy5Qsg3VXQC1KnA==");
//		headParam.put("Sec-WebSocket-Version", "13");
//		headParam.put("Upgrade", "websocket");
//		headParam.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
		try {
			String pageContent = UrlRequest.getContent(url, code, headParam, httpClient, sleep_time_per_page, errorCount);
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
		//Wy3SJwJpMKHk6ThBYHENLg==
		//WEds9EG+ZcMC03BePXfDzw==
		//gZN4zaeNy5Qsg3VXQC1KnA==
	}
}
