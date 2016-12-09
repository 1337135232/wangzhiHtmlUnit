package com.study.restructure.demo1;

import com.study.restructure.demo1.state.ChildrensPrice;
import com.study.restructure.demo1.state.NewReleasePrice;
import com.study.restructure.demo1.state.Price;
import com.study.restructure.demo1.state.RegularPrice;

public class Movie {
	
	public static final int CHILDRENS = 2;
	public static final int REGULAR = 0;
	public static final int NEW_RELEASE = -1;
	
	private String _title;
	private int _priceCode;
	private Price _price;
	
	public Movie(String _title,int _priceCode){
		this._title = _title;
		set_priceCode(_priceCode);
	}
	public String get_title() {
		return _title;
	}
	public void set_title(String _title) {
		this._title = _title;
	}
	public int get_priceCode() {
		return _priceCode;
	}
	public void set_priceCode(int _priceCode) {
		switch (_priceCode) {
		case REGULAR:
			_price = new RegularPrice();
			break;
		case CHILDRENS:
			_price = new ChildrensPrice();
			break;
		case NEW_RELEASE:
			_price = new NewReleasePrice();
			break;
		default:
			break;
		}
	}
	
	//金额计算
	public double getCharge(int daysRented){
		return _price.getCharge(daysRented);
	}
	
	public int getFrequentRenterPoints(int daysRented){
		
		return _price.getFrequentRenterPoints(daysRented);
	}
	
}
