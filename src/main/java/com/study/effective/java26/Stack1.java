package com.study.effective.java26;

import java.util.Arrays;
import java.util.EmptyStackException;
/**
 * 使用泛型
 * @author wangzhi
 *
 * @param <E>
 */
public class Stack1<E> {

	private E[] elements;
	private int size = 0;
	private static final int DEFAULT_INITIAL = 16;
	
	@SuppressWarnings("unchecked")
	public Stack1(){
		elements = (E[]) new Object[DEFAULT_INITIAL];
	}
	
	public void push(E e){
		ensureCapacity();
		elements[size++] = e;
	}
	
	public E pop(){
		if(size == 0)
			throw new EmptyStackException();
		
		E result = elements[--size];
		elements[size] = null;
		return result;
	}
	
	public boolean isEmpty(){
		return size == 0;
	}
	
	public void ensureCapacity(){
		if(elements.length == size){
			elements = Arrays.copyOf(elements, size*2+1);
		}
	}
	
	public static void main(String[] args) {
		String[] args1 = {"a","b","c"};
		Stack1<String> stack = new Stack1<String>();
		for(String arg:args1){
			stack.push(arg);
		}
		while(!stack.isEmpty()){
			System.out.println(stack.pop().toUpperCase());
		}
		
	}
	
}
