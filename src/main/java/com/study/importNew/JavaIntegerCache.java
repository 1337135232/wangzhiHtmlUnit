package com.study.importNew;

public class JavaIntegerCache {
	public static void main(String[] args) {
		execute();
//		explain();
	}
	
	public static void execute(){
		Integer it1 = 3;
		Integer it2 = 3;
		if(it1==it2){
			System.out.println("it1 = it2");
		}else{
			System.out.println("it1 != it2");
		}
		
		Integer it3 = 300;
		Integer it4 = 300;
		if(it3==it4){
			System.out.println("it3 = it4");
		}else{
			System.out.println("it3 != it4");
		}
	}
	public static void explain(){
		Integer i = 3;
		//上述代码相当于
		Integer i1 = Integer.valueOf(300);
		System.out.println(i1);
		
		//解析
//		Integer执行自动装箱时，即Integer i = n，不是Integer i = new Integer(n)
//		第一次执行的时候会初始化Integer的内部类IntegerCache的cache数组，默认cache中存放-128到127共256个值，即256个Integer对象
//		当然通过设置java.lang.Integer.IntegerCache.high参数，比如为x，则可以设置存放-128到x的共x+128个值，即x+128个Integer对象
//		第一次以后的每次自动装箱的时候，都会先从cache数组中去取数据，取不到才开始new Integer对象
		
		
//		所以execute中，执行it1=3时，会在cache中初始化缓存从-128到127共256个Integer对象
//		执行it2=3时，则会从cache中取对象，所以it1=it2
//		执行it3=300时，cache中无300，则需要new一个新的Integer
//		执行it4=300时，cache中也无300，则也需要new一个新的Integer
//		所以对象it3!对象it4  it3!=it4
		
		
	}
}
