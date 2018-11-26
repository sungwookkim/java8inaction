package Part3.Chapter8.Chapter8_8_2.templateMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Part3.Chapter8.Chapter8_8_2.templateMethod.customer.Customer;

public class Database {
	final static List<Customer> customers = Arrays.asList(
		new Customer("kim", 1)
		, new Customer("sung", 2)
		, new Customer("wook", 3) ); 
			
	public static Customer getCustomerWithId(int id) {		
		return Database.customers.stream()
			.filter((customer) -> customer.getId() == id)
			.limit(1)
			.collect(Collectors.toList())
			.get(0);
	}
}
