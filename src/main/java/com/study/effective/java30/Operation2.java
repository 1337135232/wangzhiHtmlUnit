package com.study.effective.java30;

/**
 * 解决Operation的问题
 * 
 * 因为添加了抽象方法，所以每一个新添加的枚举常量都得实现抽象方法，防止遗忘
 * @author ufenqi
 *
 */
public enum Operation2 {

	//加减乘除
	PLUS {double apply(double x,double y){return x+y;}},
	MINUS {double apply(double x,double y){return x-y;}},
	TIMES {double apply(double x,double y){return x*y;}},
	DIVIDE {double apply(double x,double y){return x/y;}};
	
	
	abstract double apply(double x,double y);
}
