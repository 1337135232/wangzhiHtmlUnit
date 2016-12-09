package com.study.bat.thread;

import java.util.List;

public class W1116_A2_Synchronized {

	private Object lock = new Object();
	
	private List<String> someFields1;
	
	private List<String> someFields2;
	
	public void add(String someText){
		
		// some code
		
		synchronized (lock){
			someFields1.add(someText);
			someFields2.add(someText);
		}
		
		// some code
	}
	
	public Object[] getSomeFields(){
		
		// some code
		
		Object[] objects1 = null;
		
		Object[] objects2 = null;
		
		synchronized(lock){
			objects1 = someFields1.toArray();
			objects2 = someFields2.toArray();
		}
		
		Object[] objects = new Object[someFields1.size()+someFields2.size()];
		
		System.arraycopy(objects1, 0, objects, 0, objects1.length);
		System.arraycopy(objects2, 0, objects, objects1.length, objects2.length);
		
		return objects;
	}
}

/**
 * lock是一个专门用于监控的对象，它没有任何实际意义，只是为了与synchronized配合，完成对两个属性的统一锁定。
 * 当然，一般情况下，也可以使用this代替lock，这其实没有什么死的规定，完全可以按照实际情况而定。
 */
