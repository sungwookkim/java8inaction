package Part1.Chapter3.Chapter3_3_6.entity;

public class Fruit {
	protected String color;
	protected Integer weight;
	
	public Fruit() {}
	
	public Fruit(Integer weight) {
		this.weight = weight;
	}

	public Fruit(String color, Integer weight) {
		this.color = color;
		this.weight = weight;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public Integer getWeight() {
		return weight;
	}
	
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}
