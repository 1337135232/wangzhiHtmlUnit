package com.study.restructure.demo1;

//租赁
public class Rental {
	
	private Movie _movie;
	private int _daysRentened;
	
	public Rental(Movie _movie, int _daysRentened) {
		this._movie = _movie;
		this._daysRentened = _daysRentened;
	}

	public Movie get_movie() {
		return _movie;
	}

	public void set_movie(Movie _movie) {
		this._movie = _movie;
	}

	public int get_daysRentened() {
		return _daysRentened;
	}

	public void set_daysRentened(int _daysRentened) {
		this._daysRentened = _daysRentened;
	}

	//金额计算
	public double getCharge(){
		return _movie.getCharge(_daysRentened);
	}
	
	public int getFrequentRenterPoints(){
		return _movie.getFrequentRenterPoints(_daysRentened);
	}
	
}
