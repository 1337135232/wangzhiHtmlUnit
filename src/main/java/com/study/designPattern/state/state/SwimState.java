package com.study.designPattern.state.state;


public class SwimState implements RunState{

	@Override
	public void run(Hero hero) {
		// TODO Auto-generated method stub
		System.out.println("--------------不能跑动---------------");
        try {
            Thread.sleep(2000);//假设眩晕持续2秒
        } catch (InterruptedException e) {}
        hero.setState(Hero.COMMON);
        System.out.println("------眩晕状态结束，变为正常状态------");
	}

}
