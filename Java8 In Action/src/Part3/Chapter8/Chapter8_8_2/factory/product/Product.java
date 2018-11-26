package Part3.Chapter8.Chapter8_8_2.factory.product;

public class Product {

	protected String name;
	
	public Product() { }
	
	public Product(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return "{" 
			+ "name : " + this.name
			+ "}";
	}
}
