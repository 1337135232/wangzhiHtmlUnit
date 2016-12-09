package com.wangzhi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileUtil {
	
	public static String fileSeparator = "/";
	public static String fileSuffix = ".txt";
	/**
	 * @user whq
	 * @date 2016年1月4日
	 * @time 下午1:57:01
	 * @param filePath
	 * @param fileName
	 * @param content
	 * @param append 同名是否覆盖
	 * @throws IOException
	 * @return void
	 * TODO
	 */
	public static void writeToFile(String filePath,String fileName,String content,boolean append) throws IOException{
		File tmpFolder = new File(filePath);
		if (!tmpFolder.exists()){
			tmpFolder.mkdirs();
		}
			
		String regex = "[\"`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regex);      
        Matcher m = p.matcher(fileName); 
        fileName = m.replaceAll("-").trim();
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		FileWriter fileWriter = new FileWriter(filePath+fileSeparator+fileName+fileSuffix, append);
		fileWriter.write(content+"\n");
		fileWriter.flush();
		fileWriter.close();
	}
	
	/**
	 * 读取文件
	 * @param filePath 文件路径（包含文件名称）
	 * @return
	 * @throws IOException
	 * @author audaque
	 * @date 2015年12月10日 下午10:52:51
	 */
	public static String readFromFile(String filePath) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;
		String NL = System.getProperty("line.separator");
		while((line = bufferedReader.readLine())!=null){
			stringBuffer.append(line+NL);
		}
		bufferedReader.close();
		return stringBuffer.toString();
	}
}
