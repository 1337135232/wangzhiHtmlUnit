package com.study.effective.java3;
//单例模式1
public class Elvis {
	
	public static final Elvis INSTANCE = new Elvis();
	
	private Elvis(){}
	
	public void leavTheBuilding(){
		System.out.println("test");
	}
}
