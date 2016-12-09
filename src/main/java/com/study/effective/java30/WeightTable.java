package com.study.effective.java30;

public class WeightTable {

	
	public static void main(String[] args) {
		
		double earthWeight = Double.parseDouble("175");//地球上的重量
		double mass = earthWeight/Planet.EARTH.surfaceGravity();//地球上的质量 因为  mg = GMm/r*r 所以 m=mg/(GM/r*r)
		
		for(Planet p:Planet.values()){
			System.out.printf("Weight on %s is %f%n",p,p.surfaceWeight(mass));
		}
	}
}


