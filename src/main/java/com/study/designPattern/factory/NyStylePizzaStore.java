package com.study.designPattern.factory;

import com.study.designPattern.factory.pojo.NyStyleCheesePizza;
import com.study.designPattern.factory.pojo.NyStyleClamPizza;
import com.study.designPattern.factory.pojo.NyStylePepperoniPizza;
import com.study.designPattern.factory.pojo.NyStyleVeggiePizza;
import com.study.designPattern.factory.pojo.Pizza;

//如果加盟店为顾客提供纽约风味的披萨，就使用NyStylePizzaStore，
//因为此类的createPizza()方法会建立纽约风味的披萨
public class NyStylePizzaStore extends PizzaStore{

	@Override
	public Pizza createPizza(String type) {
		// TODO Auto-generated method stub
		Pizza pizza = null;

        if (type.equals("cheese")) {
            pizza = new NyStyleCheesePizza();
        } else if (type.equals("pepperoni")) {
            pizza = new NyStylePepperoniPizza();
        } else if (type.equals("clam")) {
            pizza = new NyStyleClamPizza();
        } else if (type.equals("veggie")) {
            pizza = new NyStyleVeggiePizza();
        }
        return pizza;
	}

}
