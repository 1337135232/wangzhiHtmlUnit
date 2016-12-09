package com.study.designPattern.chainOfResponsibility.chainOfResponsibility;

import java.util.Collections;
import java.util.Map;

import com.study.designPattern.chainOfResponsibility.CommonUtils;

//麦当劳分店
public class McSubbranch implements Subbranch{
  
  private final static int MIN_DISTANCE = 500;//假设是500米以内送餐
  
  private static int count;//类计数
  
  private final int number;//分店号
  
  private int x;//分店的横坐标，用于判断距离
  
  private int y;//分店的纵坐标，用于判断距离
  
  private Map<String, Integer> store;//库存
  
  private Subbranch nextSubbranch;//下一家分店
  
  public McSubbranch(int x, int y, Map<String, Integer> store) {
      super();
      this.x = x;
      this.y = y;
      this.store = store;
      number = ++count;
  }
  //设置下一家分店
  public void setSuccessor(Subbranch subbranch) {
      this.nextSubbranch = subbranch;
  }
  //按照职责链处理订单
  public boolean handleOrder(Order order){
      //如果距离小于500米并且订单中的食物不缺货，则订单成功，否则失败
      if (CommonUtils.getDistance(order.getX(), order.getY(), this.x, this.y) < MIN_DISTANCE && !CommonUtils.outOfStock(store, order.getOrder())) {
          for (String name : order.getOrder().keySet()) {
              store.put(name, store.get(name) - order.getOrder().get(name));
          }
          System.out.println("订餐成功，接受订单的分店是：" + this);
          return true;
      }
      if (nextSubbranch == null) {
          return false;
      }
      return nextSubbranch.handleOrder(order);
  }

  public Map<String, Integer> getStore() {
      return Collections.unmodifiableMap(store);
  }
  
  public Subbranch getNextSubbranch() {
      return nextSubbranch;
  }
  
  public String toString() {
      return "麦当劳分店第" + number + "个";
  }
  
  
}
