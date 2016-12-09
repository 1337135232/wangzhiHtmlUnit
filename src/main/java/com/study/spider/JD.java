package com.study.spider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;

/**
 * 如果图片存入c盘报拒绝访问，请换一个盘，比如D盘
 * @author 正合奇胜
 *
 */

public class JD {

	private static final String login_host = "passport.jd.com";
	private static final String login_url_page = "https://passport.jd.com/uc/login";
	private static final CloseableHttpClient httpClient = HttpClients
			.createDefault();
	private static HashMap<String, String> params = new HashMap<String, String>();
	private static String authcode = "";

    public JD(String account, String password) {
        params.put("username", account);
        params.put("password", password);
    }	
	
	/**
	 * 登录京东
	 */
	public void login() {
		System.out.println("开始登录京东网站");
		// ************************第一步访问登录页面***************************
		String page = httpGet();
		params = getParam(page);
		//验证码第一种情况 打开登录页面直接出现验证码
		ArrayList<String> vcodeList = search(page, "id=\"JD_Verification1\" class=\"(.*?)\"");
		if(!vcodeList.isEmpty()){
			String vcode = vcodeList.get(0);
			//判断是否出现登录验证码
			if(!vcode.equals(" ")){
				System.out.println("打开登录页面,登录验证码出现了");
				String uid = params.get("uuid");
	            String get_code_url = "https://authcode.jd.com/verify/image?a=1&acid=" + uid + "&uid=" + uid + "&yys=" + System.currentTimeMillis();
	            String imageName = "C:/captcha_" + System.currentTimeMillis() + ".jpg";
	            httpGet(get_code_url, imageName);
	            System.out.println("获取验证码成功，路径为"+imageName+"     请输入验证码");
	            Scanner sc = new Scanner(System.in);
	            authcode = sc.nextLine();
			}
		}
		//登录URL
        String login_url = "https://passport.jd.com/uc/loginService?uuid=" + params.get("uuid") + "&&r=" + Math.random() + "&version=2015";
        String page2 = httpPost(login_url, params);
      //({"success":"http://www.jd.com"})
        page2 = page2.substring(1, (page2.length() - 1));
        JSONObject login_res = JSONObject.fromObject(page2);
        System.out.print("京东登录返回结果:" + login_res.toString());
		
      //验证码出现了
        String emptyAuthcode = "验证码出现";
        if (login_res.containsKey("emptyAuthcode")) {
            emptyAuthcode = login_res.getString("emptyAuthcode");
        } else {
            //出现短信验证码
            if (login_res.containsKey("venture")) {
                System.out.print("京东登录出现短信验证码");
            }
        }
        //验证码第二种情况 打开登录页面没有出现，点击登录时出现，此时参数_t 会变化，所以需要从新加载登录页面 从新获取相关参数
        if ("".equals(emptyAuthcode)) {
        	System.out.print("=============================点击登录时,登录验证码出现了===========================");
            String uid = params.get("uuid");
            String get_code_url = "https://authcode.jd.com/verify/image?a=1&acid=" + uid + "&uid=" + uid + "&yys=" + System.currentTimeMillis();
            String imageName = "C:/captcha_" + System.currentTimeMillis() + ".jpg";    //任务id + 时间戳
            httpGet(get_code_url, imageName);
            System.out.println("获取验证码成功，路径为"+imageName+"     请输入验证码");
            Scanner sc = new Scanner(System.in);
            authcode = sc.nextLine();
            String page3 = httpGet();
    		params = getParam(page3);
            //第二次登陆
            login_url = "https://passport.jd.com/uc/loginService?uuid=" + params.get("uuid") + "&&r=" + Math.random() + "&version=2015";
            page2 = httpPost(login_url, params);
          //({"success":"http://www.jd.com"})
            page2 = page2.substring(1, (page2.length() - 1));
            login_res = JSONObject.fromObject(page2);
            System.out.print("京东登录返回结果:" + login_res.toString());

            //验证码出现了
            if (login_res.containsKey("emptyAuthcode")) {
                emptyAuthcode = login_res.getString("emptyAuthcode");
            } else {
                if (login_res.containsKey("venture")) {
                }
            }
        }
        if (login_res.containsKey("emptyAuthcode")) {
            System.out.println("获取登录页面信息:【" + params.get("username") + "/" + params.get("password") + "】.验证码错误!" + login_res.getString("emptyAuthcode"));
        } else if (login_res.containsKey("pwd")) {
            System.out.println("获取登录页面信息:【" + params.get("username") + "/" + params.get("password") + "】.用户名密码出错!" + login_res.getString("pwd"));
        } else if (login_res.containsKey("username")) {
            System.out.println("获取登录页面信息:【" + params.get("username") + "/" + params.get("password") + "】.登录失败!" + login_res.getString("username"));
        }
        System.out.print("登录返回：" + login_res.toString());
        if (login_res.containsKey("success")) {
            System.out.println("获取登录页面信息:【" + params.get("username") + "/" + params.get("password") + "】.登录成功!");
        }
	}

	
	/**
	 * 获取网站中隐藏的参数
	 * @param input
	 */
	public HashMap<String, String> getParam(String input) {
		String regex = "<input type=\"hidden\".*?name=\"(.*?)\".*? value=\"(.*?)\".*?/>";
		ArrayList<String> list =search(input, regex, 2, "@~");
		System.out.println(list);
		for (String str : list) {
			String[] hidden_input = null;
			try {
				hidden_input = str.split("@~");
				String name = hidden_input[0];
				String value = hidden_input[1];
				params.put(name, value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(Arrays.toString(hidden_input));
				e.printStackTrace();
			}
		}
		return params;
	}

	/**
	 * post请求
	 * @return
	 */
	public String httpPost(String url,HashMap<String, String> params){
		
		HttpPost post = new HttpPost(url);
		post.setHeader("Accept", "text/plain, */*; q=0.01");
        post.setHeader("Accept-Encoding", "gzip, deflate, br");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        post.setHeader("Connection", "keep-alive");
//            post.setHeader("Cookie",params.get("login_page_cookie"));
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        
        List<NameValuePair> login_nvps = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            if ("username".equals(key) || "password".equals(key) || "eid".equals(key) || "fp".equals(key)) {
                continue;
            }
            login_nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        login_nvps.add(new BasicNameValuePair("loginname", params.get("username")));
        login_nvps.add(new BasicNameValuePair("loginpwd", params.get("password")));
        login_nvps.add(new BasicNameValuePair("nloginpwd", params.get("password")));
        login_nvps.add(new BasicNameValuePair("authcode", authcode));
        login_nvps.add(new BasicNameValuePair("eid", ""));
        login_nvps.add(new BasicNameValuePair("fp", ""));
        
        CloseableHttpResponse response = null;
        try {
			post.setEntity(new UrlEncodedFormEntity(login_nvps, "UTF-8"));
			response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			String page = EntityUtils.toString(entity);
			return page;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		return null;
	}
	
	/**
	 * get请求
	 */
	public String httpGet() {
		HttpGet get = new HttpGet(login_url_page);
		get.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		get.setHeader("Accept-Encoding", "gzip, deflate, br");
		get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		get.setHeader("Connection", "keep-alive");
		get.setHeader("Host", login_host);
		get.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			String page = EntityUtils.toString(entity);
			return page;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * get请求获取验证码
	 */
	public String httpGet(String url,String path) {
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			byte[] data = EntityUtils.toByteArray(entity);
			FileUtils.writeByteArrayToFile(new File(path), data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		return null;
	}


	//解析
	public ArrayList<String> search(String input,String regex){
		return search(input, regex, 1, "");
	}
	public ArrayList<String> search(String input, String regex, int size,
			String split) {
		ArrayList<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			StringBuffer str = new StringBuffer();
			for (int i = 1; i <= size ; i++) {
				String s = matcher.group(i).trim();
				if(s.isEmpty()){
					s = " ";
				}
				if(i==size){
					str.append(s);
				}else{
					str.append(s).append(split);
				}
			}
			list.add(str.toString());
		}
		return list;
	}
	public static void main(String[] args) {
		JD jd = new JD("your username","your password");
		jd.login();
	}

}
