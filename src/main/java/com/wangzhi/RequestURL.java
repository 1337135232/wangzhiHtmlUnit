package com.wangzhi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class RequestURL {
	
	private static String user_agent = "Mozilla/5.0 (Windows NT 6.1; rv:38.0) Gecko/20100101 Firefox/38.0";
	private static String accept_language = "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3";
//	private static String accept_with_image = "text/plain,text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/webp, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1";
	private static RequestConfig DEFAULT_HTTP_REQUEST_CONFIG = RequestConfig.custom().setSocketTimeout(1000 * 60)
			.setConnectTimeout(1000 * 60).setConnectionRequestTimeout(1000 * 60).build();// 缺省的HTTP请求配置
	
	public static HashMap<String,String> getContent(String url, HashMap<String, String> headParam,HashMap<String,String> resultMap, CloseableHttpClient httpClient,
			long sleep_time_per_page, int errorCount) throws InterruptedException, ParseException, IOException {
		CloseableHttpResponse response = null;
		String Code = headParam.containsKey("code")?headParam.get("code"):"utf-8";
		for (int i = 0; i < errorCount; i++) {
			try {
				Thread.sleep(sleep_time_per_page);
				long time1 = System.currentTimeMillis();
				HttpGet get = new HttpGet(url);
				get.setHeader("User-Agent", user_agent);
				get.setHeader("Accept-Language", accept_language);
				get.setConfig(DEFAULT_HTTP_REQUEST_CONFIG);
				
				if(headParam!=null){
					for (String s : headParam.keySet()) {
						get.setHeader(s, headParam.get(s));
					}
				}

				response = httpClient.execute(get);
				for(Header header:response.getAllHeaders()){
					System.out.println(header.getName()+"==="+header.getValue());
//					if(resultMap.containsKey(header.getName())){
//						String value = header.getValue()+"@#"+resultMap.get(header.getName());
//						resultMap.put(header.getName(), value);
//					}else{
//						resultMap.put(header.getName(), header.getValue());
//					}
					resultMap.put(header.getName(), header.getValue());
				}
				if (response.getStatusLine().getStatusCode() == 200) {
					String content = EntityUtils.toString(response.getEntity(), Code);

					long time2 = System.currentTimeMillis();
					System.out.println("\tGET页面耗时" + (time2 - time1) + "ms");

					resultMap.put("pageContent", content);
					return resultMap;
					
				} else if(response.getStatusLine().getStatusCode() == 302) {
					String locationUrl=response.getLastHeader("Location").getValue();
					resultMap.put("location", locationUrl);
					return resultMap;
				} else {
					System.out.println("状态码=" + response.getStatusLine().getStatusCode());
				}
			} finally {
				if (response != null) {
					try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return resultMap;
	}
	
	public static HashMap<String,String> postContent(String url, HashMap<String, String> postValue, HashMap<String, String> headParam,HashMap<String,String> resultMap,
			CloseableHttpClient httpClient) throws ClientProtocolException,
			IOException, InterruptedException {
		CloseableHttpResponse response = null;
		String Code = headParam.containsKey("code")?headParam.get("code"):"utf-8";
		for (int i = 0; i < 2; i++) {
			try {
				Thread.sleep(2700);
				long time1 = System.currentTimeMillis();
				HttpPost putPOST = new HttpPost(url);
				putPOST.setHeader("User-Agent", user_agent);
				putPOST.setHeader("Accept-Language", accept_language);
				putPOST.setHeader("Host", putPOST.getURI().getHost());
				putPOST.setHeader("Referer", url);
				if(headParam!=null){
					for (String s : headParam.keySet()) {
						putPOST.setHeader(s, headParam.get(s));
					}
				}
				putPOST.setConfig(DEFAULT_HTTP_REQUEST_CONFIG);
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (String s : postValue.keySet()) {
					list.add(new BasicNameValuePair(s, postValue.get(s).trim()));
				}

				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Code);
				putPOST.setEntity(entity);
				response = httpClient.execute(putPOST);
				for(Header header:response.getAllHeaders()){
//					System.out.println(header.getName()+"==="+header.getValue());
//					if(resultMap.containsKey(header.getName())){
//						String value = header.getValue()+"@#"+resultMap.get(header.getName());
//						resultMap.put(header.getName(), value);
//					}else{
//						resultMap.put(header.getName(), header.getValue());
//					}
					resultMap.put(header.getName(), header.getValue());
				}
				if (response.getStatusLine().getStatusCode() == 200) {
					String content = EntityUtils.toString(response.getEntity(), Code);

					long time2 = System.currentTimeMillis();
					System.out.println("POST打开页面耗时" + (time2 - time1) + "ms");
					resultMap.put("pageContent", content);
					return resultMap;
				} else if(response.getStatusLine().getStatusCode() == 302) {
					String locationUrl=response.getLastHeader("Location").getValue();
					resultMap.put("location", locationUrl);
					return resultMap;
				} else {
					System.out.println("状态码=" + response.getStatusLine().getStatusCode());
				}
			} finally {
				if (response != null) {
					try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return resultMap;
	}
	
	public static void main(String[] args) {
		String Cookie = "qs_lvt_1742=1477904108,1477904483,1477966059,1477966400,1477968045; qs_pv_1742=1918620117101404600,2947781210881835500,608893113076387300,236256583803292600,11080541024634012; route=60cd899e37616d32b077390b1a32217d; apps=jmbmgtweb; JSESSIONID=EAB37DBD0845BC396BFB694AEA23641C.sso191; _ga=GA1.2.2048125683.1477904041; _gat=1; Hm_lvt_586f9b4035e5997f77635b13cc04984c=1477904041,1477966052; Hm_lpvt_586f9b4035e5997f77635b13cc04984c=1477968045";
		mapToString(stringToMap(Cookie, ";","="));
	}
	public static String getCookie(String cookieStr){
		String cookie = "";
		String[] cookieArray = cookieStr.split("@#");
		for(int i=0;i<cookieArray.length;i++){
			cookie = cookie + cookieArray[i].split("Path=/")[0];
		}
		return cookie;
	}
	
	public static String mapToString(HashMap<String,String> stringMap){
		String str = "";
		for(String key:stringMap.keySet()){
			str += key+"="+stringMap.get(key)+";";
		}
		return str;
	}
	
	public static HashMap<String,String> stringToMap(String str,String ...s ){
		HashMap<String,String> strMap = new HashMap<String,String>();
		String[] strArray = str.split(s[0]);
		for(int i=0;i<strArray.length;i++){
			if(!strArray[i].equals(" ")){
				String[] cookieArr = strArray[i].split(s[1]);
				strMap.put(cookieArr[0], cookieArr[1]);
			}
		}
		return strMap;
	}
}
