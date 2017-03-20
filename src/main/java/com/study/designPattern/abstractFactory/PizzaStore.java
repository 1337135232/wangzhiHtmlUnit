package com.study.designPattern.abstractFactory;


/**
 * 抽象的披萨店，让各个地区的披萨店实现
 * @author wangzhi
 * 2017年2月22日
 */
public abstract class PizzaStore {

	//预定披萨
	public Pizza orderPizza(String type){
		
		Pizza pizza = createPizza(type);
		
		// 准备面皮，加调料等
	    pizza.prepare();
	    // 烘烤
	    pizza.bake();
	    // 切片
	    pizza.cut();
	    // 装盒
	    pizza.box();
	    
		return pizza;
		
	}
	
	// 现在把工厂对象移到这个方法中
    // 在PizzaStore里，“工厂方法”现在是抽象的
	//子类中实现
	abstract Pizza createPizza(String type);
}
