package com.study.restructure.demo1.state;


public abstract class Price {
	
	abstract int getPriceCode();
	public abstract double getCharge(int daysRented);
    public int getFrequentRenterPoints(int daysRented){
    	return 1;
    }
}
