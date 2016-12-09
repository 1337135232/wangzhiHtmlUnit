package com.study.factory;

import java.util.ArrayList;
import java.util.HashMap;

public class CollectionsFactory {

	//私有的构造器，禁止实例化
	private CollectionsFactory(){};
	
	//构建hashMap的创建方法
	public static <K,V> HashMap<K,V> newHashMap(){
		return new HashMap<K,V>();
	}
	
	//构建arrayList的创建方法
	public static <E> ArrayList<E> newArrayList(){
		return new ArrayList<E>();
	}
}
