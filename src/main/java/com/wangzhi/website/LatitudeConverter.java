package com.wangzhi.website;

import com.util.HttpRequest;
import com.util.RegexParser;

/**
 * 经纬度转化为地址
 * @author wangzhi
 *
 */
public class LatitudeConverter {
	
	private static final String URL_PREFIX = "http://api.map.baidu.com/geocoder/v2/?ak=7qxrBYlMecGbVswWcKQGGw6K1Ip1vk23&callback=renderReverse&location=";
	private static final String URL_SUFFIX = "&output=json&pois=1";
	private static final String REGEX_ADCODE = "\"adcode\":\"(.*?)\"";
	public static void main(String[] args) {
		String adcode = convert("39.983424","116.322987");
		System.out.println(adcode);
	}
	
	private static String convert(String latitude,String longitude){
		String url = URL_PREFIX+latitude+","+longitude+URL_SUFFIX;
		HttpRequest request = new HttpRequest();
		String result = request.get(url);
		return RegexParser.getPageByRegex(result, REGEX_ADCODE);
	}
}
