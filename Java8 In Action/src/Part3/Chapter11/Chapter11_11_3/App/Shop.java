package Part3.Chapter11.Chapter11_11_3.App;

import java.util.Random;

public class Shop {
	private final String shopName;
	
	public Shop() { 
		this.shopName = "";
	}
	
	public Shop(String shopName) {
		this.shopName = shopName;
	}
	
	public double getPrice(String product) {
		return this.calculatePrice(product);
	}
	
	private double calculatePrice(String product) {
		/*
		 * 상점의 데이터베이스를 이용해서 가격 정보를 얻는 동시에 다른 외부 서비스에 접근해야 하지만
		 * 예제에서는 실제 호출할 서비스까지 구현할 수 없으므로 임으로 1초를 지연 시킨다.
		 */
		delay();
		
		return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
	}

	public static void delay() {
		try {
			Thread.sleep(1000L);
		} catch(InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getShopName() {
		return this.shopName;
	}
	
	@Override
	public String toString() {
		return "{"
			+ "shopName : " + this.shopName
		+ "}";
	}
	
	
}
