package com.study.bat.thread;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 　ReentrantLock（可重入的锁）
　　ReentrantLock是JDK并发包中locks当中的一个类，专门用于弥补synchronized关键字的一些不足。
       接下来咱们就看一下synchronized关键字都有哪些不足，接着咱们再尝试使用ReentrantLock去解决这些问题。
　　1）synchronized关键字同步的时候，等待的线程将无法控制，只能死等。
　　解决方式：ReentrantLock可以使用tryLock(timeout, unit)方法去控制等待获得锁的时间，
       也可以使用无参数的tryLock方法立即返回，这就避免了死锁出现的可能性。
　　2）synchronized关键字同步的时候，不保证公平性，因此会有线程插队的现象。
　　解决方式：ReentrantLock可以使用构造方法ReentrantLock(fair)来强制使用公平模式，
       这样就可以保证线程获得锁的顺序是按照等待的顺序进行的，
       而synchronized进行同步的时候，是默认非公平模式的，但JVM可以很好的保证线程不被饿死。
　　
	ReentrantLock有这样一些优点，当然也有不足的地方。
	最主要不足的一点，就是ReentrantLock需要开发人员手动释放锁，并且必须在finally块中释放。
	
	与W1116_A3_ReentrantLockTest比较着看
 * @author wangzhi
 *
 */
public class W1116_A3_Lock {

	private ReentrantLock nonfairLock = new ReentrantLock();
	
	private ReentrantLock fairLock = new ReentrantLock(true);
	
	private List<String> someFields;
	
	public void add(String someText){
		
		// 等待获得锁，与synchronized类似
		
		nonfairLock.lock();
		
		try{
			someFields.add(someText);
		}finally{
			
			// finally中释放锁是无论如何都不能忘的
			
			nonfairLock.unlock();
		}
	}
	
	public void addTimeout(String someText){
		
		// 尝试获取，如果10秒没有获取到则立即返回
		
		try {
			if(!fairLock.tryLock(10, TimeUnit.SECONDS)){
				return;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return;
		}
		
		try{
			someFields.add(someText);
		}finally{
			
			// finally中释放锁是无论如何都不能忘的
			
			fairLock.unlock();
		}
	}
}

/**
 * 以上主要展示了ReentrantLock的基本用法和限时的等待
 */
