package com.study.designPattern.Iterator;

//定义：迭代器模式提供一种方法顺序访问一个聚合对象中的各个元素，而又不暴露其内部的表示。
public class Main {
	
	public static void main(String[] args) {
		Menu pancakeHouseMenu = new PancakeHouseMenu();
		Menu restaurantMenu = new RestaurantMenu();
		Waitress w = new Waitress(pancakeHouseMenu,restaurantMenu);
		w.printMenu();
	}
}
