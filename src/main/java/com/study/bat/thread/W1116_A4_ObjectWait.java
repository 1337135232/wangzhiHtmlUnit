package com.study.bat.thread;

/**
 * JVM内部的条件等待机制。Java当中的类有一个共同的父类Object，而在Object中，有一个wait的本地方法
 * 
 * @author wangzhi
 * 
 */
public class W1116_A4_ObjectWait {

	private volatile static boolean lock;

	public static void main(String[] args) throws InterruptedException {

		final Object object = new Object();

		Thread thread1 = new Thread(new Runnable() {

			@Override
			public void run() {

				System.out.println("等待被通知！");

				try {
					synchronized (object) {

						while (!lock) {

							object.wait();
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e);
				}

				System.out.println("已被通知");
			}
		});

		Thread thread2 = new Thread(new Runnable() {

			@Override
			public void run() {

				System.out.println("马上开始通知！");

				synchronized (object) {

					object.notify();
					lock = true;

				}
				System.out.println("已通知");
			}
		});

		thread1.start();
		Thread.sleep(1000);
		thread2.start();
		
	}
}
