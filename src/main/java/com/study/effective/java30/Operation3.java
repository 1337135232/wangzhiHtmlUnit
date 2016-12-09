package com.study.effective.java30;

import java.util.HashMap;
import java.util.Map;

/**
 * 解决Operation的问题
 * 
 * 因为添加了抽象方法，所以每一个新添加的枚举常量都得实现抽象方法，防止遗忘
 * @author ufenqi
 *
 */
public enum Operation3 {

	//加减乘除
	PLUS("+") {double apply(double x,double y){return x+y;}},
	MINUS("-") {double apply(double x,double y){return x-y;}},
	TIMES("*") {double apply(double x,double y){return x*y;}},
	DIVIDE("/") {double apply(double x,double y){return x/y;}};
	
	abstract double apply(double x,double y);
	
	private final String symbol;//符号
	
	private Operation3(String symbol) {
		this.symbol = symbol;
	}
	
	@Override  //目的是打印出对象的值，而不是对象自己。即打印+ 而不是打印PLUS     注释掉此方法和不注释显示结果不同
	public String toString(){
		return symbol;
	}
	
	private static final Map<String,Operation3> stringToEnum = new HashMap<String,Operation3>();
	static{
		for(Operation3 o:Operation3.values()){
			stringToEnum.put(o.toString(), o);//+ 与 PLUS对应放入到map中
		}
	}
	
	//根据+ 从map中取出PLUS
	public static Operation3 fromString(String symbol){
		return stringToEnum.get(symbol);
	}
	
	public static void main(String[] args) {
		double x = Double.parseDouble("2");
		double y = Double.parseDouble("4");
		
		for(Operation3 o:Operation3.values()){
			System.out.printf("%f %s %f = %f%n",x,o,y,o.apply(x, y));
		}
	}
}
