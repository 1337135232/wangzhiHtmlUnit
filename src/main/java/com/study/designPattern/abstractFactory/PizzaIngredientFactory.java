package com.study.designPattern.abstractFactory;

import com.study.designPattern.abstractFactory.pojo.Cheese;
import com.study.designPattern.abstractFactory.pojo.Clams;
import com.study.designPattern.abstractFactory.pojo.Dough;
import com.study.designPattern.abstractFactory.pojo.Pepperoni;
import com.study.designPattern.abstractFactory.pojo.Sauce;
import com.study.designPattern.abstractFactory.pojo.Veggies;

/**
 * 披萨原来工厂接口
 * @author wangzhi
 * 2017年2月22日
 */
public interface PizzaIngredientFactory {

	// 在接口中，每个原料都有一个对应的方法创建该原料
    public Dough createDough();
    public Sauce createSauce();
    public Cheese createCheese();
    public Veggies[] createVeggies();
    public Pepperoni createPepperoni();
    public Clams createClams();
}
