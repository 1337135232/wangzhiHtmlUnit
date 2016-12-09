package com.wangzhi;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.ParseException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Test1 {
	private static long sleep_time_per_page = 3000;
	private static int errorCount = 2;
	private static String code = "utf-8";
	public static void main(String[] args) {
		String url = "https://sso.jingoal.com/oauth/authorize?client_id=jmbmgtweb&response_type=code&state=%7Baccess_count%3A1%7D&locale=zh_CN&redirect_uri=https%3A%2F%2Fweb.jingoal.com%2F";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HashMap<String, String> resultMap = new HashMap<String, String>();
		HashMap<String, String> headParam = new HashMap<String, String>();
		headParam.put("Accept",
				"text/html, application/xhtml+xml, image/jxr, */*");
		headParam.put("Accept-Encoding", "gzip, deflate");
		headParam.put("Accept-Language",
				"zh-Hans-CN, zh-Hans; q=0.8, en-US; q=0.5, en; q=0.3");
		headParam.put("Connection", "Keep-Alive");
		headParam
				.put("Cookie",
						"route=dc5c3e9fa8227458899814effe094d83; qs_lvt_1742=1477998617,1477998653,1477998697,1477998752,1477998817; qs_pv_1742=2061080157859030200,3882470307142657000,367835954134277700,290284231524968440,3272399234861380600; apps=jmbmgtweb; JSESSIONID=B683E488D70DD38BD7083D66A1705027.sso110.30; Hm_lvt_586f9b4035e5997f77635b13cc04984c=1477998397; Hm_lpvt_586f9b4035e5997f77635b13cc04984c=1477998817; _ga=GA1.2.1921415596.1477998428; _gat=1");
		headParam
				.put("Referer",
						"https://sso.jingoal.com/oauth/authorize?client_id=jmbmgtweb&response_type=code&state=%7Baccess_count%3A1%7D&locale=zh_CN&redirect_uri=https%3A%2F%2Fweb.jingoal.com%2F");
		headParam
				.put("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
		try {
			String baseContent = UrlRequest.getContent(url, code, headParam, httpClient, sleep_time_per_page, errorCount);
			System.out.println(baseContent);
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
}
