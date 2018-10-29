package Part1.Chapter3.Chapter3_3_6.entity;

public class AppleEntity extends Fruit {
	
	public AppleEntity() {}
	
	public AppleEntity(Integer weight) {
		this.weight = weight;
	}

	public AppleEntity(String color, Integer weight) {
		this.color = color;
		this.weight = weight;
	}
}