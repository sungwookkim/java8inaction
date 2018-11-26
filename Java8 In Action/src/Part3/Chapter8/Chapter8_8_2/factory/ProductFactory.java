package Part3.Chapter8.Chapter8_8_2.factory;

import Part3.Chapter8.Chapter8_8_2.factory.product.Bond;
import Part3.Chapter8.Chapter8_8_2.factory.product.Loan;
import Part3.Chapter8.Chapter8_8_2.factory.product.Product;
import Part3.Chapter8.Chapter8_8_2.factory.product.Stock;

public class ProductFactory {
	public static Product createProduct(String name) {
		switch (name) {
			case "loan" : return new Loan();
			case "stock" : return new Stock();
			case "bond" : return new Bond();
			default : throw new RuntimeException("No such product " + name);
		}
	}

}
