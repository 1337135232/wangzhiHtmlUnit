package com.study.restructure.demo1;

import java.util.Enumeration;
import java.util.Vector;

public class Customer {

	private String _name;
	private Vector<Rental> _rentals = new Vector<Rental>();
	
	public Customer(String _name) {
		this._name = _name;
	}

	
	public Vector<Rental> get_rentals() {
		return _rentals;
	}


	public void set_rentals(Rental rental) {
		this._rentals.add(rental);
	}


	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String statement(){
		int frequentRenterPoints = 0;
		Enumeration<Rental> rentals = _rentals.elements();
		String result = "Rental Record for "+ get_name()+"\n";
		while(rentals.hasMoreElements()){
			
			Rental each = rentals.nextElement();
			
			result+="\t"+each.get_movie().get_title()+"\t"+
					String.valueOf(each.getCharge())+"\n";
		}
		
		result += "Amount owed is "+String.valueOf(getTotalCharge())+"\n";
		result += "You earned "+String.valueOf(getTotalFrequentRenterPoints())+" frequent renter points";
		return result;
		
	}
	
	private double getTotalCharge(){
		double result = 0;
		Enumeration<Rental> rentals = _rentals.elements();
		while(rentals.hasMoreElements()){
			Rental each = rentals.nextElement();
			result += each.getCharge();
		}
		
		return result;
	}
	
	private int getTotalFrequentRenterPoints(){
		int result = 0;
		Enumeration<Rental> rentals = _rentals.elements();
		while(rentals.hasMoreElements()){
			Rental each = rentals.nextElement();
			result += each.getFrequentRenterPoints();
		}
		return result;
	}
	
}
