package com.study.designPattern.factory;

import com.study.designPattern.factory.pojo.ChicagoCheesePizza;
import com.study.designPattern.factory.pojo.ChicagoClamPizza;
import com.study.designPattern.factory.pojo.ChicagoPepperoniPizza;
import com.study.designPattern.factory.pojo.ChicagoVeggiePizza;
import com.study.designPattern.factory.pojo.Pizza;

//类似的，利用芝加哥子类，我们得到了带芝加哥原料的createPizza()实现
public class ChicagoStylePizzaStore extends PizzaStore{

	@Override
	public Pizza createPizza(String type) {
		// TODO Auto-generated method stub
		Pizza pizza = null;

        if (type.equals("cheese")) {
            pizza = new ChicagoCheesePizza();
        } else if (type.equals("pepperoni")) {
            pizza = new ChicagoPepperoniPizza();
        } else if (type.equals("clam")) {
            pizza = new ChicagoClamPizza();
        } else if (type.equals("veggie")) {
            pizza = new ChicagoVeggiePizza();
        }
        return pizza;
	}

}
