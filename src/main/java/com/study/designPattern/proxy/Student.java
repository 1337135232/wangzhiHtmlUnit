package com.study.designPattern.proxy;

public class Student implements StudentInterface{
	
	private String name;
	
	public Student(){}
	public Student(String name){
		this.name = name;
	}
	@Override
	public String print() {
		// TODO Auto-generated method stub
        System.out.println("Hello World!");   
        return "success";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
