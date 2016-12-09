package com.wangzhi;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.ParseException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Test{
	
	private static long sleep_time_per_page = 3000;
	private static int errorCount = 2;
	private static String code = "utf-8";
	private static String Cookie = "qs_lvt_1742=1477904108,1477904483,1477966059,1477966400,1477968045; qs_pv_1742=1918620117101404600,2947781210881835500,608893113076387300,236256583803292600,11080541024634012; route=60cd899e37616d32b077390b1a32217d; apps=jmbmgtweb; JSESSIONID=EAB37DBD0845BC396BFB694AEA23641C.sso191; _ga=GA1.2.2048125683.1477904041; _gat=1; Hm_lvt_586f9b4035e5997f77635b13cc04984c=1477904041,1477966052; Hm_lpvt_586f9b4035e5997f77635b13cc04984c=1477968045";
	private static String url_JMB = "https://sso.jingoal.com/oauth/authorize?client_id=jmbmgtweb&response_type=code&state=%7Baccess_count%3A1%7D&locale=zh_CN&redirect_uri=https%3A%2F%2Fweb.jingoal.com%2F";
	private static String url_base = "https://web.jingoal.com/mgt/workbench/v1/get_userconfig?code=rE4itV&ouri=https%3A%2F%2Fweb.jingoal.com%2F&b1477891981123=1";
//	private static String url_base = "https://web.jingoal.com/mgt/workbench/v1/get_userconfig?code=FKiTw3&ouri=https%3A%2F%2Fweb.jingoal.com%2F&b1477894258443=1";
//	code不同
	
//	{"error":"BadCredentials","username":"18612065527","showIdentifyCode":false} 错误
	public static void main(String[] args) {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HashMap<String,String> resultMap = new HashMap<String,String>();
		HashMap<String, String> headParam = new HashMap<String, String> ();
		String cookie = firstBeforeStep(httpClient,resultMap);
//		System.out.println("===========");
//		System.out.println(cookie);
//		resultMap.clear();
		headParam.put("Cookie", cookie);
		resultMap.put("CookieBef", cookie);
		String result_First = firstStep(httpClient,resultMap,headParam);
		if(result_First.equals("{\"ok\":true}")){
			secondStep(httpClient,resultMap,headParam);
		}
	}
	
	private static String firstBeforeStep(CloseableHttpClient httpClient,HashMap<String,String> resultMap){
		String url = "https://sso.jingoal.com/oauth/authorize?client_id=jmbmgtweb&response_type=code&state=%7Baccess_count%3A1%7D&locale=zh_CN&redirect_uri=https%3A%2F%2Fweb.jingoal.com%2F#/login";
		try {
			String cookie = UrlRequest.getWebsiteCookie(url);
			return cookie;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	//请求第一步  得到结果 {"ok":true}
	private static String firstStep(CloseableHttpClient httpClient,HashMap<String,String> resultMap,HashMap<String, String> headParam){
		String url = "https://sso.jingoal.com/oauth/authorize?client_id=jmbmgtweb&response_type=code&state=%7Baccess_count%3A1%7D&locale=zh_CN&redirect_uri=https%3A%2F%2Fweb.jingoal.com%2F";
		HashMap<String, String> postValue = new  HashMap<String, String>();
		postValue.put("login_type", "default");
		postValue.put("username", "18612065527");
		postValue.put("password", "7c222fb2927d828af22f592134e8932480637c0d");
		postValue.put("identify", "");
		try {
			resultMap = RequestURL.postContent(url, postValue, headParam,resultMap, httpClient);
			String pageContent = resultMap.get("pageContent");
			System.out.println(pageContent);
			return pageContent;
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
		return "";
	}
	//请求第二步 获取location
	private static void secondStep(CloseableHttpClient httpClient,HashMap<String,String> resultMap,HashMap<String, String> headParam){
		String url = "https://sso.jingoal.com/oauth/authorize?client_id=jmbmgtweb&response_type=code&state=%7Baccess_count%3A1%7D&locale=zh_CN&redirect_uri=https%3A%2F%2Fweb.jingoal.com%2F";
		String cookie = resultMap.get("Set-Cookie");
		String CookieBef = resultMap.get("CookieBef");
		
		cookie = RequestURL.getCookie(cookie);
		System.out.println(cookie);
		System.out.println(CookieBef);
		HashMap<String,String> strMap = RequestURL.stringToMap(cookie, ";","=");
		HashMap<String,String> strMap2 = RequestURL.stringToMap(CookieBef, ";","=");
		
		strMap2.put(" JSESSIONID",strMap.get("JSESSIONID"));
		cookie = RequestURL.mapToString(strMap2);
		System.out.println("================");
		System.out.println(cookie);
//		headParam.put("Accept","text/html, application/xhtml+xml, image/jxr, */*");
//		headParam.put("Accept-Language","zh-Hans-CN, zh-Hans; q=0.8, en-US; q=0.5, en; q=0.3");
//		headParam.put("Host","sso.jingoal.com");
		headParam.put("Cookie", cookie);
//		headParam.put("Referer","https://sso.jingoal.com/oauth/authorize?client_id=jmbmgtweb&response_type=code&state=%7Baccess_count%3A1%7D&locale=zh_CN&redirect_uri=https%3A%2F%2Fweb.jingoal.com%2F");
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