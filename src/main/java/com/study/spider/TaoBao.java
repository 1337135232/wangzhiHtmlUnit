package com.study.spider;

import org.openqa.selenium.By.ById;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.util.webDriver.WebDriverRequest;

public class TaoBao {

	private static final String username = "";
	private static final String password = "";
	private static final String taobao_login = "https://login.taobao.com/member/login.jhtml?spm=a21bo.50862.754894437.1.SkFl4D&f=top&redirectURL=https%3A%2F%2Fwww.taobao.com%2F";
	public static void main(String[] args) {
		
//		System.setProperty("webdriver.chrome.driver", "C:/Users/ufenqi/git/wangzhiHtmlUnit/target/classes/conf/seleunim-driver/chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
		WebDriverRequest request = new WebDriverRequest();
		WebDriver driver = request.getDriver();
		
		driver.get(taobao_login);
		driver.findElement(ById.id("J_Quick2Static")).click();
		
		WebElement uEle = driver.findElement(ById.id("TPL_username_1"));
		uEle.clear();
		uEle.sendKeys(username);
		
		WebElement passEle = driver.findElement(ById.id("TPL_password_1"));
		passEle.clear();
		passEle.sendKeys(password);
		
		WebElement submitEle = driver.findElement(ById.id("J_SubmitStatic"));
		submitEle.click();
		
		driver.close();
		driver.quit();
		request.close();
		System.out.println(driver.getCurrentUrl());
		System.out.println(driver.getPageSource());
		
		
	}
}
