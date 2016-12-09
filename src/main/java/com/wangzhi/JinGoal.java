package com.wangzhi;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class JinGoal {
	
	public static void main(String[] args) {
		executeMaiMai();
	}
	
	public static void executeJinGoal(){
		String url = "https://sso.jingoal.com/oauth/authorize?client_id=jmbmgtweb&response_type=code&state=%7Baccess_count%3A1%7D&locale=zh_CN&redirect_uri=https%3A%2F%2Fweb.jingoal.com%2F#/login";
		try {
			final WebClient webClient = UrlRequest.getWebClient();
			final HtmlPage page = webClient.getPage(url);
			final HtmlTextInput email = page.getHtmlElementById("email");
			email.setValueAttribute("18611936357");
			final HtmlPasswordInput password = page.getHtmlElementById("password");
			password.setValueAttribute("12345678");
			HtmlAnchor anchor = (HtmlAnchor) page.getByXPath(  
		            "//*[@class=\"btn-submit ng-binding\"]").get(0); 
			HtmlPage page2 = anchor.click();  
			System.out.println(page2.asXml());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//好使
	public static void executeMaiMai(){
		try {
			final WebClient webClient = UrlRequest.getWebClient();
			final HtmlPage page1 = webClient.getPage("https://maimai.cn/login?to=http://maimai.cn/edit_info?fr=");

			final HtmlTextInput textField = page1.getElementByName("m");
			final HtmlPasswordInput textField2 = page1.getElementByName("p");

			 final HtmlSubmitInput button =  (HtmlSubmitInput) page1.getByXPath(  
		            "//*[@class=\"loginBtn\"]").get(0); 
			// Change the value of the text field
			textField.setValueAttribute("13621262240");
			textField2.setValueAttribute("00000000");

			// Now submit the form by clicking the button and get back the second page.
			final HtmlPage page2 = button.click();  
			System.out.println(page2.asXml());
			FileUtil.writeToFile("F:/maimai", "maimai", page2.asXml(), false);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ElementNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
