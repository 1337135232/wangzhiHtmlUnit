package com.study.effective.java30;

/**
 * 此程序在添加新的枚举常量时，容易忘记添加apply中的case语句
 * @author ufenqi
 *
 */
public enum Operation {

	PLUS,MINUS,TIMES,DIVIDE;//加减乘除
	
	double apply(double x,double y){
		switch(this){
			case PLUS: return x+y;
			case MINUS:return x-y;
			case TIMES:return x*y;
			case DIVIDE:return x/y;
		}
		throw new AssertionError("Unknown op:"+this);
	}
}
