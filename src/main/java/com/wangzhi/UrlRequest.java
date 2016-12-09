package com.wangzhi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class UrlRequest {

	// 默认HTTP参数
	private static String user_agent = "Mozilla/5.0 (Windows NT 6.1; rv:38.0) Gecko/20100101 Firefox/38.0";
	private static String accept_language = "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3";
	private static String accept_with_image = "text/plain,text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/webp, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1";
	private static RequestConfig DEFAULT_HTTP_REQUEST_CONFIG = RequestConfig.custom().setSocketTimeout(1000 * 60)
			.setConnectTimeout(1000 * 60).setConnectionRequestTimeout(1000 * 60).build();// 缺省的HTTP请求配置
	
	/**
	 * 获取代理的clien
	 * @param proxyHost
	 * @param proxyPort
	 * @return
	 * @author wanggzhi
	 * @date 2016年3月23日 下午10:12:20
	 */
	public static WebClient getWebClientByProxy( String proxyHost, int proxyPort) throws Exception {
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38,proxyHost,proxyPort);
		return webClient;
	}
	
	/**
	 * 获取默认的client
	 * @return
	 * @author wangzhi
	 * @date 2016年3月23日 下午10:12:06
	 */
	public static WebClient getWebClient() throws Exception {
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
		return webClient;
	}
	
	/**
	 * 配置属性
	 * @param webClient
	 * @return
	 * @author audaque
	 * @date 2016年3月23日 下午10:13:47
	 */
	public static WebClient getClient(WebClient webClient) throws Exception {
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setTimeout(30 * 1000);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.waitForBackgroundJavaScript(600*1000);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		return webClient;
	}
	
	/**
	 * 获取cookie
	 * @return
	 * @author wangzhi
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws FailingHttpStatusCodeException 
	 * @date 2016年3月23日 下午10:14:44
	 */
	public static String getWebSiteCookie(WebClient webClient,String url) throws Exception{
		Object page = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(10000);
		String cookiesStr = "";
		if (page instanceof HtmlPage) {
			// 获取cookie串
			CookieManager cm = webClient.getCookieManager();// __jsluid
															// __jsl_clearance
															// JSESSIONID
			StringBuilder cookieSB = new StringBuilder();
			Set<Cookie> cookies = cm.getCookies();
			for (Iterator<Cookie> it = cookies.iterator(); it.hasNext();) {
				Cookie c = it.next();
				cookieSB.append(c.getName()).append("=")
						.append(c.getValue()).append("; ");
			}
			cookiesStr = cookieSB.toString();
		}
		webClient.close();
		return cookiesStr;
	}
	/**
	 * 通过代理ip获取cookie
	 * @param url
	 * @return
	 * @throws Exception
	 * @author audaque
	 * @date 2016年3月7日 上午10:46:43
	 */
	public static String getWebsiteCookieByProxy(String url,String proxyHost, int proxyPort) throws Exception {
		WebClient webClient = getWebClientByProxy(proxyHost,proxyPort);
		webClient = getClient(webClient);
		return getWebSiteCookie(webClient,url);
		
	}
	/**
	 * 获取cookie
	 * @param url
	 * @return
	 * @throws Exception
	 * @author audaque
	 * @date 2016年3月7日 上午10:46:43
	 */
	public static String getWebsiteCookie(String url) throws Exception {
		WebClient webClient = getWebClient();
		webClient = getClient(webClient);
		return getWebSiteCookie(webClient,url);
	}
	/**
	 * 获取
	 * @param httpClient
	 * @param captureUrl
	 * @param urlPost
	 * @return
	 * @throws Exception
	 * @author audaque
	 * @date 2016年3月3日 下午2:50:23
	 */
	public static byte[] getCaptureContent(CloseableHttpClient httpClient,String captureUrl,String cookiesStr) throws Exception{
		byte[] captureContent = getContentByte(captureUrl, "utf-8", cookiesStr, httpClient,2700, 2);
		return captureContent;
	}
	/**
	 * 
	 * @param url
	 * @param postValue
	 *            POST构建参数
	 * @param Code
	 *            编码
	 * @param cookie
	 *            cookie
	 * @param httpClient
	 * @param sleep_time_per_page
	 *            间隔时间
	 * @param errorCount
	 *            循环错误次数
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws InterruptedException
	 */
	public static String postContent(String url, HashMap<String, String> postValue, String Code, String cookie,
			CloseableHttpClient httpClient, long sleep_time_per_page, int errorCount) throws ClientProtocolException,
			IOException, InterruptedException {
		CloseableHttpResponse response = null;
		for (int i = 0; i < errorCount; i++) {
			try {
				Thread.sleep(sleep_time_per_page);
				long time1 = System.currentTimeMillis();
				HttpPost putPOST = new HttpPost(url);
				putPOST.setHeader("User-Agent", user_agent);
				putPOST.setHeader("Accept-Language", accept_language);
				putPOST.setHeader("Host", putPOST.getURI().getHost());
				putPOST.setHeader("Referer", url);
				
				putPOST.setConfig(DEFAULT_HTTP_REQUEST_CONFIG);

				if (cookie != null && !cookie.equals("")) {
					putPOST.setHeader("Cookie", cookie);
				} else {
					// System.out.println("Cookie为空");
				}

				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (String s : postValue.keySet()) {
					list.add(new BasicNameValuePair(s, postValue.get(s)));
				}

				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Code);
				putPOST.setEntity(entity);
				response = httpClient.execute(putPOST);
//				String content1 = EntityUtils.toString(response.getEntity(),Charset.forName("UTF-8"));
//				System.out.println(content1);
				if (response.getStatusLine().getStatusCode() == 200) {
					String content = EntityUtils.toString(response.getEntity(), Code);

					long time2 = System.currentTimeMillis();
					System.out.println("POST打开页面耗时" + (time2 - time1) + "ms");

					return content;
				} else if(response.getStatusLine().getStatusCode() == 302) {
					String locationUrl=response.getLastHeader("Location").getValue();
					return locationUrl;
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
		return "";
	}
	
	public static String postContent(String url, HashMap<String, String> postValue, String Code, HashMap<String, String> headParam,
			CloseableHttpClient httpClient) throws ClientProtocolException,
			IOException, InterruptedException {
		CloseableHttpResponse response = null;
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
				if (response.getStatusLine().getStatusCode() == 200) {
					String content = EntityUtils.toString(response.getEntity(), Code);

					long time2 = System.currentTimeMillis();
					System.out.println("POST打开页面耗时" + (time2 - time1) + "ms");

					return content;
				} else if(response.getStatusLine().getStatusCode() == 302) {
					String locationUrl=response.getLastHeader("Location").getValue();
					return locationUrl;
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
		return "";
	}

	/**
	 * 
	 * @param url
	 * @param code
	 *            编码
	 * @param cookie
	 * @param httpClient
	 * @param sleep_time_per_page
	 *            间隔时间
	 * @param errorCount
	 *            循环错误次数
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String getContent(String url, String code, String cookie, CloseableHttpClient httpClient,
			long sleep_time_per_page, int errorCount) throws InterruptedException, ParseException, IOException {
		CloseableHttpResponse response = null;
		for (int i = 0; i < errorCount; i++) {
			try {
				Thread.sleep(sleep_time_per_page);
				long time1 = System.currentTimeMillis();
				HttpGet get = new HttpGet(url);
				get.setHeader("User-Agent", user_agent);
				get.setHeader("Accept-Language", accept_language);
				get.setHeader("Host", get.getURI().getHost());
				get.setHeader("Referer", url);
				get.setConfig(DEFAULT_HTTP_REQUEST_CONFIG);
				if (cookie != null && !cookie.equals("")) {
					get.setHeader("Cookie", cookie);
				} else {
					// System.out.println("Cookie为空");
				}

				response = httpClient.execute(get);
				if (response.getStatusLine().getStatusCode() == 200) {
					String content = EntityUtils.toString(response.getEntity(), code);

					long time2 = System.currentTimeMillis();
					System.out.println("\tGET页面耗时" + (time2 - time1) + "ms");

					return content;
					
				} else if(response.getStatusLine().getStatusCode() == 302) {
					String locationUrl=response.getLastHeader("Location").getValue();
					return locationUrl;
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
		return "";
	}
	
	public static String getContent(String url, String code, HashMap<String, String> headParam, CloseableHttpClient httpClient,
			long sleep_time_per_page, int errorCount) throws InterruptedException, ParseException, IOException {
		CloseableHttpResponse response = null;
		for (int i = 0; i < errorCount; i++) {
			try {
				Thread.sleep(sleep_time_per_page);
				long time1 = System.currentTimeMillis();
				HttpGet get = new HttpGet(url);
//				get.setHeader("User-Agent", user_agent);
//				get.setHeader("Accept-Language", accept_language);
				get.setConfig(DEFAULT_HTTP_REQUEST_CONFIG);
				
				if(headParam!=null){
					for (String s : headParam.keySet()) {
						get.setHeader(s, headParam.get(s));
					}
				}

				response = httpClient.execute(get);
				if (response.getStatusLine().getStatusCode() == 200) {
					Header[] headers = response.getAllHeaders();
					System.out.println(headers.toString());
					for(int j=0;j<headers.length;j++){
						System.out.println(headers[j]);
					}
					String content = EntityUtils.toString(response.getEntity(), code);

					long time2 = System.currentTimeMillis();
//					System.out.println("\tGET页面耗时" + (time2 - time1) + "ms");

					return content;
					
				} else if(response.getStatusLine().getStatusCode() == 302) {
					String locationUrl=response.getLastHeader("Location").getValue();
					return locationUrl;
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
		return "";
	}
	

	/**
	 * 返回CloseableHttpResponse,主要访问验证码页面
	 * 
	 * @param url
	 * @param code
	 *            编码
	 * @param cookie
	 * @param httpClient
	 * @param sleep_time_per_page
	 *            间隔时间
	 * @param errorCount
	 *            循环错误次数
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static byte[] getContentByte(String url, String code, String cookie, CloseableHttpClient httpClient,
			long sleep_time_per_page, int errorCount) throws InterruptedException, ParseException, IOException {
		CloseableHttpResponse response = null;
		for (int i = 0; i < errorCount; i++) {
			try {
				Thread.sleep(sleep_time_per_page);
				long time1 = System.currentTimeMillis();
				HttpGet get = new HttpGet(url);
				get.setHeader("User-Agent", user_agent);
				get.setHeader("Accept-Language", accept_language);
				get.setHeader("Host", get.getURI().getHost());
				get.setHeader("Referer", url);
				get.setConfig(DEFAULT_HTTP_REQUEST_CONFIG);
				if (cookie != null && !cookie.equals("")) {
					get.setHeader("Cookie", cookie);
				} else {
					// System.out.println("Cookie为空");
				}
				response = httpClient.execute(get);
				if (response.getStatusLine().getStatusCode() == 200) {
					byte[] captureContent = EntityUtils.toByteArray(response.getEntity());
					long time2 = System.currentTimeMillis();
					System.out.println("GET页面耗时" + (time2 - time1) + "ms");

					return captureContent;
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
		return null;
	}


	/**
	 * 获取网站的set-cookie值
	 * 
	 * @param url
	 * @return setcookie值
	 * @throws IOException
	 * @throws ClientProtocolException
	 * */
	public static String setCookie(String url, CloseableHttpClient httpClient, int errorCount) throws ClientProtocolException,
			IOException {
		for (int i = 0; i < errorCount; i++) {
			CloseableHttpResponse response = null;
			try {
				long time1 = System.currentTimeMillis();
				HttpGet setCookieGet = new HttpGet(url);
				setCookieGet.setHeader("User-Agent", user_agent);
				setCookieGet.setHeader("Accept", accept_with_image);
				setCookieGet.setHeader("Accept-Language", accept_language);
				setCookieGet.setHeader("Host", setCookieGet.getURI().getHost());
				setCookieGet.setConfig(DEFAULT_HTTP_REQUEST_CONFIG);
				response = httpClient.execute(setCookieGet);
				if (response.getStatusLine().getStatusCode() == 200) {
					Header[] cookies = response.getHeaders("Set-Cookie");
					String sessionID = "";
					for (Header cookie : cookies) {
						int index = cookie.getValue().indexOf(";");
						sessionID = sessionID + cookie.getValue().substring(0, index) + ";";
						System.out.println("set-cookie=" + sessionID);
					}
					long time2 = System.currentTimeMillis();
					System.out.println("\tsetCookie页面耗时" + (time2 - time1) + "ms");
					return sessionID;
				} else {
					System.out.println("\t获取Cookie失败！");
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
		return "";
	}


	public static String setCookie(String url, HashMap<String, String> postValue, String Code,
			CloseableHttpClient httpClient, long sleep_time_per_page, int errorCount) throws ClientProtocolException,
			IOException, InterruptedException {
		CloseableHttpResponse response = null;
		for (int i = 0; i < errorCount; i++) {
			try {
				Thread.sleep(sleep_time_per_page);
				long time1 = System.currentTimeMillis();
				HttpPost putPOST = new HttpPost(url);
				putPOST.setHeader("User-Agent", user_agent);
				putPOST.setHeader("Accept-Language", accept_language);
				putPOST.setHeader("Host", putPOST.getURI().getHost());
				putPOST.setHeader("Referer", url);
				putPOST.setConfig(DEFAULT_HTTP_REQUEST_CONFIG);

				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (String s : postValue.keySet()) {
					list.add(new BasicNameValuePair(s, postValue.get(s)));
				}

				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Code);
				putPOST.setEntity(entity);
				response = httpClient.execute(putPOST);
				Header[] cookies = response.getHeaders("Set-Cookie");
				String sessionID = "";
				for (Header cookie : cookies) {
					int index = cookie.getValue().indexOf(";");
					sessionID = sessionID + cookie.getValue().substring(0, index) + ";";
					System.out.println("set-cookie=" + sessionID);
				}
				long time2 = System.currentTimeMillis();
				System.out.println("\tsetCookie页面耗时" + (time2 - time1) + "ms");
				return sessionID;
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
		return "";
	}

	/**
	 * 获取匹配的正则表达式值
	 * 
	 * @param inputString
	 * @param regex
	 * @return
	 */
	public static String getPageByRegex(String inputString, String regex) {
		Pattern nop = Pattern.compile(regex);
		Matcher m = nop.matcher(inputString);
		while (m.find()) {
			return new String(m.group(1).trim());
		}
		return "";
	}


	/**
	 * 获取if内容
	 * 
	 * @param functionCongent
	 * @param str
	 * @return
	 */

	public String functionStr(String functionCongent, String str) {
		// if (str == '1') {
		// window.location = '/businessPublicity.jspx?id=8FE2233CDA89697A6D9D905C95B8ECC1';
		// }
		str = str.replaceAll("(.*?[(])|(.*?,)", "").trim();
		String regex = "(if.*?['\"]" + str + "['\"][\\s\\S]*?[}])";
		String cont = getPageByRegex(functionCongent, regex);
		if (cont.equals("")) {// else情况时
			regex = "(else[{][\\s\\S]*?[}])";
			cont = getPageByRegex(functionCongent, regex);
		}

		cont = cont.replaceAll("\\s+//.+", "");
		return cont;
	}

	/**
	 * 直接获取url
	 * 
	 * @param content
	 * @return
	 */
	public String gainLocation(String content) {
		int no1 = content.indexOf("{");
		if (no1 == -1) {
			System.out.println("");
		}
		content = content.substring(no1);
		String regex = "(['\"].*?);";
		String match = getPageByRegex(content, regex).trim();
		// match = match.replace("\")", "").replaceAll("[\"';]", "").trim();
		return match;
	}

	/**
	 * 拼接带有参数的url
	 * 
	 * @param content
	 * @param id
	 * @return
	 */
	public String gainLocation2(String content, String url, HashMap<String, String> map) {
		String[] locationSplit = content.split("\\+");
		StringBuffer completeUrl = new StringBuffer();

		for (String l : locationSplit) {
			l = l.trim();

			for (Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				if (l.equals(key)) {
					l = entry.getValue();
					break;
				} else if (l.equals("nqymc")) {
					l = map.get("qymc");
				}

				if (l.equals("new Date().getTime()")) {
					l = System.currentTimeMillis() + "";
				}
				if (l.equals("Request['rid']")) {
					if (url.indexOf("?") != -1) {
						String rid = url.substring(url.indexOf("?"));
						l = rid;
					}

				}

			}

			l = l.replaceAll("[\"';[)]]", "").trim();
			completeUrl.append(l);
		}

		return completeUrl.toString();
	}

	/**
	 * 获取function的范围
	 * 
	 * @param content
	 * @param functionName
	 * @return
	 */
	public String functionRange(String content, String functionName) {
		// function togo(str) {
		// if (str == '1') {
		// window.location = '/businessPublicity.jspx?id=8FE2233CDA89697A6D9D905C95B8ECC1';
		// } else if (str == '2') {
		// window.location = '/enterprisePublicity.jspx?id=8FE2233CDA89697A6D9D905C95B8ECC1';
		// }else if (str == '3') {
		// window.location = '/otherDepartment.jspx?id=8FE2233CDA89697A6D9D905C95B8ECC1';
		// }else if(str == '4'){
		// window.location = '/justiceAssistance.jspx?id=8FE2233CDA89697A6D9D905C95B8ECC1';
		// }
		// }
		String cont = "";
		functionName = functionName.split(",")[0];
		String regex = "(function " + functionName + " {0,}[(][\\s\\S]+)";
		String str = getPageByRegex(content, regex);
		int k1 = 0;// {个数
		int k2 = 0;// }个数
		for (int i = 0; i < str.length(); i++) {
			String item = str.charAt(i) + "";
			if (item.equals("{")) {
				k1++;
			} else if (item.equals("}")) {
				k2++;
			}
			if (k2 != 0 && k1 != 0 && k1 == k2) {
				cont = str.substring(0, i);
				return cont;
			}
		}
		return cont;
	}

	/**
	 * 手动输入验证码
	 * 
	 * @param string
	 * @return
	 */
	public static String enterText(String string) {
		// 创建输入对象
		Scanner sc = new Scanner(System.in);
		// 获取用户输入的字符串
		String str = null;
		System.out.println(string);
		str = sc.nextLine();
		sc.close();
		// System.out.println("你输入的字符为:" + str);
		return str;
	}

	/**
	 * 保存异常到本地<br>
	 * 例如：exceptionHold(url, e1, EXCEPTION_FOLDER + "ErrorID.log");
	 * 
	 * @param content
	 *            附加内容
	 * @param e
	 *            异常内容
	 * @param flie
	 *            保存路径<br>
	 *            urlHandle.exceptionHold(sdf2.format(new Date(System.currentTimeMillis())) + "\t" + keyword, t, EXCEPTION_FOLDER + "详情页.log");
	 */
	public void exceptionHold(String content, Throwable e, String flie) {
		if (e != null) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			String tmp = "[" + String.format("%tF %<tT", (new Date(System.currentTimeMillis()))) + "]";
			writer.append(tmp + writer.getBuffer().toString());

			try {
				PrintWriter exception = new PrintWriter(new BufferedWriter(new FileWriter(flie, true)), true);
				exception.println(content);
				exception.println(writer);
				exception.println("***************************************************************");
				exception.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("e为空");
		}
	}

	/**
	 * 提取json内容
	 * 
	 * @param content
	 * @return
	 */
	public ArrayList<String> extractJson(String content) {
		ArrayList<String> objectList = new ArrayList<String>();
		ArrayList<String> arrayList = new ArrayList<String>();
		ArrayList<String> returnList = new ArrayList<String>();
		int k1 = 0;// {个数
		int k2 = 0;// }个数
		int k3 = 0;// {开始位置
		int k4 = 0;// }结束位置
		for (int i = 0; i < content.length(); i++) {
			String item = content.charAt(i) + "";
			if (item.equals("{")) {
				if (i + 1 <= content.length() && !(content.charAt(i + 1) + "").matches("\r")) {
					k1++;
					if (k1 == 1) {
						k3 = i + 1;
					}
				}
			} else if (item.equals("}")) {
				if (k1 != 0) {
					k2++;
				}
				k4 = i + 1;
			}
			if (k2 != 0 && k1 != 0 && k1 == k2) {
				String str = content.substring(k3 - 1, k4);
				String regex = "\\{([\\s\\S]*)(\".*\"[ ]{0,}:[ ]{0,}\".*\")([\\s\\S]*)\\}";
				if (str.matches(regex)) {
					objectList.add(str);
					// System.out.println("大括号数组\n" + str);
				}
				k1 = 0;
				k2 = 0;
			}
		}
		k1 = 0;
		k2 = 0;
		k3 = 0;
		k4 = 0;
		for (int i = 0; i < content.length(); i++) {
			String item = content.charAt(i) + "";
			if (item.equals("[")) {
				k1++;
				if (k1 == 1) {
					k3 = i + 1;
				}
			} else if (item.equals("]")) {
				if (k1 != 0) {
					k2++;
				}
				k4 = i + 1;
			}
			if (k2 != 0 && k1 != 0 && k1 == k2) {
				String str = content.substring(k3 - 1, k4);
				String regex = "\\[([\\s\\S]*)(\".*\"[ ]{0,}:[ ]{0,}\".*\")([\\s\\S]*)\\]";
				if (str.matches(regex)) {
					arrayList.add(str);
					// System.out.println("方括号数组\n" + str);
				}
				k1 = 0;
				k2 = 0;
			}
		}
		// 判断类型
		for (int i = 0; i < arrayList.size(); i++) {
			for (int j = 0; j < objectList.size(); j++) {
				String array = arrayList.get(i);
				String object = objectList.get(j);
				// 数组类型
				if (array.length() > object.length()) {
					if (array.contains(object)) {
						returnList.add(array);
						break;
					}
				}
			}
		}
		for (int i = 0; i < objectList.size(); i++) {
			for (int j = 0; j < arrayList.size(); j++) {
				String object = objectList.get(i);
				String array = arrayList.get(j);
				// 数组类型
				if (object.length() > array.length()) {
					if (object.contains(array)) {
						returnList.add(object);
						break;
					}
				}
			}
		}

		return returnList;

	}

	/**
	 * 表字段处理类<br>
	 * 替换汉字，字母，数字，下划线_,以外的字符
	 * 
	 * @param field
	 * @return
	 */
	public static String fieldHandle(String field,String replace) {
		String field2 = null;
		if (field == null) {
			System.out.println("字符处理失败！");
		} else {
			field2 = field.trim().replaceAll("[^\u4e00-\u9fa50-9a-zA-Z]", replace);
			if (field2.length() < 60) {
			} else {
				System.out.println("字符过长！\t" + field);
				field2 = field2.substring(0, 60);
			}
		}
		return field2;
	}
	
	public static void main(String[] args) throws SQLException {
		System.out.println(fieldHandle(" as78王$%:~",""));
	}
	
	/**
	 * 无效table跳过
	 * 
	 * @param table
	 * @return 无效返回false
	 */
	public Boolean invalidTable(String table) {
		if (table.toString().contains(">上一页</") && table.toString().contains("下一页</")) {// 为翻页table
			return false;
		} else if (table.toString().replaceAll("\\s", "").contains("><<")
				&& table.toString().replaceAll("\\s", "").contains(">></")) {// 为翻页table
			return false;
		}
		return true;
	}

}
