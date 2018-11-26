package Part3.Chapter8.Chapter8_8_2.templateMethod.customer;

public class Customer {
	private final String name;
	private final int id;
	
	public Customer(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return "{"
			+ "name : " + this.name
			+ ", id : " + this.id
			+ "}";
	}	 
}
