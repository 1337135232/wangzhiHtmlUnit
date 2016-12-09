package com.study.effective.java28;

import java.util.Iterator;
import java.util.List;

/**
 * use tongpeifu
 * @author wangzhi  2016 11 20
 *
 */
public class ImpComparable {
	
	//根据元素自然顺序计算列表的最大值
	public static <T extends Comparable<? super T>> T max(List<? extends T> list){
		
		Iterator<? extends T> i = list.iterator();
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
