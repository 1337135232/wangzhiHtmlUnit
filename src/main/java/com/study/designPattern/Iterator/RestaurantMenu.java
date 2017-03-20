package com.study.designPattern.Iterator;

import java.util.Iterator;

/**
 * 餐厅的菜单实现
 * @author wangzhi 2017年2月22日
 */
public class RestaurantMenu implements Menu{

	// 餐厅采用使用的是数组，所以可以控制菜单的长度，
	// 并且在取出菜单项时，不需要转型
	private static final int MAX_ITEMS = 6;
	private int numberOfItems = 0;
	private MenuItem[] menuItems;

	public RestaurantMenu() {
		menuItems = new MenuItem[MAX_ITEMS];

		// 和煎饼屋一样，餐厅使用addItem()辅助方法在构造器中创建菜单项的
		addItem("Vegetarian BLT",
				"(Fakin') Bacon with lettuce & tomato on whole wheat", true,
				2.99);
		addItem("BLT", "Bacon with lettuce & tomato on whole wheat", false,
				2.99);
		addItem("Soup of the day",
				"Soup of the day, with a side of potato salad", false, 3.29);
		addItem("Hotdog",
				"A hot dog, with saurkraut, relish, onions, topped with cheese",
				false, 3.05);
	}

	public void addItem(String name, String description, boolean vegetarian,
			double price) {
		// 餐厅坚持让菜单保持在一定的长度之内
		MenuItem menuItem = new MenuItem(name, description, vegetarian, price);
		if (numberOfItems >= MAX_ITEMS) {
			System.err.println("Sorry, menu is full! Can't add item to menu");
		} else {
			menuItems[numberOfItems] = menuItem;
			numberOfItems = numberOfItems + 1;
		}
	}

	// getMenuItems()返回一个菜单项的数组
	public MenuItem[] getMenuItems() {
		return menuItems;
	}

	//创建迭代器，从而将数组的结构隐藏起来
	public Iterator<MenuItem> createIterator(){
		return new RestaurantIterator();
	}
	// 正如煎饼屋那样，这里还有很多其他的菜单代码依赖于这个数组
	// ...
	
	private class RestaurantIterator implements Iterator<MenuItem>{

		private int position = 0;
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			if(position>=menuItems.length||menuItems[position]==null){
				return false;
			}else{
				return true;
			}
		}
		@Override
		public MenuItem next() {
			// TODO Auto-generated method stub
			MenuItem menuItem = menuItems[position];
			position++;
			return menuItem;
		}
		@Override
		public void remove() {
			// TODO Auto-generated method stub
			if (position <= 0) {
	            throw new IllegalStateException("You can't remove an item until you've done at least one next()");
	        }
			if(menuItems[position-1]!=null){
				for(int i=position;i<menuItems.length-1;i++){
					menuItems[i] = menuItems[i+1];
				}
				menuItems[menuItems.length-1] = null;
			}
		}
	}
}

