package Part3.Chapter8.Chapter8_8_2.templateMethod.abs;

import java.util.function.Consumer;
import java.util.function.Function;

import Part3.Chapter8.Chapter8_8_2.templateMethod.Database;
import Part3.Chapter8.Chapter8_8_2.templateMethod.customer.Customer;

public abstract class OnlineBanking {
	Function<Integer, Customer> getCustomerWithId = (id) -> Database.getCustomerWithId(id);  
	
	public void processCustomer(int id) {
		Customer c = this.getCustomerWithId.apply(id);
		
		this.makeCustomerHappy(c);
	}
	
	public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
		Customer c = this.getCustomerWithId.apply(id);
		
		makeCustomerHappy.accept(c);
	}
	
	public Function<Integer, Customer>  rtnCustomerWithId() {
		return this.getCustomerWithId;
	}
	
	public abstract void makeCustomerHappy(Customer c);
}
