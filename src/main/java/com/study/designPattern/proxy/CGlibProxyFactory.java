package com.study.designPattern.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CGlibProxyFactory implements MethodInterceptor{

	private Object target;
	
	public Object createStudent(Object target){
		this.target = target;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(this);
		return enhancer.create();
	}
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		Student student = (Student) this.target;
		Object result = null;
		if(student.getName() != null)    
            result = method.invoke(student, args);    
        else    
            System.out.println("方法已经被拦截...");    
        return result;   
	}

}
