package com.study.designPattern.chainOfResponsibility.chainOfResponsibility;

import java.util.HashMap;
import java.util.Map;

public class Client {

    public static void main(String[] args) {
        //假设初始库存都是以下这些东西
        Map<String, Integer> store = new HashMap<String, Integer>();
        store.put("汉堡", 5);
        store.put("薯条", 5);
        store.put("可乐", 5);
        store.put("雪碧", 5);
        //假设有5个分店
        Subbranch mcSubbranch1 = new McSubbranch(0, 0, new HashMap<String, Integer>(store));
        Subbranch mcSubbranch2 = new McSubbranch(100, 120, new HashMap<String, Integer>(store));
        Subbranch mcSubbranch3 = new McSubbranch(-100, -120, new HashMap<String, Integer>(store));
        Subbranch mcSubbranch4 = new McSubbranch(1000, 20, new HashMap<String, Integer>(store));
        Subbranch mcSubbranch5 = new McSubbranch(-500, 0, new HashMap<String, Integer>(store));
        
        //注册5个分店
        OrderManager.getOrderManager().registerSubbranch(mcSubbranch1,mcSubbranch2,mcSubbranch3,mcSubbranch4,mcSubbranch5);
        
        //开始订餐，假设订餐者的坐标是900,20 
        Map<String, Integer> order = new HashMap<String, Integer>();
        order.put("汉堡", 2);
        order.put("可乐", 1);
        order.put("薯条", 1);
        
        print(mcSubbranch1);
        System.out.println("------------------------------------------");
        
        //开始订餐，直接找订餐管理部门订餐
        OrderManager.getOrderManager().handleOrder(new Order(900, 20, order));
        
        System.out.println("------------------------------------------");
        print(mcSubbranch1);
    }
    
    public static void print(Subbranch subbranch){
        if (subbranch == null ) {
            return;
        }
        do {
            if (subbranch instanceof McSubbranch) {
                System.out.println("[" + subbranch + "]的菜单:" + ((McSubbranch) subbranch).getStore());
            }
        } while ((subbranch = ((McSubbranch) subbranch).getNextSubbranch()) != null);
    }
    
}
