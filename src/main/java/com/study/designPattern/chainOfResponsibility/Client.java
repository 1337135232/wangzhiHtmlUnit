package com.study.designPattern.chainOfResponsibility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

	public static void main(String[] args) {
		
		//假设分店的初始库存都是以下这些东西
		Map<String, Integer> store = new HashMap<String, Integer>();
		store.put("汉堡", 5);
        store.put("薯条", 5);
        store.put("可乐", 5);
        store.put("雪碧", 5);
        
      //假设有5个分店
        McSubbranch mcSubbranch1 = new McSubbranch(0, 0, new HashMap<String, Integer>(store));
        McSubbranch mcSubbranch2 = new McSubbranch(100, 120, new HashMap<String, Integer>(store));
        McSubbranch mcSubbranch3 = new McSubbranch(-100, -120, new HashMap<String, Integer>(store));
        McSubbranch mcSubbranch4 = new McSubbranch(1000, 20, new HashMap<String, Integer>(store));
        McSubbranch mcSubbranch5 = new McSubbranch(-500, 0, new HashMap<String, Integer>(store));
        
        List<McSubbranch> mcSubbranchs = Arrays.asList(mcSubbranch1,mcSubbranch2,mcSubbranch3,mcSubbranch4,mcSubbranch5);
        
      //开始订餐，假设订餐者的坐标是900,20 
        Map<String, Integer> order = new HashMap<String, Integer>();
        order.put("汉堡", 2);
        order.put("可乐", 1);
        order.put("薯条", 1);
        
        print(mcSubbranchs);
        System.out.println("------------------------------------------");
        
      //开始一家一家挨着尝试订餐，直到成功
        for (McSubbranch mcSubbranch : mcSubbranchs) {
            if (mcSubbranch.order(900, 20, order)) {
                System.out.println("订餐成功，接受订单的分店是：" + mcSubbranch);
                break;
            }
        }
        System.out.println("------------------------------------------");
        print(mcSubbranchs);
	}
	
	public static void print(List<McSubbranch> mcSubbranchs){
        for (McSubbranch mcSubbranch : mcSubbranchs) {
            System.out.println("[" + mcSubbranch + "]的库存:" + mcSubbranch.getStore());
        }
    }
}
