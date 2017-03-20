package com.study.designPattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*解释：

首先调用代理工厂的createStudentProxy(Object stu)创建StudentBean类的代理类.
在该方法内，调用Proxy.newProxyInstance()方法创建代理对象。第一个参数是目标对象的类加载器，第二个参数是目标对象实现的接口，第三个参数传入一个InvocationHandler实例，该参数和回调有关系。
每当调用目标对象的方法的时候，就会回调该InvocationHandler实例的方法，也就是public Object invoke()方法，我们就可以把限制的条件放在这里，条件符合的时候，就可以调用method.invoke()方法真正的调用目标对象的方法，否则，则可以在这里过滤掉不符合条件的调用。
Proxy 实现 AOP 功能总结：

目标对象必须实现接口。
调用Proxy.newProxyInstance()方法，返回创建的代理对象。
由于该方法需要一个实现了InvocationHandler接口的对象，所以我们还要重写该接口的invoke()方法。
我们的限制条件就可以放在这个invoke()方法中，当满足条件，就调用method.invoke()真正的调用目标对象的方法，否则，不做任何事情，直接过滤。*/
public class ProxyFactory implements InvocationHandler{

	private Object target;//被代理的目标对象
	
	public Object createStudentProxy(Object target){
		this.target = target;
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), this);
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		Student student = (Student) this.target;
		Object object = null;//方法返回结果
		if(student.getName()!=null){
			object = method.invoke(student,args);
		}else{
            System.out.println("名字为空，代理类已经拦截！");    
		}
		return object;
	}

}
