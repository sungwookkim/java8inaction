package Part3.Chapter8.Chapter8_8_2.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import Part3.Chapter8.Chapter8_8_2.factory.product.Bond;
import Part3.Chapter8.Chapter8_8_2.factory.product.Loan;
import Part3.Chapter8.Chapter8_8_2.factory.product.Product;
import Part3.Chapter8.Chapter8_8_2.factory.product.Stock;

public class LambdaProductFactory {
	final static Map<String, Supplier<Product>> map = new HashMap<>();

	static {
		map.put("loan", Loan::new);
		map.put("stock", Stock::new);
		map.put("bond", Bond::new);
	}
	
	public static Product createProduct(String name) {
		Supplier<Product> p = map.get(name);
		
		if(p != null) {
			return p.get();
		}
		
		throw new IllegalArgumentException("no such product " + name);
	}
}
