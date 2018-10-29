package Part1.Chapter2.Chapter2_2_4.entity;

/**
 * @author sinnakeWEB
 *
 */
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
	public void setWeight(int weight) {
		this.weight = weight;
	}	
	
}
