package com.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author huangyi
 * @discription 页面解析工具类 正则表达式解析
 * 
 */
public class RegexParser {

	/**
	 * 清除页面中的script
	 * 
	 * @param inputString
	 * @return
	 */

	public static String clearPageScript(String inputString) {
		String pageContent = "";
		Pattern p_html = Pattern.compile("<script[\\s\\S]*?>[\\s\\S]*?</script>",
				Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(inputString.toLowerCase());
		pageContent = m_html.replaceAll("");
		return pageContent;
	}

	/**
	 * 清除页面中的所有标签项
	 * 
	 * @param inputString
	 * @return
	 */
	public static String clearPageAllTag(String inputString) {
		String pageContent = "";
		Pattern p_html = Pattern.compile("<[\\s\\S].*?>", Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(inputString.toLowerCase());
		pageContent = m_html.replaceAll("");
		return pageContent;
	}

	/**
	 * 清除页面中的css
	 * 
	 * @param inputString
	 * @return
	 */
	public static String clearPageCss(String inputString) {
		String pageContent = "";
		Pattern p_html = Pattern.compile("<style[\\s\\S]*?>[\\s\\S]*?</style>",
				Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(inputString.toLowerCase());
		pageContent = m_html.replaceAll("");
		return pageContent;
	}
	
	/**
	 * 清除页面中的无用标签
	 * 
	 * @param inputString
	 * @return
	 */
	public static String clearPageFilter(String inputString) {
		String pageContent = "";
		Pattern p_html = Pattern.compile("<(a|li).*?>[\\s\\S]*?</a>",
				Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(inputString.toLowerCase());
		pageContent = m_html.replaceAll("");
		return pageContent;
	}
	/**
	 * 通过正则表达式获取数据，返回一个String的值
	 * 
	 * @param content
	 *            html的文档内容
	 * @param reg
	 *            正则表达式
	 * @return
	 */
	public static List<String> getValuesByReg(String content, String reg) {
		List<String> values = new ArrayList<String>();
		String value = "";
		Pattern p = Pattern.compile(reg.substring(1), Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		while (m.find()) {
			if (m.groupCount() >= 1) {
				value = m.group(1);
			} else {
				value = m.group();
			}
			values.add(value);
		}
		return values;
	}


	/**
	 * 获取匹配的正则表达式值返回集合
	 * 
	 * @param inputString
	 * @param regex
	 * @return
	 */
	public static ArrayList<String> searchStr(String inputString, String regex) {
		ArrayList<String> tablelist = new ArrayList<String>();
		Pattern nop = Pattern.compile(regex);
		Matcher m = nop.matcher(inputString);
		while (m.find()) {
			String str=m.group(1).trim();
			if("".equals(str)){
				str="";
			}
			tablelist.add(str);
		}
		return tablelist;
	}

	/**
	 * 获取页面中body部分
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPageBody(String inputString) {
		Pattern nop = Pattern.compile("<body[\\s\\S]*?>([\\s\\S]*?)</body>");
		Matcher m = nop.matcher(inputString.toLowerCase());
		while (m.find()) {
			return new String(m.group(1).trim());
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
	 * 通过正则获取集合内容
	 * 
	 * @param inputString
	 * @param regex
	 * @param size
	 * @param spiltStr
	 * @return
	 */
	public static ArrayList<String> searchStr(String inputString, String regex, int size,
			String spiltStr) {
		ArrayList<String> tablelist = new ArrayList<String>();
		Pattern nop = Pattern.compile(regex);
		Matcher m = nop.matcher(inputString);

		while (m.find()) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i <= size; i++) {
				String s = new String(m.group(i).trim());
				if (s.isEmpty()) {
					s = " ";
				}

				if (i == size) {
					sb.append(s);
				} else {
					sb.append(s).append(spiltStr);
				}

			}
			tablelist.add(sb.toString());
		}
		return tablelist;
	}
	
	/**
     *正则匹配
     * @param s
     * @param pattern
     * @return
     */
     public static boolean matcher(String s, String pattern) { 
    	 Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE); 
    	 Matcher matcher = p.matcher(s);
    	 if (matcher.find()) {
    		 return true;
	     } else {
	    	 return false;
	     }
     }
     public static String baseParse(String content ,String regex,int group) {
 		if(content==null)
 			 throw new  NullPointerException();
 		if(regex==null)
 			 throw new  NullPointerException();
 		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
 		Matcher m = p.matcher(content);
 		if(m.find()) 
 			return	m.group(group);
 		return null;
 	}
 	
 	public static String baseParse(String content ,String regex) {
 		if(content==null||regex==null)
 			 throw new  NullPointerException();
 		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
 		Matcher m = p.matcher(content);
 		if(m.find()) 
 			return	m.group();
 		return null;
 	}
 	
 	public static boolean ismatching(String content ,String regex) {
 		if(content==null||regex==null)
 			 throw new  NullPointerException();
 		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
 		Matcher m = p.matcher(content);
 		if(m.find()) 
 			return	true;
 		return false;
 	}
 	
 	public static List<String> baseParseList(String content ,String regex,int group,int i) {
 		if(content==null||regex==null)
 			 throw new  NullPointerException();
 		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
 		Matcher m = p.matcher(content);
 		List<String> strList=new LinkedList<String>();
 		int sum=0;
 			while(m.find()) 
 				strList.add(m.group(group)+"["+i+","+((++sum))+"]");
 		return strList;
 	}
 	public static List<String> baseParseList(String content ,String regex,int group) {
 		if(content==null||regex==null)
 			 throw new  NullPointerException();
 		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
 		Matcher m = p.matcher(content);
 		List<String> strList=new LinkedList<String>();
 		while(m.find()) 
 			strList.add(m.group(group));
 		return strList;
 	}

 	public static List<String> baseParseList(String content ,String regex) {
 		if(content==null||regex==null)
 			 throw new  NullPointerException();
 		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
 		Matcher m = p.matcher(content);
 		List<String> strList=new LinkedList<String>();
 		while(m.find()) 
 		 strList.add(m.group());
 		return strList;
 	}
 	
 	
 	/***
 	 *  用于带组号  多返回匹配
 	 *  i代表层号，sum代表id号。也就是第几个的意思。用于标识此值是属于那层的第几个
 	 * @param content
 	 * @param regex
 	 * @param group
 	 * @return
 	 */
 	public static List<String> getParseList(String content ,String regex,int group,int sequence) {
 		if(content==null||regex==null)
 			throw new NullPointerException();
 		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
 		Matcher m = p.matcher(content);
 		List<String> strList=new LinkedList<String>();
 			int sum=0;
 			while(m.find()) 
 			{
 				String str=m.group(group);
 				//str=HttpUtil.filterTag(str);
 				//new XiHuLog("[Group:]"+group+"[Regex:]\t"+regex+"[Value:]\t"+str);
 				strList.add(str);
 				sum++;
 				if(sum==sequence)
 					break;
 			}
 		return strList;
 	}
}
