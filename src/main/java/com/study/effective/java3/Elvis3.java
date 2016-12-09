package com.study.effective.java3;
//单例模式3 通过枚举类型  推荐使用本方法
public enum Elvis3 {
	INSTANCE;
	
	public void leavTheBuilding(){
		System.out.println("test");
	}
}
