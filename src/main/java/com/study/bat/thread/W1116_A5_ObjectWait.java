package com.study.bat.thread;

/**
 * 咱们就来谈谈wait到底有哪些不足。
 * 　　1）wait方法当使用带参数的方法wait(timeout)或者wait(timeout,nanos)时，无法反馈究竟是被唤醒还是到达了等待时间
 * ，大部分时候，我们会使用循环（就像上面的例子一样）来检测是否达到了条件。 　　解决方式：Condition可以使用返回值标识是否达到了超时时间。
 * 　　2）由于wait
 * ,notify,notifyAll方法都需要获得当前对象的锁，因此当出现多个条件等待时，则需要依次获得多个对象的锁，这是非常恶心麻烦且繁琐的事情。
 * 　　解决方式：Condition只需要获得Lock的锁即可，一个Lock可以拥有多个条件。
 * 
 * 与W1116_A5_ConditionTest比较着看
 * @author wangzhi
 * 
 */
public class W1116_A5_ObjectWait {

	public static void main(String[] args) throws InterruptedException {

		final Object object1 = new Object();
		final Object object2 = new Object();

		Thread thread1 = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					System.out.println("等待object1被通知！");

					synchronized (object1) {

						object1.wait();
					}

					System.out.println("object1已被通知，马上开始通知object2！");

					synchronized (object2) {
						object2.notify();
					}
					System.out.println("通知object2完毕！");

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("马上开始通知object1！");
					synchronized (object1) {
						object1.notify();
					}
					System.out.println("通知object1完毕，等待object2被通知！");
					synchronized (object2) {
						object2.wait();
					}
					System.out.println("object2已被通知！");
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});

		thread1.start();
        Thread.sleep(10000);
        thread2.start();
	}
}

/**
 * 这是一个多条件的示例。基本逻辑是，线程1先等待线程2通知，然后线程2再等待线程1通知。请记住，这是两个不同的条件。可以看到，如果使用wait的话，必须两次获得两个锁，一不小心可能还会出现死锁
*/