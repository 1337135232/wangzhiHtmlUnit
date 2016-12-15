package com.wangzhi;

import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.math.NumberUtils;

import com.study.factory.CollectionsFactory;
import com.util.HttpRequest;
import com.util.RegexParser;

public class CenterBank {

	private static final String url = "http://srh.bankofchina.com/search/whpj/search.jsp";
	private static final String regex = "<td>美元</td>[\\s\\S]*?<td>(.*?)</td>";
	
	public static void main(String[] args) {
		while(true){
			execute();
			try {
				Thread.sleep(1000*30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static void execute() {
		try {
			HttpRequest request = new HttpRequest();
			Map<String,String> paramaters = CollectionsFactory.newHashMap();
			paramaters.put("erectDate","");
			paramaters.put("nothing","");
			paramaters.put("pjname","1316");
			String pageContent = request.post(url, paramaters);
			Double num = NumberUtils.createDouble(RegexParser.getPageByRegex(pageContent, regex));
			if(num.compareTo(692.1d)>0){
				JOptionPane.showMessageDialog(null, "现在的美元价格是"+num);
			}
			System.out.println(num);
//		JOptionPane.showMessageDialog(null, "aaaa+现在的美元价格是"+num);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
