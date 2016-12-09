package com.study.effective.java2;

public class NutritionFacts2 {
	
	private int servingSize = -1; //required
	private int servings = -1;    //required
	private int calories = -1;	   //optional
	private int fat = -1;	   //optional
	private int sodium = -1;	   //optional
	private int carbohydrate = -1;	   //optional
	
	
	public NutritionFacts2(){
	}


	public void setServingSize(int servingSize) {
		this.servingSize = servingSize;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}


	public void setCalories(int calories) {
		this.calories = calories;
	}

	public void setFat(int fat) {
		this.fat = fat;
	}

	public void setSodium(int sodium) {
		this.sodium = sodium;
	}

	public void setCarbohydrate(int carbohydrate) {
		this.carbohydrate = carbohydrate;
	}
	


	
}
