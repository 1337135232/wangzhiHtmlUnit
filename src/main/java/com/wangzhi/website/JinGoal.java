package com.wangzhi.website;

import java.util.HashMap;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class JinGoal {
	private static long sleep_time_per_page = 3000;
	private static int errorCount = 2;
	private static String code = "utf-8";
	
	public static void main(String[] args) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HashMap<String,String> resultMap = new HashMap<String,String>();
		HashMap<String, String> headParam = new HashMap<String, String> ();
		String url_login = "https://sso.jingoal.com/#/login";
		resultMap = MaiMai.commonGetStep(url_login, httpClient, headParam, resultMap);
		System.out.println(resultMap.get("Set-Cookie"));
		String url_post = "https://sso.jingoal.com/oauth/authorize?client_id=jmbmgtweb&response_type=code&state=%7Baccess_count%3A1%7D&locale=zh_CN&redirect_uri=https%3A%2F%2Fweb.jingoal.com%2F";
		HashMap<String, String> postValue = new  HashMap<String, String>();
		postValue.put("login_type", "default");
		postValue.put("username", "18612065527");
		postValue.put("password", "7c222fb2927d828af22f592134e8932480637c0d");
		postValue.put("identify", "");
		
		headParam.put("Host", "sso.jingoal.com");
		headParam.put("Referer", url_post);
		headParam.put("Set-Cookie", resultMap.get("Set-Cookie"));
		headParam.put("Content-Language", "zh-CN");
		resultMap = MaiMai.commonPostStep(url_post, httpClient, headParam, postValue, resultMap);
		String pageContent = resultMap.get("pageContent");
		if(pageContent.equals("{\"ok\":true}")){
			resultMap = MaiMai.commonGetStep(url_post, httpClient, headParam, resultMap);
			resultMap.remove("pageContent");
			System.out.println(resultMap);
		}
	}
}
