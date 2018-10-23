package Part1.Chapter3.Chapter3_3_5.entity;

public class AppleEntity {

	private String color;
	private Integer weight;
	
	public AppleEntity() {}
	
	public AppleEntity(String color, Integer weight) {
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