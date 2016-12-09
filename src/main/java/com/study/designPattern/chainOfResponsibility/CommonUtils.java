package com.study.designPattern.chainOfResponsibility;

import java.util.Map;

//简单的工具类
public class CommonUtils {
  
  private CommonUtils(){}

  //计算坐标之间的距离
  public static double getDistance(int x1,int y1,int x2,int y2){
      return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }
  
  //查看库存是否能满足订单
  public static boolean outOfStock(Map<String, Integer> store,Map<String, Integer> order){
	  //订单为空
      if (order == null || order.size() == 0) {
          return false;
      }
      //库存为空
      if (store == null || store.size() == 0) {
          return true;
      }
      //库存减去订单，查看库存是否能满足订单
      for (String name : order.keySet()) {
          if (store.get(name) - order.get(name) < 0) {
              return true;
          }
      }
      return false;
  }
  
}
