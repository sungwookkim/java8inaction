package Part3.Chapter11.Chapter11_11_4.App;

import java.util.Random;

public class ExchangeService {
	public enum Money {
		USD(100), EUR(200);
		
		private final int exchange;
		
		Money(int exchange) {
			this.exchange = exchange;
		}
	}
	
	public static double getRate(Money exchange) {
		delay();
		return exchange.exchange;
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
