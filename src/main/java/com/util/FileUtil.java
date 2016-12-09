package com.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class FileUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileUtil.class);

	public static String fileSeparator = "/";
	public static String fileSuffix = ".txt";
	// 存储路径，自定义
	//private static final String basePath = "D:/logs/p2pblack/imgs";
	
	
	
	
	
	/**
	 * 
	 * @param filePath:文件所在的文件夹
	 * @param fileName：文件名
	 * @param subfix:图片后缀 .png .jpg等
	 * @param httpResponse:请求得到的response
	 * @param append：同名是否覆盖
	 * @return
	 *@author weirongzhi
	 *@2016年10月8日下午6:30:42
	 */
	public static File writeResponseToFile(String filePath, String fileName,String suffix,
			HttpResponse httpResponse, boolean append){
		if (filePath.lastIndexOf("/")!=filePath.length()) {
			filePath=filePath+"/";
		}
		File dir=new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
			 System.out.println("文件/图片存放于"+filePath+"目录下");
		      logger.info("文件/图片存放于"+filePath+"目录下");
		}
		BufferedOutputStream  bufo=null;
		File img=new File(filePath+fileName+suffix);
		try {
			byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
			bufo=new BufferedOutputStream(new FileOutputStream(img, append));
			bufo.write(bytes, 0, bytes.length);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if (bufo!=null) {
				try {
					bufo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 将byte数组转换成图片
	 * @param filePath:文件所在的文件夹
	 * @param fileName：文件名
	 * @param subfix:图片后缀 .png .jpg等
	 * @param bytes
	 * @param append：同名是否覆盖
	 *@author weirongzhi
	 *2016年9月22日上午11:04:36
	 */
	public static File writeToImg(String filePath, String fileName,String suffix,
			byte[] bytes, boolean append){
		if (filePath.lastIndexOf("/")!=filePath.length()) {
			filePath=filePath+"/";
		}
		File dir = new File(filePath);
	    if(!dir.exists()){
	      dir.mkdirs();
	      System.out.println("图片存放于"+filePath+"目录下");
	      logger.info("图片存放于"+filePath+"目录下");
	    }
		BufferedOutputStream  bufo=null;
		File img=new File(filePath+fileName+suffix);
		try {
			bufo=new BufferedOutputStream(new FileOutputStream(img, append));
			bufo.write(bytes, 0, bytes.length);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if (bufo!=null) {
				try {
					bufo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * @user whq
	 * @date 2016年1月4日
	 * @time 下午1:57:01
	 * @param filePath
	 * @param fileName
	 * @param content
	 * @param append
	 *            同名是否覆盖
	 * @throws IOException
	 * @return void TODO
	 */
	public static void writeToFile(String filePath, String fileName,
			String content, boolean append) throws IOException {
		File tmpFolder = new File(filePath);
		if (!tmpFolder.exists()) {
			tmpFolder.mkdirs();
		}

		String regex = "[\"`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(fileName);
		fileName = m.replaceAll("-").trim();
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		FileWriter fileWriter = new FileWriter(filePath + fileSeparator
				+ fileName + fileSuffix, append);
		fileWriter.write(content + "\n");
		fileWriter.flush();
		fileWriter.close();
	}

	/**
	 * 读取文件
	 * 
	 * @param filePath
	 *            文件路径（包含文件名称）
	 * @return
	 * @throws IOException
	 * @author audaque
	 * @date 2015年12月10日 下午10:52:51
	 */
	public static String readFromFile(String filePath) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(
				new File(filePath)));
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;
		String NL = System.getProperty("line.separator");
		while ((line = bufferedReader.readLine()) != null) {
			stringBuffer.append(line + NL);
		}
		bufferedReader.close();
		return stringBuffer.toString();
	}

	
	/**
	 * 读取文件流
	 * 
	 * @param is
	 * @param charset
	 * @return
	 */
	public List<String> readFileLines(InputStream is, String charset) {
		if (charset == null)
			throw new NullPointerException();
		List<String> lines = new ArrayList<String>();
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(is, charset);
			br = new BufferedReader(isr);
			while (br.ready())
				lines.add(br.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != br) {
					br.close();
				}
				if (null != isr) {
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lines;
	}
	/**
	 * 根据文件名称查找文件流
	 * 
	 * @param fileName
	 * @return
	 */
	public InputStream getInputStream(String fileName) {
		InputStream is = null;
		try {
			// 读取class路径下面的文件
			is = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
		} catch (Exception e) {
			is = getInputStreamFromConf(fileName);
		}
		if (null == is) {
			is = getInputStreamFromConf(fileName);
		}
		return is;
	}
	
	/**
	 * 从conf中获取配置文件流
	 * 
	 * @param fileName
	 * @return
	 */
	public InputStream getInputStreamFromConf(String fileName) {
		InputStream is = null;
		// 读取conf路径下面的文件
		File conf = new File(FileUtil.getResourcePath());
		File file = new File(conf, fileName);
		try {
			is = new FileInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}


	/**
	 * 获取资源路径
	 * 
	 * @return
	 */
	public static String getResourcePath() {
		String path = "";
		// 读取conf路径下面的文件
		try {
			File conf = new File(new File(new FileUtil().getClassPath()).getParentFile(), "conf");
			if (conf.isDirectory()) {
				path = conf.getAbsolutePath();
			} else {
				path = FileUtil.class.getClassLoader().getResource("").getFile().toString()+"conf/";
			}
		} catch (Exception e) {
			logger.info("资源路径未找到！");
		}

		return path;
	}
	/****
	 * 获取classPath
	 */
	public String getClassPath() {
		URL url = FileUtil.class.getProtectionDomain().getCodeSource().getLocation(); // Gets
		String jarPath = null;
		try {
			jarPath = URLDecoder.decode(url.getFile(), "UTF-8"); // Should fix
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String parentPath = new File(jarPath).getParentFile().getPath(); // Path
		parentPath = parentPath + File.separator;
		return parentPath;
	}
	public void method1(File file, String text) {
		FileWriter fw = null;
		try {
			// 如果文件存在，则追加内容；如果文件不存在，则创建文件
			fw = new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(text);
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 追加写入文件
	 * 
	 * @param file
	 * @param text
	 */
	public void appendFile(File file, String text) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(text);
		} catch (Exception e) {
			logger.info("文件加载错误，未找到文件" + file.getName());
		} finally {
			try {
				if (null != out) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {

			}
		}
	}
	public void writeFile(String pathName, String text) {
		try {
			FileWriter fw = new FileWriter(new File(pathName));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取文件流
	 * 
	 * @param is
	 * @param charset
	 * @return
	 */
	public HashSet<String> readFileLinesToSet(InputStream is, String charset) {
		if (charset == null)
			throw new NullPointerException();
		HashSet<String> lines = new HashSet<String>();
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(is, charset);
			br = new BufferedReader(isr);
			while (br.ready())
				lines.add(br.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lines;
	}
}
