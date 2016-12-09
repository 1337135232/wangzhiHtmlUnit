package com.study.bat.thread;

import java.util.concurrent.Exchanger;

/**
 * Exchanger 这个类是为了帮助猿友们方便的实现两个线程交换数据的场景，使用起来非常简单，看看下面这段代码。
 * @author wangzhi
 *
 */
public class W1116_A9_Exchanger {

	public static void main(String[] args) {
		
		final Exchanger<String> exchanger = new Exchanger<String>();
		
		Thread thread1 = new Thread(new Runnable(){

			@Override
			public void run() {
				
				try {
					System.out.println("线程1等待接收");
					String content = exchanger.exchange("aaaaaaaaaaaa");
					System.out.println("线程1收到为："+content);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		Thread thread2 = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					System.out.println("线程2等待接收并沉睡3秒");
					Thread.sleep(3000);
					String content = exchanger.exchange("bbbbbbbbbbb");
					System.out.println("线程2收到的为：" + content);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		thread1.start();
        thread2.start();
	}
}


//两个线程在只有一个线程调用exchange方法的时候调用方会被挂起，
//当都调用完毕时，双方会交换数据。在任何一方没调用exchange之前，线程都会处于挂起状态。