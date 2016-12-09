package com.wangzhi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class IOPageContent {
	
	public static String execute(String filePath){
		File file = new File(filePath);
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			String NL = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				sb.append(line + NL);
			}
			reader.close();
			//System.out.println(sb.toString());
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static void main(String[] args) {
	}
}
