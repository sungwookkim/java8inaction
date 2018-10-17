package Part1.Chapter2.Chapter2_2_1.entity;

/**
 * @author sinnakeWEB
 *
 */
public class AppleEntity {

	private String color;
	private int weight;

	public AppleEntity() {}
	
	public AppleEntity(String color, int weight) {
		this.color = color;
		this.weight = weight;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
}
