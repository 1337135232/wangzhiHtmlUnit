package com.study.designPattern.state.state;



public class SpeedUpState implements RunState{

	@Override
	public void run(Hero hero) {
		System.out.println("--------------加速跑动---------------");
        try {
            Thread.sleep(4000);//假设加速持续4秒
        } catch (InterruptedException e) {}
        hero.setState(Hero.COMMON);
        System.out.println("------加速状态结束，变为正常状态------");
	}


}
