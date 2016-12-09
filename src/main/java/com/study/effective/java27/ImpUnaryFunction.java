package com.study.effective.java27;

public class ImpUnaryFunction {
	
	private static UnaryFunction<Object> IDENTITY_FUNCTION = new UnaryFunction<Object>() {
		
		@Override
		public Object apply(Object arg) {
			// TODO Auto-generated method stub
			return arg;
		}
	};
	
	@SuppressWarnings("unchecked")
	public static <T> UnaryFunction<T> identyFunction(){
		
		return (UnaryFunction<T>) IDENTITY_FUNCTION;
	}
	
	public static void main(String[] args) {
		
		String[] strings = {"jute","hemp","nylon"};
		UnaryFunction<String> sameString = identyFunction();
		for(String s:strings){
			System.out.println(sameString.apply(s));
		}
		
		Number[] numbers = {1,2.0,3L};
		UnaryFunction<Number> sameNumber = identyFunction();
		for(Number n:numbers){
			System.out.println(sameNumber.apply(n));
		}
	}
}
