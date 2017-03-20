package com.study.designPattern.factory;

/**
 * 
 工厂方法模式（Factory Method Pattern）通过让子类决定该创建的对象是什么，来达到将对象创建的过程封装的目的。
PizzaStore就是创建者（Creator）类。它定义了一个抽象的工厂方法，让子类实现此方法制造产品。
创建者通常会包含依赖于抽象产品的代码，而这些抽象产品由子类制造。创建者不需要真的知道在制造哪种具体产品。
能够产生产品的类称为具体创建者。NYPizzaStore和ChicagoPizzaStore就是具体创建者。
Pizza是产品类。工厂生产产品，对PizzaStore来说，产品就是Pizza。
抽象的Creator提供了一个创建对象的方法的接口，也称为“工厂方法”。在抽象的Creator中，任何其他实现的方法，都可能使用到这个工厂方法所制造出来的产品，但只有子类真正实现这个工厂方法并创建产品。
 * @author wangzhi
 * 2017年2月22日
 */
public class UMLModel {

	// Creator是一个类，它实现了所有操纵产品的方法，但不实现工厂方法
	public abstract class Creator{
	    void anOperation(){
	        // ...
	    }
	    // Creator的所有子类都必须实现这个抽象的factoryMethod()方法
	    abstract void factoryMethod();
	}

	// 具体的创建者
	public class ConcreteCreator extends Creator{
	    // ConcreteCreator实现了factoryMethod()，以实际制造出产品。
	    @Override
	    void factoryMethod() {
	        // ...
	    }
	}

	// 所有产品必须实现这个接口，这样一来，
	// 使用这些产品的类就可以引用这个接口，而不是具体的类
	public abstract class Product{
	    void operation(){
	        // ...
	    }
	}

	// 具体的产品
	public class ConcreteProduct extends Product{
	}
}
