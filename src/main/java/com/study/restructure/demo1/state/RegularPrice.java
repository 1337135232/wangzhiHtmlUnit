package com.study.restructure.demo1.state;

import com.study.restructure.demo1.Movie;

public class RegularPrice extends Price{

	@Override
	public int getPriceCode() {
		// TODO Auto-generated method stub
		return Movie.REGULAR;
	}

	@Override
	public double getCharge(int daysRented) {
		// TODO Auto-generated method stub
		double result = 2;
		if(daysRented>2){
			result+=(daysRented-2);
		}
		return result;
	}

}
