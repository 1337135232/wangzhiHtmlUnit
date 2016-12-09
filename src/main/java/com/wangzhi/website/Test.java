package com.wangzhi.website;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.util.RegexParser;
import com.util.UnicodeConverter;
import com.wangzhi.IOPageContent;

public class Test {
	public static void main(String[] args) {
		String pageContent = IOPageContent.execute("F:/maimai/contacts.txt");
		String s = UnicodeConverter.decodeUnicode(pageContent);
		String regex = "\"id\":([\\s\\S]*?),";
		ArrayList<String> result = RegexParser.searchStr(s, regex);
		System.out.println(result);
	}
}
