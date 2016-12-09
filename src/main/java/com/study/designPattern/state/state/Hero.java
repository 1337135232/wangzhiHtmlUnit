package com.study.designPattern.state.state;

public class Hero {

	public static final RunState COMMON = new CommonState();//正常状态
	private static final RunState SPEED_UP = new SpeedUpState();//加速状态
	private static final RunState SPEED_DOWN = new SpeedDownState();//减速状态
	private static final RunState SWIM = new SwimState();//眩晕状态
	
	private RunState state = COMMON;//默认正常状态
	
	private Thread runThread;//跑动线程
	
	//设置状态
	public void setState(RunState state){
		this.state = state;
	}
	
	private boolean isRunning(){
		return runThread != null && !runThread.isInterrupted();
	}
	//停止跑动
	public void stopRun(){
		if(isRunning()){
			runThread.interrupt();
			System.out.println("------停止跑动-------");
		}
	}
	
	public void startRun(){
		if(isRunning()){
			return;
		}
		
		final Hero hero = this;
		runThread = new Thread(new Runnable(){
			public void run(){
				while(!runThread.isInterrupted()){
					state.run(hero);
				}
			}
		});
		
		 System.out.println("--------------开始跑动---------------");
	     runThread.start();
	}
	
    public static void main(String[] args) throws InterruptedException {
    	Hero hero = new Hero();
        hero.startRun();
        hero.setState(Hero.SPEED_UP);
        Thread.sleep(5000);
        hero.setState(Hero.SPEED_DOWN);
        Thread.sleep(5000);
        hero.setState(Hero.SWIM);
        Thread.sleep(5000);
        hero.stopRun();
	}
}
