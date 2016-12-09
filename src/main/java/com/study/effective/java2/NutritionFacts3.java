package com.study.effective.java2;

public class NutritionFacts3 {
	
	private final int servingSize; //required
	private final int servings;    //required
	private final int calories;	   //optional
	private final int fat;	   //optional
	private final int sodium;	   //optional
	private final int carbohydrate;	   //optional
	
	public static class Builder{
		private final int servingSize; //required
		private final int servings ;    //required
		
		
		private int calories = -1;	   //optional
		private int fat = -1;	   //optional
		private int sodium = -1;	   //optional
		private int carbohydrate = -1;	   //optional
		
		public Builder(int servingSize,int servings){
			this.servingSize = servingSize;
			this.servings = servings;
		}
		
		public Builder calories(int val){
			this.calories = val;
			return this;
		}
		
		public Builder fat(int val){
			this.fat = val;
			return this;
		}
		
		public Builder sodium(int val){
			this.sodium = val;
			return this;
		}
		
		public Builder carbohydrate(int val){
			this.carbohydrate = val;
			return this;
		}
		
		public NutritionFacts3 build(){
			return new NutritionFacts3(this);
		}
	}
	public NutritionFacts3(Builder builder){
		this.servingSize = builder.servingSize;
		this.servings = builder.servings;
		this.calories = builder.calories;
		this.fat = builder.fat;
		this.sodium = builder.sodium;
		this.carbohydrate = builder.carbohydrate;
	}
	
	
	public static void main(String[] args) {
		NutritionFacts3 t = new NutritionFacts3.Builder(0, 0).calories(0).fat(0).sodium(0).carbohydrate(0).build();
	}
}
