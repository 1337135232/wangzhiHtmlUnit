package com.study.designPattern.Iterator;

import java.util.Iterator;

/**
 * 服务员类 负责打印煎饼屋和餐厅的菜单
 * @author wangzhi
 * 2017年2月22日
 */
public class Waitress {
    private Menu pancakeHouseMenu;
    private Menu restaurantMenu;
    
    // 在构造器中，女招待照顾两个菜单
    public Waitress(Menu pancakeHouseMenu, Menu restaurantMenu) {
        this.pancakeHouseMenu = pancakeHouseMenu;
        this.restaurantMenu = restaurantMenu;
    }
    
    public void printMenu() {
        // 这个printMenu()方法为每一个菜单各自创建一个迭代器
        Iterator<MenuItem> pancakeIterator = pancakeHouseMenu.createIterator();
        Iterator<MenuItem> restaurantIterator = restaurantMenu.createIterator();
        // 然后调用重载的printMenu()，将迭代器传入
        printMenu(pancakeIterator);
        printMenu(restaurantIterator);
    }
    
    // 这个重载的printMenu()方法，使用迭代器来遍历菜单项并打印出来
    private void printMenu(Iterator<?> iterator) {
        while (iterator.hasNext()) {
            MenuItem menuItem = (MenuItem) iterator.next();
            System.out.println(menuItem.getName() + " " + 
                    menuItem.getPrice() + " " + menuItem.getDescription());
        }
    }
}
