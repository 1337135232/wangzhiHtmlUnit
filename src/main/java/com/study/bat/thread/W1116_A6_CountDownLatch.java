package com.study.bat.thread;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch这个类是为了帮助猿友们方便的实现一个这样的场景，就是某一个线程需要等待其它若干个线程完成某件事以后才能继续进行
 * @author wangzhi
 *
 */
public class W1116_A6_CountDownLatch {

	/**
	 * CountDownLatch(10)与for(int i=0;i<10;i++){要相等，也就是都要为10。
	 * 否则，如果线程数为10，循环为5，则主线程会一直等待，等到有10个线程执行完才会执行。
	 * 如果线程数为10，循环为50，则主线程会在执行完10个线程后继续进行
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		
		final CountDownLatch countDownLatch = new CountDownLatch(10);//设置执行的线程数
		
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
					System.out.println("执行任务["+number+"]");
					countDownLatch.countDown();
					System.out.println("完成任务[" + number + "]");
				}
				
			};
			
			Thread thread = new Thread(runnable);
			
			thread.start();
		}
		
		System.out.println("主线程开始等待...");
        countDownLatch.await();
        System.out.println("主线程执行完毕...");
	}
}

/**
 * 这个程序的主线程会等待CountDownLatch进行10次countDown方法的调用才会继续执行。
 * 我们可以从打印的结果上看出来，尽管有的时候完成任务的打印会出现在主线程执行完毕之后，
 * 但这只是因为countDown已经执行完毕，主线程的打印语句先一步执行而已。
 */
