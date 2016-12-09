package com.study.effective.java27;

import java.util.Iterator;
import java.util.List;

/**
 * max方法不会测试，暂时无法写出测试程序
 * @author wangzhi  2016 11 20
 *
 */
public class ImpComparable {

	
	//根据元素自然顺序计算列表的最大值
	public static <T extends Comparable<T>> T max(List<T> list){
		
		Iterator<T> i = list.iterator();
		T result = i.next();
		while(i.hasNext()){
			T t = i.next();
			if(t.compareTo(result)>0){
				result = t;
			}
		}
		return result;
	}
	
}
