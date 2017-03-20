package com.study.designPattern.proxy;

public class ExcuteMain {

	public static void main(String[] args) {
		ProxyFactory pf = new ProxyFactory();
		StudentInterface obj = (StudentInterface) pf.createStudentProxy(new Student());
		obj.print();
		
		StudentInterface obj2 = (StudentInterface) pf.createStudentProxy(new Student("aaaa"));
		String result = obj2.print();
		System.out.println(result);
		
		CGlibProxyFactory cglbpf = new CGlibProxyFactory();
		Student stu = (Student)cglbpf.createStudent(new Student());
		stu.print();
		
		Student stu2 = (Student) cglbpf.createStudent(new Student("bbbb"));
		String result2 = stu2.print();
		System.out.println(result2);
		
		
	}
}
