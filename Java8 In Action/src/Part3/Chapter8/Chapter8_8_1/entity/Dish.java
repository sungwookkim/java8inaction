package Part3.Chapter8.Chapter8_8_1.entity;

public class Dish {
	private final String name;
	private final boolean vegetarian;
	private final int calories;
	private Type type;
	
	public Dish(String name, boolean vegetarian, int calories, Type type) {	
		this.name = name;
		this.vegetarian = vegetarian;
		this.calories = calories;
		this.type = type;
	}
	
	public String getName() {
		return this.name;
	}

	public int getCalories() {
		return this.calories;
	}
	
	public Type getType() {
		return this.type;
	}

	public boolean isVegetarian() {
		return vegetarian;
	}
	
	public CaloricLevel getCaloricLevel() {
		if(this.getCalories() <= 400) {
			return Dish.CaloricLevel.DIET;
		} else if(this.getCalories() <= 700) {
			return Dish.CaloricLevel.NORMAL; 
		} else {
			return Dish.CaloricLevel.FAT;
		}
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	public enum Type {
		MEAT, FISH, OTHER
	}
	
	public enum CaloricLevel {
		DIET, NORMAL, FAT
	}
}
