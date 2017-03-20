package com.study.designPattern.Iterator;

import java.util.Iterator;

public interface Menu {
	public Iterator<MenuItem> createIterator();
}
