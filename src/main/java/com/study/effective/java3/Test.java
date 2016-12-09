package com.study.effective.java3;

public class Test {

	public static void main(String[] args) {
		testElvis();
		System.out.println("====================");
		testElvis2();
		System.out.println("====================");
		testElvis3();
	}
	
	public static void testElvis(){
		Elvis t = Elvis.INSTANCE;
		Elvis t2 = Elvis.INSTANCE;
		if(t == t2){
			System.out.println("t == t2");
		}
		t.leavTheBuilding();
	}
	public static void testElvis2(){
		Elvis2 t = Elvis2.getInstance();
		Elvis2 t2 = Elvis2.getInstance();
		if(t == t2){
			System.out.println("t == t2");
		}
		t.leavTheBuilding();
	}
	public static void testElvis3(){
		Elvis3 t = Elvis3.INSTANCE;
		Elvis3 t2 = Elvis3.INSTANCE;
		if(t == t2){
			System.out.println("t == t2");
		}
		t.leavTheBuilding();
	}
}
