package com.study.designPattern.abstractFactory;

import com.study.designPattern.abstractFactory.pojo.Cheese;
import com.study.designPattern.abstractFactory.pojo.Clams;
import com.study.designPattern.abstractFactory.pojo.Dough;
import com.study.designPattern.abstractFactory.pojo.Pepperoni;
import com.study.designPattern.abstractFactory.pojo.Sauce;
import com.study.designPattern.abstractFactory.pojo.Veggies;

public abstract class Pizza {
    String name;
    // 每个披萨都持有一组在准备时会用到的原料
    Dough dough;
    Sauce sauce;
    Veggies veggies[];
    Cheese cheese;
    Pepperoni pepperoni;
    Clams clams;
    
    // 现在把prepare()方法声明成抽象。在这个方法中，我们需要收集披萨所需的原料，而这些原料当然是来自原料工厂了。
    abstract void prepare();
    
    // 其他的方法保持不动
    void bake(){
        // ...
    }
    
    void cut(){
        // ...
    }
    
    void box(){
        // ...
    }
}
