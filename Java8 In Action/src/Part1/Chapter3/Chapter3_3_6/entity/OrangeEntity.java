package Part1.Chapter3.Chapter3_3_6.entity;

public class OrangeEntity extends Fruit {
	public OrangeEntity() {}
	
	public OrangeEntity(Integer weight) {
		this.weight = weight;
	}

	public OrangeEntity(String color, Integer weight) {
		this.color = color;
		this.weight = weight;
	}
}
