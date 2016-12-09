package com.study.effective.java3;
//单例模式2
public class Elvis2 {
	
	private static final Elvis2 INSTANCE = new Elvis2();
	
	private Elvis2(){}
	
	public static Elvis2 getInstance(){
		return INSTANCE;
	}
	public void leavTheBuilding(){
		System.out.println("test");
	}
}
