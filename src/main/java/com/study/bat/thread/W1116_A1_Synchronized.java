package com.study.bat.thread;

import java.util.List;

/**
 * synchronized是JVM提供的同步机制，它可以修饰方法或者代码块。
 * 此外，在修饰代码块的时候，synchronized可以指定锁定的对象，比如常用的有this，类字面常量等。
 * 在使用synchronized的时候，通常情况下，我们会针对特定的属性进行锁定，有时也会专门建立一个加锁对象。
 * @author wangzhi
 *
 */
public class W1116_A1_Synchronized {

	private List<String> someFields;
	
	public void add(String someText){
		
		// some code
		
		synchronized (someFields) {
			someFields.add(someText);
		}
		
		// some code
	}
	
	public Object[] getSomeFields(){
		
		// some code
		
		synchronized (someFields){
			return someFields.toArray();
		}
	}
}

/**
 * 这种方式一般要优于使用this或者类字面常量进行锁定的方式，
 * 因为synchronized修饰的非静态成员方法默认是使用的this进行锁定，
 * 而synchronized修饰的静态成员方法默认是使用的类字面常量进行的锁定，
 * 因此如果直接在synchronized代码块中使用this或者类字面常量，
 * 可能会不经意的与synchronized方法产生互斥。
 * 通常情况下，使用属性进行加锁，能够更加有效的提高并发度，从而在保证程序正确的前提下尽可能的提高性能。
**/