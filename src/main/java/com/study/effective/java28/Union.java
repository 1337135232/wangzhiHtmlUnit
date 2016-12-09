package com.study.effective.java28;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 将java27中的Union类中的union方法修改为使用通配符
 * @author ufenqi
 *
 */
public class Union {

	public static <E> Set<E> union(Set<? extends E> s1,Set<? extends E> s2){
		Set<E> result = new HashSet<E>(s1);
		result.addAll(s2);
		return result;
	}
	
	public static void main(String[] args) {
		Set<Integer> guys = new HashSet<Integer>(
				Arrays.asList(1,2,3));
		
		Set<Double> stooges = new HashSet<Double>(
				Arrays.asList(1d,2d,3d));
		
		Set<Number> aflCio = Union.<Number>union(guys, stooges);
		System.out.println(aflCio);
		
		Set<Integer> stooges1 = new HashSet<Integer>(
				Arrays.asList(10,20,30));
		
		aflCio = Union.<Number>union(guys, stooges1);
		System.out.println(aflCio);
	}
}
