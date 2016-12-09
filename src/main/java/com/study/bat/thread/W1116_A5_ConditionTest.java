package com.study.bat.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 只需要获取lock一次就可以了，在内部咱们可以使用两个或多个条件而不再需要多次获得锁。这种方式会更加直观，大大增加程序的可读性。
 * 
 * 与W1116_A5_ObjectWait比较着看
 * @author wangzhi
 *
 */
public class W1116_A5_ConditionTest {

	private static ReentrantLock lock = new ReentrantLock();
	
	public static void main(String[] args) throws InterruptedException {
		
		final Condition condition1 = lock.newCondition();
		final Condition condition2 = lock.newCondition();
		
		Thread thread1 = new Thread(new Runnable(){

			@Override
			public void run() {
				lock.lock();
				
				try {
					System.out.println("等待condition1被通知");
					condition1.await();
					
					System.out.println("condition1已被通知，马上开始通知condition2");
					condition2.signal();
					
					System.out.println("通知condition2完毕");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					
					lock.unlock();
				}
			}
			
		});
		
		
		Thread thread2 = new Thread(new Runnable(){

			@Override
			public void run() {
				
				lock.lock();
				
				try {
					System.out.println("马上开始通知condition1！");
					condition1.signal();
					
					System.out.println("通知condition1完毕，等待condition2被通知！");
					condition2.await();
					
					 System.out.println("condition2已被通知！");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					
					lock.unlock();
				}
			}
			
		});
		
		thread1.start();
        Thread.sleep(1000);
        thread2.start();
		
	}
}
