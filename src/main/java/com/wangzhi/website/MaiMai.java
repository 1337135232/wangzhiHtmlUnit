package com.wangzhi.website;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.util.RegexParser;
import com.util.UnicodeConverter;
import com.wangzhi.RequestURL;

public class MaiMai {
	
	private static long sleep_time_per_page = 3000;
	private static int errorCount = 2;
	private static String code = "utf-8";
	private static final String url_login = "https://maimai.cn/login";
	private static final String url_contacts = "https://maimai.cn/contact/inapp_dist1_list";
	public static void main(String[] args) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HashMap<String,String> resultMap = new HashMap<String,String>();
		HashMap<String, String> headParam = new HashMap<String, String> ();
		headParam.put("code", code);
		HashMap<String, String> postValue = new  HashMap<String, String>();
		postValue.put("m","13621262240");
		postValue.put("p","00000000");
		postValue.put("to","");
		postValue.put("pa","+86");
		resultMap = commonPostStep(url_login,httpClient, headParam, postValue, resultMap);
		if(resultMap.containsKey("location")){
			String location = resultMap.get("location");
			if(location.equals("http://maimai.cn/feed_list")){
				System.out.println("login success");
				resultMap = commonGetStep(url_contacts,httpClient, headParam, resultMap);
				String pageContent = resultMap.get("pageContent");
				pageContent = UnicodeConverter.decodeUnicode(pageContent);
				String regex = "\"id\":([\\s\\S]*?),";
				ArrayList<String> id_list = RegexParser.searchStr(pageContent, regex);
				System.out.println(id_list);
				for(int i=0;i<id_list.size();i++){
					String id = id_list.get(i);
					String url_detail = "https://maimai.cn/contact/detail/"+id+"?from=webview%23%2Fcontact%2Finapp_dist1_list";
					resultMap = commonGetStep(url_detail, httpClient, headParam, resultMap);
					pageContent = resultMap.get("pageContent");
					pageContent = UnicodeConverter.decodeUnicode(pageContent);
					String name = RegexParser.getPageByRegex(pageContent, "\"name\":([\\s\\S]*?),");
					System.out.println(pageContent);
					System.out.println(name);
				}
			}
		}
	}
	
	public static HashMap<String,String> commonPostStep(String url,CloseableHttpClient httpClient,HashMap<String, String> headParam,HashMap<String, String> postValue,HashMap<String,String> resultMap){
		try {
			return RequestURL.postContent(url, postValue, headParam,resultMap, httpClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	public static HashMap<String,String> commonGetStep(String url,CloseableHttpClient httpClient,HashMap<String, String> headParam,HashMap<String,String> resultMap){
		try {
			return RequestURL.getContent(url, headParam, resultMap, httpClient, sleep_time_per_page, errorCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
}
