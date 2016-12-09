package com.study.restructure.demo1.state;

import com.study.restructure.demo1.Movie;

public class ChildrensPrice extends Price{

	@Override
	public int getPriceCode() {
		// TODO Auto-generated method stub
		return Movie.CHILDRENS;
	}

	@Override
	public double getCharge(int daysRented) {
		// TODO Auto-generated method stub
		double result = 1.5;
		if(daysRented>3){
			result+=(daysRented-3);
		}
		return result;
	}

}
