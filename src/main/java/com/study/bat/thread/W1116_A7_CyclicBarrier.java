package com.study.bat.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier这个类是为了帮助猿友们方便的实现多个线程一起启动的场景，就像赛跑一样，只要大家都准备好了，那就开始一起冲
 * @author wangzhi
 *
 */
public class W1116_A7_CyclicBarrier {

	public static void main(String[] args) {
		
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
		
		for(int i=0;i<10;i++){
			final int number = i+1;
			Runnable runnable = new Runnable(){

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("等待执行任务[" + number + "]");
					try {
						cyclicBarrier.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("开始执行任务[" + number + "]");
				}
				
			};
			
			Thread thread = new Thread(runnable);
			
			thread.start();
		}
		
	}
}
