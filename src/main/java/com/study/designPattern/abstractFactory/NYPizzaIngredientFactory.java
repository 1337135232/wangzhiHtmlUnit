package com.study.designPattern.abstractFactory;

import com.study.designPattern.abstractFactory.pojo.Cheese;
import com.study.designPattern.abstractFactory.pojo.Clams;
import com.study.designPattern.abstractFactory.pojo.Dough;
import com.study.designPattern.abstractFactory.pojo.FreshClams;
import com.study.designPattern.abstractFactory.pojo.Garlic;
import com.study.designPattern.abstractFactory.pojo.MarinaraSauce;
import com.study.designPattern.abstractFactory.pojo.Mushroom;
import com.study.designPattern.abstractFactory.pojo.Onion;
import com.study.designPattern.abstractFactory.pojo.Pepperoni;
import com.study.designPattern.abstractFactory.pojo.RedPepper;
import com.study.designPattern.abstractFactory.pojo.ReggianoCheese;
import com.study.designPattern.abstractFactory.pojo.Sauce;
import com.study.designPattern.abstractFactory.pojo.SlicedPepperoni;
import com.study.designPattern.abstractFactory.pojo.ThinCrushDough;
import com.study.designPattern.abstractFactory.pojo.Veggies;

public class NYPizzaIngredientFactory implements PizzaIngredientFactory {
    // 对于原料家族内的每一种原料，我们都提供了纽约的版本
    @Override
    public Dough createDough() {
        return new ThinCrushDough();
    }

    @Override
    public Sauce createSauce() {
        return new MarinaraSauce();
    }

    @Override
    public Cheese createCheese() {
        return new ReggianoCheese();
    }

    @Override
    public Veggies[] createVeggies() {
        Veggies veggies[] = {new Garlic(), new Onion(), new Mushroom(), new RedPepper()};
        return veggies;
    }

    @Override
    public Pepperoni createPepperoni() {
        // 这是切片的意式腊肠，纽约和芝加哥都会用到它
        return new SlicedPepperoni();
    }

    @Override
    public Clams createClams() {
        // 纽约靠海，所以有新鲜的蛤蜊。芝加哥就必须使用冷冻的蛤蜊
        return new FreshClams();
    }
}
