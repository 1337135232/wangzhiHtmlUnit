package com.study.restructure.demo1.state;

import com.study.restructure.demo1.Movie;

public class NewReleasePrice extends Price{

	@Override
	public int getPriceCode() {
		// TODO Auto-generated method stub
		return Movie.NEW_RELEASE;
	}

	@Override
	public double getCharge(int daysRented) {
		// TODO Auto-generated method stub
		return daysRented*3;
	}

	@Override
	public int getFrequentRenterPoints(int daysRented) {
		// TODO Auto-generated method stub
		return daysRented>1? 2:1;
	}

}
