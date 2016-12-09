package com.util.webDriver;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.util.FileUtil;



/**
 * 可以利用WebDriver实现请求得到相应的信息，但是比httpClient请求效率低一些， <br>
 * 因此建议只有用httpClient抓不到信息时使用。 目前提供的默认WebDriver是PhantomJSDriver。
 * 
 * @author yp
 *
 */
public class WebDriverRequest implements Closeable {
	private static final Logger logger = Logger.getLogger(WebDriverRequest.class);

	protected final WebDriver driver;

	private static final int redirect = 3;
	private static final int implicitlyWait = 30000;
	private static final int pageLoadTimeout = 120000;
	private static final int maxLoadTimeout = 120000;
	private static final int scriptTimeout = 30000;
	private static final boolean javascriptEnabled = Boolean.parseBoolean("true");
	private static final String[] phantomjsCliArgs = { "--load-images=false" };
	private static final String[] phantomjsCliArgsLoadImages = { "--load-images=true" };
	private static final String phantomjsUserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36";
	private static final String phantomjsTakesScreenshot = "false";

	static {
		String classPath=FileUtil.getResourcePath();
	/*	String classPath = WebDriverRequest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File confFile = new File(new File(classPath).getParentFile().getParentFile(), "conf");
		classPath = confFile.exists() ? confFile.getPath() + "/" : classPath;*/
		// 指定PhantomJS 可执行程序的位置
		if (System.getProperty("os.name").startsWith("Win")) {
			System.setProperty("phantomjs.binary.path", classPath + "phantomjs/phantomjs.exe");
		} else {
			System.setProperty("phantomjs.binary.path", classPath + "phantomjs/phantomjs");
		}
	}

	public WebDriverRequest() {
		this(false,null);
	}

	public WebDriverRequest(final String proxy) {
		this(false,proxy);
	}
	
	public WebDriverRequest(final String proxyIp, final int proxyPort) {
		this(false,proxyIp,proxyPort);
	}
	
	public WebDriverRequest(final boolean takesScreenshot, final String proxyIp, final int proxyPort) {
		this(takesScreenshot,proxyIp + ":" + proxyPort);
	}
	
	public WebDriverRequest(final boolean takesScreenshot,final String proxy) {
		this.driver = createPhantomJSDriver(takesScreenshot, proxy);
	}

	public WebDriverRequest(final WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * 创建PhantomJSDriver
	 * @param takesScreenshot
	 * @param proxy
	 * @return
	 */
	public static PhantomJSDriver createPhantomJSDriver(final boolean takesScreenshot, final String proxy) {

		DesiredCapabilities caps = DesiredCapabilities.phantomjs();
		caps.setCapability("phantomjs.page.settings.userAgent", phantomjsUserAgent + RandomUtils.nextInt(0, 100000));
		if (takesScreenshot) {
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomjsCliArgsLoadImages);
			caps.setCapability("takesScreenshot", "true");
		} else {
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomjsCliArgs);
			caps.setCapability("takesScreenshot", phantomjsTakesScreenshot);
		}
		caps.setJavascriptEnabled(javascriptEnabled);
		if (proxy != null && !"".equals(proxy)) {
			caps.setCapability(CapabilityType.PROXY,
					new Proxy().setHttpProxy(proxy).setSslProxy(proxy).setFtpProxy(proxy));
		}
		PhantomJSDriver driver = new PhantomJSDriver(caps);

		driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.MILLISECONDS);
		driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.MILLISECONDS);
		driver.manage().timeouts().setScriptTimeout(scriptTimeout, TimeUnit.MILLISECONDS);
		driver.manage().window().maximize();
		return driver;
	}
	
	/**
	 * 创建FireFox  driver
	 * @param proxy
	 * @return
	 */
	public static FirefoxDriver createFirefoxDriver(final String proxy) {

		DesiredCapabilities caps = DesiredCapabilities.firefox();

		if (proxy != null && !"".equals(proxy)) {
			caps.setCapability(CapabilityType.PROXY,
					new Proxy().setHttpProxy(proxy).setSslProxy(proxy).setFtpProxy(proxy));
		}
		FirefoxDriver driver = new FirefoxDriver(caps);

		driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.MILLISECONDS);
		driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.MILLISECONDS);
		driver.manage().timeouts().setScriptTimeout(scriptTimeout, TimeUnit.MILLISECONDS);
		driver.manage().window().maximize();
		return driver;
	}
	
	
	/**
	 * 请求url获取文本
	 * @param url
	 * @return
	 */
	public String get(final String url) {
		return getHtml(doGet(url));
	}

	/**
	 * 请求url并返回driver，目的是获取driver中加载的response
	 * @param url
	 * @return
	 */
	public WebDriver doGet(final String url) {
		boolean inited = false;
		int index = 0;
		int timeout = pageLoadTimeout;
		while (!inited && index < redirect) {
			// 最后一次跳转使用最大的默认超时时间
			if (index == redirect - 1) {
				timeout = maxLoadTimeout;
			}
			inited = isSuccessfulLoad(url, timeout);
			index++;
		}
		if (!inited && index == redirect) {
			// 最终跳转失败
			throw new WebDriverException(
					String.format("can not get the url [%s] after retry %s times!", url, redirect));
		}

		return this.driver;
	}
	
	/**
	 * 设置超时时间，加载url  this.driver.get(url);
	 * @param url
	 * @param timeout
	 * @return
	 */
	private boolean isSuccessfulLoad(final String url, final int timeout) {
		try {
			this.driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.MILLISECONDS);
			this.driver.get(url);
			logger.info("successful load url:" + url);
			return true;// 跳转并且加载页面成功在返回true
		} catch (WebDriverException e) {
			logger.warn(String.format("url[%s]\n%s", url, e.getMessage()));
			return false;// 超时的情况下或者异常返回false
		}
	}
	
	/**
	 * 获取页面
	 * @param driver
	 * @return
	 */
	public String getHtml(final WebDriver driver) {
		String html = driver.getPageSource();
		if (html.length() < 80) {
			throw new WebDriverException(String.format("page source length is less than 80.\nurl[%s]\n%s",
					driver.getCurrentUrl(), driver.getPageSource()));
		}
		return html;
	}

	/**
	 * 请求获取cookie
	 * @param url
	 * @return
	 */
	public String getCookies(final String url) {
		return convertCookies(doGet(url).manage().getCookies());
	}

	/**
	 * 将webdriver加载的cookie写出
	 * @param Cookies
	 * @return
	 */
	public static String convertCookies(final Set<Cookie> Cookies) {
		StringBuilder sb = new StringBuilder();
		for (Cookie cookie : Cookies) {
			sb.append(cookie.getName());
			sb.append('=');
			sb.append(cookie.getValue());
			sb.append("; ");
		}
		logger.info("Cookies:" + sb.toString());
		return sb.toString();
	}

	
	
	
	
	
	
	
	public static CookieStore convertCookies2(final Set<Cookie> Cookies) {
		// org.apache.http.cookie.Cookie
		BasicCookieStore cookieStore = new BasicCookieStore();
		for (Cookie cookie : Cookies) {
			BasicClientCookie clientCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
			// clientCookie.setVersion(0);
			clientCookie.setDomain("211.141.74.198");
			clientCookie.setPath(cookie.getPath());
			clientCookie.setExpiryDate(cookie.getExpiry());
			clientCookie.setSecure(cookie.isSecure());
			cookieStore.addCookie(clientCookie);
			logger.info("Cookies:" + cookie.getName() + ":" + cookie.getValue());
		}

		return cookieStore;
	}

	public Object executeScript(final String using, final Object... args) {
		Object object = ((JavascriptExecutor) driver).executeScript(using, args);
		logger.debug(String.format("execute script:[%s]", using));
		return object;
	}

	public void saveScreenShotByXPath(final String xpath, final String fileName) throws IOException {
		driver.manage().window().maximize();
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage img = ImageIO.read(scrFile);
		WebElement element = driver.findElement(By.xpath(xpath));
		Point p = element.getLocation();
		Dimension dimension = element.getSize();
		BufferedImage dest = img.getSubimage(p.x, p.y, dimension.width, dimension.height);
		ImageIO.write(dest, "png", scrFile);
		FileUtils.copyFile(scrFile, new File(fileName));
	}

	public void saveScreenShotByXPathJpg(final String xpath, final String fileName) throws IOException {
		driver.manage().window().maximize();
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage img = ImageIO.read(scrFile);
		WebElement element = driver.findElement(By.xpath(xpath));
		Point p = element.getLocation();
		Dimension dimension = element.getSize();
		BufferedImage dest = img.getSubimage(p.x, p.y, dimension.width, dimension.height);
		ImageIO.write(dest, "jpg", scrFile);
		FileUtils.copyFile(scrFile, new File(fileName));
	}

	public void saveScreenShotByXPathBeiJing(final String xpath, final String fileName) throws IOException {
		driver.manage().window().maximize();
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage img = ImageIO.read(scrFile);
		WebElement element = driver.findElement(By.xpath(xpath));
		Point p = element.getLocation();
		Dimension dimension = element.getSize();
		BufferedImage dest = img.getSubimage(p.x, p.y + 14, dimension.width, dimension.height);
		ImageIO.write(dest, "png", scrFile);
		FileUtils.copyFile(scrFile, new File(fileName));
	}

	public void saveScreenShotByXPathYunNan(final String xpath, final String fileName) throws IOException {
		driver.manage().window().maximize();
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage img = ImageIO.read(scrFile);
		WebElement element = driver.findElement(By.xpath(xpath));
		Point p = element.getLocation();
		Dimension dimension = element.getSize();
		BufferedImage dest = img.getSubimage(p.x + 465, p.y + 200, dimension.width, dimension.height);
		ImageIO.write(dest, "png", scrFile);
		FileUtils.copyFile(scrFile, new File(fileName));
	}

	@Override
	public void close() {
		driver.close();
		driver.quit();
	}

	public static void main(String[] args) {

		WebDriverRequest request = new WebDriverRequest();
		System.out.println(request.get("http://www.baidu.com"));
//		String cookies = request.getCookies("http://www.ahcredit.gov.cn/search.jspx");
//		System.out.println(cookies);
		request.close();
	}
}
