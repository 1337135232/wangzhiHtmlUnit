package com.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlUtil {

	/**
	 * 从头url中解析出主机名
	 * @param url
	 * @return
	 */
	public static String getHost(String url){
		int start = url.indexOf("//");
		if(start>0){
			url = url.substring(start+2);
		}
		int end = url.indexOf("/");
		if(end>0){
			url = url.substring(0, end);
		}
		end = url.indexOf(":");
		if(end>0){
			url = url.substring(0, end);
		}
		return url;
	}
	/**
	 * URL编码
	 * @param keyword
	 * @param encoding
	 * @return
	 */
	public static String encode(String keyword,String encoding){
		String result = keyword;
		try {
			result = URLEncoder.encode(keyword, encoding);
		} catch (UnsupportedEncodingException e1) {
			
		}
		return result;
	}
	/**
	 * URL解码
	 * @param keyword
	 * @param encoding
	 * @return
	 */
	public static String decode(String keyword,String encoding){
		String result = keyword;
		try {
			result = URLDecoder.decode(keyword, encoding);
		} catch (UnsupportedEncodingException e1) {
			
		}
		return result;
	}
	
	public static void main(String[] args){
		System.out.println(getHost("http://hc.apache.org/1.html"));
	}
}
