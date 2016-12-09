package com.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;


/**
 * @Description：httpclient获取方法，包含get和post方法，可以使用代理ip。
 * @author yp
 * @date 2016-10-28
 *
 */
@SuppressWarnings("deprecation")
public class HttpRequest {

	private static final Logger logger = Logger.getLogger(HttpRequest.class);

	public static Pattern encodePartner = Pattern.compile("<meta[^<>]*charset=['\" ]*([a-zA-Z0-9\\-]+)[^<>]*>",
			Pattern.CASE_INSENSITIVE);

	public static Pattern bodyPartner = Pattern.compile("(?is)<body.*?>.*?</body>");
	public static Pattern scriptPartner = Pattern.compile("(?is)<script.*?>.*?</script>");

	private static CookieStore cookieStore = new BasicCookieStore();

	private String url;
	private String host;
	private String referer;
	
	//默认保存cookie
	private boolean storeCookie = true;
	private CloseableHttpClient httpclient;
	private HttpClientContext context;
	private List<URI> redirectUris;

	private String proxyHost;
	private int proxyPort;

	private Map<String, String> header;

	private int statusCode = -1;

	public HttpRequest() {
	}

	/**
	 * 创建通过代理访问的httpRequest
	 * 
	 * @param proxyHost
	 * @param proxyPort
	 */
	public HttpRequest(String proxyHost, int proxyPort) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param path
	 */
	public void download(String url, String path) {
		download(url, path,new StringBuffer(""));
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param path
	 */
	public void download(String url, String path, StringBuffer cookies) {
		download(url, path, null,cookies);
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param path
	 */
	public void download(String url, String path, Map<String, String> headers) {
		download(url, path, headers,new StringBuffer(""));
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param path
	 */
	public void download(String url, String path, Map<String, String> headers, StringBuffer cookies) {
		httpclient = createHttpClient();
		HttpGet httpGet = createHttpGet(url);
		CloseableHttpResponse response = null;
		try {
			// 设置请求头
			if (headers != null && headers.keySet().size() > 0) {
				for (String key : headers.keySet()) {
					httpGet.addHeader(key, headers.get(key));
				}
			}
			response = httpclient.execute(httpGet);
			if(!StringUtils.isEmpty(cookies)){
				Header[] tempHeaders = response.getAllHeaders();
				for (Header header : tempHeaders) {
					if (!header.getName().equals("Set-Cookie")) {
						continue;
					}
					if (!cookies.toString().equals("")) {
						cookies.append(" ");
					}
					cookies.append(header.getValue());
				}
			}
			logger.info("Cookies=" + cookies.toString());
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() >= 400) {
				throw new IOException("Got bad response, error code = " + response.getStatusLine().getStatusCode()
						+ " imageUrl: " + url);
			}
			if (entity != null) {
				InputStream input = entity.getContent();
				OutputStream output = new FileOutputStream(new File(path));
				IOUtils.copy(input, output);
				output.flush();
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(httpclient!=null){
				try {
					httpclient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 发送http get请求
	 * 
	 * @param url
	 * @return
	 */
	public String get(String url) {
		return get(url, false,new StringBuffer(""),null);
	}
	
	/**
	 * 发送http get请求
	 * 
	 * @param url
	 * @return
	 */
	public String get(String url, StringBuffer cookies) {
		return get(url, false, cookies,null);
	}

	
	/**
	 * 发送http get请求
	 * 
	 * @param url
	 * @return
	 */
	public String get(String url, Map<String, String> headers) {
		return get(url, false, new StringBuffer(""),headers);
	}
	/**
	 * 发送http get请求
	 * 
	 * @param url
	 * @return
	 */
	public String get(String url, boolean isCheck,StringBuffer cookies,Map<String, String> headers) {
		prepare(url);
		HttpGet httpGet = createHttpGet(url, isCheck);
		CloseableHttpResponse response = null;
		String content = null;
		try {
			if (context == null) {
				context = createHttpClientContext();
			}
			
			// 设置请求头
			if (headers != null && headers.keySet().size() > 0) {
				for (String key : headers.keySet()) {
					httpGet.addHeader(key, headers.get(key));
				}
			}
			
			response = httpclient.execute(httpGet, context);

			if(!StringUtils.isEmpty(cookies)){
				Header[] tempHeaders = response.getAllHeaders();

				for (Header header : tempHeaders) {
					if (!header.getName().equals("Set-Cookie")) {
						continue;
					}
					if (!cookies.toString().equals("")) {
						cookies.append(" ");
					}
					cookies.append(header.getValue());
				}
				logger.info("Cookies=" + cookies.toString());
			}
			
			statusCode = response.getStatusLine().getStatusCode();

			content = getContent(response);

			redirectUris = context.getRedirectLocations();

			referer = getFinalUrl();

		} catch (SocketTimeoutException e) {
			logger.error("网络请求过程中，数据传输超时:" + e.getMessage() + ", url=" + url);
		} catch (ConnectTimeoutException e) {
			logger.error("网络请求链接超时:" + e.getMessage() + ", url=" + url);
		} catch (HttpHostConnectException e) {
			logger.error("网络请求被拒绝:" + e.getMessage() + ", url=" + url);
		} catch (Exception e) {
			logger.error("网络请求失败:" + e.getMessage() + ", url=" + url);
		} finally {
			if (response != null) {
				try {
					response.close();
					httpclient.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Http请求：" + url + ", " + (content != null));
		}
		//
		if (content == null) {
			setError(error + 1);
		} else {
			setSuccess(success + 1);
		}
		if (success + error >= 1000) {
			synchronized (HttpRequest.class) {
				logger.warn("请求总数：" + (success + error) + ", 成功：" + success + ", 失败：" + error);
				setError(0);
				setSuccess(0);
			}
		}

		return content;
	}
	
	private void prepare(String url){
		url = url.replace("{", "%7B").replace("}", "%7D");
		url = url.replace(" ", "%20");
		this.url = url;
		this.host = UrlUtil.getHost(url);
		httpclient = createHttpClient();
	}

	/**
	 * 发送http get请求
	 * 
	 * @param url
	 * @return
	 */
	public String getRealUrl(String url) {
		prepare(url);
		
		get(url);

		return redirectUris.get(redirectUris.size() - 1).toString();
	}

	/**
	 * Http Client发送Post提交参数
	 * 
	 * @param url
	 *            表单提交的地址
	 * @param paramaters
	 *            提交表单时的参数
	 * @return
	 */
	public String post(String url, Map<String, String> paramaters) {
		return post(url, null, paramaters);

	}

	/**
	 * Http Client发送Post提交参数
	 * 
	 * @param url
	 *            表单提交的地址
	 * @param paramaters
	 *            提交表单时的参数
	 * @return
	 */
	public String post(String url, Map<String, String> headers, Map<String, String> paramaters) {
		return post(url, headers, paramaters, "");
	}

	/**
	 * Http Client发送Post提交参数
	 * 
	 * @param url
	 *            表单提交的地址
	 * @param paramaters
	 *            提交表单时的参数
	 * @param encoding
	 *            设置请求参数编码
	 * @return
	 */
	public String post(String url, Map<String, String> headers, Map<String, String> paramaters, String encoding) {
		prepare(url);
		
		HttpPost httpPost = createHttpPost(url);
		CloseableHttpResponse response = null;
		String content = null;
		try {
			if (context == null) {
				context = createHttpClientContext();
			}
			// 设置请求头
			if (headers != null && headers.keySet().size() > 0) {
				for (String key : headers.keySet()) {
					httpPost.addHeader(key, headers.get(key));
				}
			}
			// 设置Post表单提交参数
			List<NameValuePair> p = null;
			if (paramaters != null && paramaters.keySet().size() > 0) {
				p = new ArrayList<NameValuePair>();
				for (String key : paramaters.keySet()) {
					p.add(new BasicNameValuePair(key, paramaters.get(key)));
				}
			}
			if (p != null && !StringUtils.isEmpty(encoding)){
				httpPost.setEntity(new UrlEncodedFormEntity(p, encoding));
			}else if(p != null){
				httpPost.setEntity(new UrlEncodedFormEntity(p, HTTP.UTF_8));
				
			}

			response = httpclient.execute(httpPost, context);

			statusCode = response.getStatusLine().getStatusCode();

			content = getContent(response);

			redirectUris = context.getRedirectLocations();

			referer = getFinalUrl();
		} catch (SocketTimeoutException e) {
			logger.error("网络请求过程中，数据传输超时:" + e.getMessage() + ", url=" + url);
		} catch (ConnectTimeoutException e) {
			logger.error("网络请求链接超时:" + e.getMessage() + ", url=" + url);
		} catch (HttpHostConnectException e) {
			logger.error("网络请求被拒绝:" + e.getMessage() + ", url=" + url);
		} catch (Exception e) {
			logger.error("网络请求失败:" + e.getMessage() + ", url=" + url);
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
					httpclient.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Http请求：" + url + ", " + (content != null));
		}
		//
		if (content == null) {
			setError(error + 1);
		} else {
			setSuccess(success + 1);
		}
		if (success + error >= 1000) {
			synchronized (HttpRequest.class) {
				logger.warn("请求总数：" + (success + error) + ", 成功：" + success + ", 失败：" + error);
				setError(0);
				setSuccess(0);
			}
		}

		return content;
	}

	/**
	 * 如果存在跳转，返回跳转之后的url，如果没有跳转，返回当前url
	 * 
	 * @return
	 */
	public String getFinalUrl() {
		if (redirectUris != null && redirectUris.size() > 0) {
			return redirectUris.get(redirectUris.size() - 1).toString();
		} else {
			return url;
		}
	}

	/**
	 * 从response中解析内容，如果header中没有包含编码，会根据内容中的meta转码
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public String getContent1(HttpResponse response) throws ParseException, IOException {

		HttpEntity entity = response.getEntity();
		String content = null;
		if (entity.getContentEncoding() != null && entity.getContentEncoding().getValue().equalsIgnoreCase("gzip")) {
			GzipDecompressingEntity gde = new GzipDecompressingEntity(entity);
			content = processEntity(response, gde.getContent(), "utf-8");
		} else if (entity.getContentEncoding() != null
				&& entity.getContentEncoding().getValue().equalsIgnoreCase("deflate")) {
			DeflateDecompressingEntity dde = new DeflateDecompressingEntity(entity);
			content = processEntity(response, dde.getContent(), "utf-8");
		} else {
			content = processEntity(response, entity.getContent(), "utf-8");
		}
		return content;
	}

	private static String processEntity(HttpResponse responses, InputStream inputStream, String charset)
			throws IOException {
		String result = null;
		DataInputStream dis = new DataInputStream(inputStream);
		ByteArray ba = new ByteArray();
		int lenth = 1024;
		byte[] bbb = new byte[lenth];
		while (true) {
			int i = dis.read(bbb);
			if (i == -1)
				break;
			ba.append(bbb);
			if (i < 1024)
				ba.clearNull(lenth - i);
		}
		result = ba.toString();
		String charsetTemp = getCharSet(result);
		if (responses.getFirstHeader("Content-Type") != null) {
			String charsetTempTwo = getCharSetByContentType(responses.getFirstHeader("Content-Type").toString());
			if (charsetTempTwo != null)
				if (!charsetTempTwo.equals(charsetTemp)) {
					charsetTemp = charsetTempTwo;
				}
		}
		if (charsetTemp != null)
			if (charsetTemp.equalsIgnoreCase(charset))
				return ba.toString();
			else
				return ba.toString(charsetTemp);
		else
			return result;
	}

	public static String getCharSetByContentType(String text) {
		if (text == null)
			throw new NullPointerException();
		String charsetL[] = { "UTF-8", "GBK", "GB2312", "ISO-8859-1", "UTF-16" };
		String charset = RegexParser.baseParse(text, "charset=([\\s\\S]*)", 1);
		if (charset == null)
			return null;
		charset = charset.replace(";", "");
		String charsetR[] = null;
		if (charset == null)
			return null;
		/****************/
		if (charset.contains(",")) {
			charsetR = charset.split(",");
		}
		/****************/
		for (String str : charsetL)
			if (charset.equalsIgnoreCase(str))
				return str;
		for (String strr : charsetR)
			for (String str : charsetL)
				if (RegexParser.ismatching(strr, str))
					return str;
		return null;
	}

	public static String getCharSet(String text) {
		if (text == null)
			throw new NullPointerException();
		String charsetL[] = { "UTF-8", "GBK", "GB2312", "ISO-8859-1", "UTF-16" };
		String charset = RegexParser.baseParse(text, "charset=[\"|']?([\\s\\S]*?)[\"|']", 1);
		if (charset == null)
			return null;
		for (String str : charsetL)
			if (charset.equalsIgnoreCase(str))
				return str;
		for (String str : charsetL)
			if (RegexParser.ismatching(charset, str))
				return str;
		return null;
	}

	/**
	 * 从response中解析内容，如果header中没有包含编码，会根据内容中的meta转码
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public String getContent(HttpResponse response) throws ParseException, IOException {

		Header contentTypeHeader = response.getFirstHeader("Content-Type");
		String contentType = null;
		String charset = "ISO-8859-1";
		if (contentTypeHeader != null) {
			contentType = contentTypeHeader.getValue();
			if (contentType != null && contentType.indexOf("=") > 0) {
				charset = contentType.substring(contentType.indexOf("=") + 1);
			}
		}

		HttpEntity entity = response.getEntity();
		if (entity == null) {
			return null;
		}
		String content = null;
		try {
			InputStream inputStream = entity.getContent();
			Header ceHeader = entity.getContentEncoding();
			if (ceHeader != null && ceHeader.getValue().contains("gzip")) {
				inputStream = new GZIPInputStream(inputStream);
			}
			if (StringUtils.containsIgnoreCase(charset, "2312")) {
				content = IOUtils.toString(inputStream, "GBK");
			} else {
				content = IOUtils.toString(inputStream, charset);
			}
		} catch (EOFException e) {
			e.printStackTrace();
		}

		if (!StringUtils.containsIgnoreCase(contentType, "charset") && content != null) {
			charset = null;
			String ct = scriptPartner.matcher(content).replaceAll("");
			ct = bodyPartner.matcher(ct).replaceAll("");
			// System.out.println(ct);
			Matcher matcher = encodePartner.matcher(ct);

			// meta包含一个charset，就用这个charset解析；如果没有或者多个，就用编码探测方式
			TreeSet<String> metaCharset = new TreeSet<String>();
			while (matcher.find()) {
				String m = matcher.group(1).toUpperCase();
				if (m.equals("GB2312")) {
					m = "GBK";
				}
				metaCharset.add(m);
			}
			if (metaCharset.size() == 1) {
				charset = metaCharset.first();
			}
			if (charset == null) {
				charset = detect(content);
			}

			if (charset != null) {
				try {
					if (StringUtils.containsIgnoreCase(charset, "2312")) {
						charset = "GBK";
					}
					content = new String(content.getBytes("ISO-8859-1"), charset);
				} catch (UnsupportedEncodingException e) {
					logger.error("html编码提取错误：" + charset + ",url=" + url);
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	protected InputStream decompressStream(InputStream input) {
		// we need a pushbackstream to look ahead
		PushbackInputStream pb = new PushbackInputStream(input, 2);
		byte[] signature = new byte[2];
		try {
			pb.read(signature); // read the signature
			pb.unread(signature); // push back the signature to the stream
			// check if matches standard gzip magic number
			if (signature[0] == (byte) 0x1f && signature[1] == (byte) 0x8b)
				return new GZIPInputStream(pb);
			else
				return pb;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pb;
	}

	protected HttpClientContext createHttpClientContext() {
		HttpClientContext context = HttpClientContext.create();
		if (storeCookie) {
			if (cookieStore == null) {
				cookieStore = new BasicCookieStore();
			}
			context.setCookieStore(cookieStore);
		}
		return context;
	}

	/**
	 * 创建常用的HttpClient
	 */
	protected CloseableHttpClient createHttpClient() {

		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		SSLContext sslContext = SSLContexts.createSystemDefault();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", plainsf).register("https", sslsf).build();

		HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);

		HttpClientBuilder builder = HttpClients.custom();
		if (proxyHost != null && proxyPort > 0) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			builder.setRoutePlanner(routePlanner);
		}

		CloseableHttpClient httpclient = builder.setConnectionManager(cm).setRedirectStrategy(new LaxRedirectStrategy())
				.setRetryHandler(new DefaultHttpRequestRetryHandler(3, false)).disableContentCompression().build();

		return httpclient;
		
		/*try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						@Override
						public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
								throws java.security.cert.CertificateException {
							// TODO Auto-generated method stub
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();*/
	}

	/**
	 * 创建常用的HttpGet
	 */
	protected HttpGet createHttpGet(String url) {
		return createHttpGet(url, false);
	}

	/**
	 * 创建常用的HttpGet
	 */
	protected HttpGet createHttpGet(String url, boolean isCheck) {
		HttpGet httpGet = new HttpGet(url);
		useChrome41Header(httpGet);
		if (header != null) {
			for (String key : header.keySet()) {
				httpGet.addHeader(key, header.get(key));
			}
		}
		if (isCheck) {
			RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
					.setSocketTimeout(10000).setConnectTimeout(8000).setMaxRedirects(3).build();
			httpGet.setConfig(requestConfig);
		}else {
//			RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
//					.setSocketTimeout(20000).setConnectTimeout(20000).setMaxRedirects(10).build();
//			httpGet.setConfig(requestConfig);	
			RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
					.setSocketTimeout(120000).setConnectTimeout(120000).setMaxRedirects(10).build();
			httpGet.setConfig(requestConfig);
		}

	

		return httpGet;
	}

	/**
	 * 获取cookies信息
	 * 
	 * @param url
	 * @param paramaters
	 * @return
	 */
	public String getCookies(String url, Map<String, String> paramaters) {
		HttpRequest request = new HttpRequest();
		StringBuffer sb = new StringBuffer();
		if (null == paramaters || paramaters.size() == 0) {
			request.get(url);
		} else {
			request.post(url, paramaters);
		}
		List<Cookie> cookies = HttpRequest.getCookieStore().getCookies();
		for (Cookie c : cookies) {
			if (!sb.toString().equals("")) {
				sb.append("; ");
			}
			sb.append(c.getName() + "=" + c.getValue());
		}
		logger.info("Cookies=" + sb.toString());
		return sb.toString();
	}
	
	/**
	 * 获取cookies信息
	 * 
	 * @param url
	 * @param paramaters
	 * @return
	 */
	public String getCookies(String url, Map<String, String> paramaters,StringBuffer sb) {
		return getCookies(null, url, paramaters, sb);
	}
	/**
	 * 获取cookies信息
	 * 
	 * @param url
	 * @param paramaters
	 * @return
	 */
	public String getCookies(HttpRequest request, String url, Map<String, String> paramaters,StringBuffer sb) {
		String html = "";
		if(request==null){
			request = new HttpRequest();
		}
		if (null == paramaters || paramaters.size() == 0) {
			html = request.get(url);
		} else {
			html = request.post(url, paramaters);
		}
		List<Cookie> cookies = HttpRequest.getCookieStore().getCookies();
		for (Cookie c : cookies) {
			if (!sb.toString().equals("")) {
				sb.append("; ");
			}
			sb.append(c.getName() + "=" + c.getValue());
		}
		logger.info("Cookies=" + sb.toString());
		return html;
	}

	/**
	 * 创建常用的HttpPost
	 */
	protected HttpPost createHttpPost(String url) {
		HttpPost httpPost = new HttpPost(url);
		useChrome41Header(httpPost);
		if (header != null) {
			for (String key : header.keySet()) {
				httpPost.addHeader(key, header.get(key));
			}
		}

		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
				.setSocketTimeout(120000).setConnectTimeout(120000).setMaxRedirects(10).build();
		httpPost.setConfig(requestConfig);

		return httpPost;
	}

	/**
	 * 编码探测
	 * @param text
	 * @return
	 */
	public static String detect(String text){
		byte[] buf = new byte[4096];
        InputStream fis = new ByteArrayInputStream(text.getBytes());
        UniversalDetector detector = new UniversalDetector(null);
        int nread;
        try {
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			  detector.handleData(buf, 0, nread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        
        return encoding;
	}
	protected void useChrome41Header(HttpGet httpGet) {
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
		httpGet.addHeader("Accept-Ranges", "none");
		httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		httpGet.addHeader("Cache-Control", "max-age=0");
		httpGet.addHeader("Connection", "keep-alive");
		httpGet.addHeader("Host", host);
		// httpGet.addHeader("Cookie",
		// "pageReferrInSession=http%3A//rd2.zhaopin.com/s/homepage.asp;
		// dywem=95841923.y; utype=200527277; Home_ResultForCustom_pageSize=60;
		// strloginusertype=4;
		// JSpUserInfo=3473207359664264547342675B7752685973557350664B645D733F6724775868527356735366416453734B675F775568537324731566026448731067057708685A7331732A664E642814103C51772B682F7359735F66316422734E67517727682F7359735F6627642B734E67517720682C73597357664164517341675F775D68547354735666336417730267447706680E7309735F66206432734E6758775E6820733073596644644B7340675A774568507353735E664164577348672E7721685C735F7320663D645B734867267728685C735F73276637645B7348672B7729685C7355735F66266427734E672077376853735573556644645E7342675E775D6852735F73276632645B7348672E7724685C735F73206630645B73486739772C685C7354735F663A6436734E675177306835735973556648643;
		// Home_ResultForCustom_isOpen=true;
		// Home_ResultForResumeId_orderBy=DATE_MODIFIED%2C1; urlfrom=121122523;
		// urlfrom2=121122523; adfcid=u2757457.k9454548909.a6191549905.pb;
		// adfcid2=u2757457.k9454548909.a6191549905.pb; adfbid=0; adfbid2=0;
		// dywez=95841923.1438666897.10.8.dywecsr=other|dyweccn=121122523|dywecmd=cnt|dywectr=zhilian;
		// __xsptplus30=30.6.1438666898.1438666898.1%231%7Cother%7Ccnt%7C121122523%7C%7C%23%231c_Zt_140CYypi8WPc-TTeG-XZFBBdUx%23;
		// pcc=r=809963044&t=0;
		// __zpWAM=1437038588245.245584.1438572114.1438667174.4; __zpWAMs1=1;
		// __zpWAMs2=1; JsOrglogin=601581833; xychkcontr=54446%2c0;
		// lastchannelurl=http%3A//hr.zhaopin.com/hrclub/index.html;
		// RDpUserInfo=; isNewUser=1; JsNewlogin=200527277; cgmark=2;
		// RDsUserInfo=266422644E674565577354665173557752674164516444674C652B732A6659730E7716671C6400640767046513730B660A7302773A67106408641467026508735F663173297758679EF4E3E944124C65257320665973527751674764526445674C6525732A66597308F87FF7DB36FD3692281805E7114B014C73A1E187315A646F0323F11B349F205F6630732A77586736645D6436673A655A7307660A73007710671C6426641A670465087302661073147711671C6408641667596504730B6609735C77366727645B6442674C652673306659735F77486747644664426744655D73506652735C77216737645B644267476556735666557354775D674264506448673365297359662373257722677B9BF93734673665237337666D8C5C7729673E645B64436747655773556654735777566741645D64306733655A735166577357775E67266432644E674665577357665F73247724674E64256430674765577354665173537751674764506445674C652373256659732477266743645664436742655373506650735177536737645F6443674565527354665473577756674364566443674C6523732766597357775E6720642F644E6744655C732D6634735A775767476454645D674665567355665F73267729674E64566448670;
		// SearchHead_Erd=rd; Home_ResultForCustom_orderBy=DATE_MODIFIED%2C1;
		// SearchHistory_StrategyId_1=%2fHome%2fResultForCustom%3fSF_1_1_1%3djava%26orderBy%3dDATE_MODIFIED%252c1%26pageSize%3d60%26SF_1_1_27%3d0%26exclude%3d1;
		// dywea=95841923.2863707354950804000.1432547217.1438579635.1438666897.10;
		// dywec=95841923; dyweb=95841923.7.10.1438666897;
		// __utma=269921210.2133347118.1432547217.1438579635.1438666897.11;
		// __utmb=269921210.7.10.1438666897; __utmc=269921210;
		// __utmz=269921210.1438666897.11.8.utmcsr=other|utmccn=121122523|utmcmd=cnt;
		// __utmv=269921210.|2=Member=200527277=1");
		if (referer == null) {
			httpGet.addHeader("Referer", host);
		} else {
			httpGet.addHeader("Referer", referer);
		}
		httpGet.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36"
						+ RandomUtils.nextInt(0, 100000));

	}

	protected void useChrome41Header(HttpPost httpPost) {
		httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpPost.addHeader("Accept-Encoding", "gzip, deflate, sdch");
		httpPost.addHeader("Accept-Ranges", "none");
		httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		httpPost.addHeader("Cache-Control", "max-age=0");
		httpPost.addHeader("Connection", "keep-alive");
		httpPost.addHeader("Host", host);
		// httpGet.addHeader("Cookie",
		// "pageReferrInSession=http%3A//rd2.zhaopin.com/s/homepage.asp;
		// dywem=95841923.y; utype=200527277; Home_ResultForCustom_pageSize=60;
		// strloginusertype=4;
		// JSpUserInfo=3473207359664264547342675B7752685973557350664B645D733F6724775868527356735366416453734B675F775568537324731566026448731067057708685A7331732A664E642814103C51772B682F7359735F66316422734E67517727682F7359735F6627642B734E67517720682C73597357664164517341675F775D68547354735666336417730267447706680E7309735F66206432734E6758775E6820733073596644644B7340675A774568507353735E664164577348672E7721685C735F7320663D645B734867267728685C735F73276637645B7348672B7729685C7355735F66266427734E672077376853735573556644645E7342675E775D6852735F73276632645B7348672E7724685C735F73206630645B73486739772C685C7354735F663A6436734E675177306835735973556648643;
		// Home_ResultForCustom_isOpen=true;
		// Home_ResultForResumeId_orderBy=DATE_MODIFIED%2C1; urlfrom=121122523;
		// urlfrom2=121122523; adfcid=u2757457.k9454548909.a6191549905.pb;
		// adfcid2=u2757457.k9454548909.a6191549905.pb; adfbid=0; adfbid2=0;
		// dywez=95841923.1438666897.10.8.dywecsr=other|dyweccn=121122523|dywecmd=cnt|dywectr=zhilian;
		// __xsptplus30=30.6.1438666898.1438666898.1%231%7Cother%7Ccnt%7C121122523%7C%7C%23%231c_Zt_140CYypi8WPc-TTeG-XZFBBdUx%23;
		// pcc=r=809963044&t=0;
		// __zpWAM=1437038588245.245584.1438572114.1438667174.4; __zpWAMs1=1;
		// __zpWAMs2=1; JsOrglogin=601581833; xychkcontr=54446%2c0;
		// lastchannelurl=http%3A//hr.zhaopin.com/hrclub/index.html;
		// RDpUserInfo=; isNewUser=1; JsNewlogin=200527277; cgmark=2;
		// RDsUserInfo=266422644E674565577354665173557752674164516444674C652B732A6659730E7716671C6400640767046513730B660A7302773A67106408641467026508735F663173297758679EF4E3E944124C65257320665973527751674764526445674C6525732A66597308F87FF7DB36FD3692281805E7114B014C73A1E187315A646F0323F11B349F205F6630732A77586736645D6436673A655A7307660A73007710671C6426641A670465087302661073147711671C6408641667596504730B6609735C77366727645B6442674C652673306659735F77486747644664426744655D73506652735C77216737645B644267476556735666557354775D674264506448673365297359662373257722677B9BF93734673665237337666D8C5C7729673E645B64436747655773556654735777566741645D64306733655A735166577357775E67266432644E674665577357665F73247724674E64256430674765577354665173537751674764506445674C652373256659732477266743645664436742655373506650735177536737645F6443674565527354665473577756674364566443674C6523732766597357775E6720642F644E6744655C732D6634735A775767476454645D674665567355665F73267729674E64566448670;
		// SearchHead_Erd=rd; Home_ResultForCustom_orderBy=DATE_MODIFIED%2C1;
		// SearchHistory_StrategyId_1=%2fHome%2fResultForCustom%3fSF_1_1_1%3djava%26orderBy%3dDATE_MODIFIED%252c1%26pageSize%3d60%26SF_1_1_27%3d0%26exclude%3d1;
		// dywea=95841923.2863707354950804000.1432547217.1438579635.1438666897.10;
		// dywec=95841923; dyweb=95841923.7.10.1438666897;
		// __utma=269921210.2133347118.1432547217.1438579635.1438666897.11;
		// __utmb=269921210.7.10.1438666897; __utmc=269921210;
		// __utmz=269921210.1438666897.11.8.utmcsr=other|utmccn=121122523|utmcmd=cnt;
		// __utmv=269921210.|2=Member=200527277=1");
		if (referer == null) {
			httpPost.addHeader("Referer", host);
		} else {
			httpPost.addHeader("Referer", referer);
		}
		httpPost.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36"
						+ RandomUtils.nextInt(0, 100000));

	}

	public static CookieStore getCookieStore() {
		return cookieStore;
	}

	public static void setCookieStore(CookieStore cookieStore) {
		HttpRequest.cookieStore = cookieStore;
	}

	public boolean isStoreCookie() {
		return storeCookie;
	}

	public HttpRequest setStoreCookie(boolean storeCookie) {
		if (this.storeCookie != storeCookie) {
			this.storeCookie = storeCookie;
			context = createHttpClientContext();
		}
		return this;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	/**
	 * 测试http连接成功率，多线程会造成阻塞，仅测试用
	 */
	private static int success = 0;
	private static int error = 0;

	public static synchronized void setSuccess(int success) {
		HttpRequest.success = success;
	}

	public static synchronized void setError(int error) {
		HttpRequest.error = error;
	}

	public static void main(String[] args) throws IOException {
		/*
		 * try { Map<String, Object> map = ProxyUtil.getProxy(); if(map!= null){
		 * String proxyHost = (String)map.get("proxyIp"); int proxyPort =
		 * ((Double)map.get("proxyPort")).intValue(); System.out.println(
		 * "proxy: "+proxyHost+":"+proxyPort); HttpRequest httpRequest = new
		 * HttpRequest(proxyHost, proxyPort); // httpRequest = new
		 * HttpRequest(); System.out.println(httpRequest.get(
		 * "http://news.ifeng.com/a/20150331/43448888_0.shtml")); } } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		/*
		 * Proxy proxy = ProxyUtil.getProxy(true); HttpRequest httpRequest = new
		 * HttpRequest(proxy.getProxyIp(), proxy.getProxyPort());
		 */
		HttpRequest httpRequest = new HttpRequest();
		// HttpRequest httpRequest = new HttpRequest("127.0.0.1", 8888);
		// http://www.mof.gov.cn/mofhome/mof/xinwenlianbo/anhuicaizhengxinxilianbo/201503/t20150316_1202624.html
		// http://www.siilu.com/20150410/130599.shtml
		// http://www.ithome.com/html/it/143101.htm
		// String content =
		// httpRequest.get("http://www.saic.gov.cn/ywdt/gsyw/zjyw/xxb/201505/t20150505_155927.html");

		String content = httpRequest
				.get("http://rd.zhaopin.com/resumepreview/resume/viewone/2/JM190420252R90250000000_1_1?searchresume=1");

		System.out.println(content);

		/*
		 * URL url=new URL(
		 * "http://www.mof.gov.cn/mofhome/mof/xinwenlianbo/anhuicaizhengxinxilianbo/201503/t20150316_1202624.html"
		 * ); URLConnection connection = url.openConnection(); InputStream input
		 * = connection.getInputStream();
		 * 
		 * if ("gzip".equals(connection.getContentEncoding())) { input = new
		 * GZIPInputStream(input); } Map<String, List<String>> headers =
		 * connection.getHeaderFields(); System.out.println(new
		 * GsonBuilder().setPrettyPrinting().create().toJson(headers));
		 * 
		 * System.out.println(IOUtils.toString(input));
		 */

	}

}
