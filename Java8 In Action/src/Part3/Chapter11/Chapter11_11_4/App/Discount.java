package Part3.Chapter11.Chapter11_11_4.App;

import java.util.Random;

public class Discount {
	public enum Code {
		NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
		
		private final int percentage;
		
		Code(int percentage) {
			this.percentage = percentage;
		}
	}
	
	public static String applyDiscount(Quote quote) {
		// 기존 가격에 할인 코드를 적용한다.
		return quote.getShopName() + " price is " + Discount.apply(quote.getPrice(), quote.getDiscountCode());
	}
	
	private static double apply(double price, Code code) {
		// Discount 서비스의 응답 지연을 흉내낸다. 
		delay();
		
		return price * (100 - code.percentage) / 100;
	}
	
	private static void delay() {
		int randomDelay = 500 + new Random().nextInt(2000);
		
		try {
			Thread.sleep(randomDelay);
		} catch(InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
