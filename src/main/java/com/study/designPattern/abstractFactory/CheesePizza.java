package com.study.designPattern.abstractFactory;

public class CheesePizza extends Pizza {
    PizzaIngredientFactory ingredientFactory;
    
    // 要制作披萨，需要工厂提供原料。
    // 所以每个披萨类都需要从构造器参数中得到一个工厂，并把这个工厂存储在一个实例变量中。
    public CheesePizza(PizzaIngredientFactory ingredientFactory){
        this.ingredientFactory = ingredientFactory;
    }
    
    @Override
    void prepare() {
        // prepare()方法一步一步地创建芝士披萨，每当需要原料时，就跟工厂要。
        dough = ingredientFactory.createDough();
        sauce = ingredientFactory.createSauce();
        cheese = ingredientFactory.createCheese();
    }
}
