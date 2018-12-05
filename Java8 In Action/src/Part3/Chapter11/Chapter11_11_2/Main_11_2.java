package Part3.Chapter11.Chapter11_11_2;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import Part3.Chapter11.Chapter11_11_2.App.Shop;

/*
 * 11.2 비동기 API 구현
 */
public class Main_11_2 {

	public static void doSomeThingElse() {
		System.out.println("doSomeThingElse method execute!!"); 
	}
	
	public static void main(String[] args) {
		Shop shop = new Shop("BestShop");
		
		long start = System.nanoTime();
		// 상점에 제품가격 정보 요청
		Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
		Future<Double> futureSupplyPrice = shop.getPriceSupplyAsync("my favorite product");
		System.out.println("Invocation returned after " 
			+ ((System.nanoTime() - start) / 1_000_000) + " msecs");	
		
		doSomeThingElse();
		
		try {
			/*
			 * 가격 정보가 있으면 Future에서 가격정보를 읽고, 가격 정보가 없으면
			 * 가격 정보를 받을 때까지 블록한다.
			 */
			System.out.printf("Price is %.2f%n", futurePrice.get(1, TimeUnit.SECONDS));			
			System.out.printf("Supply Price is %.2f%n", futureSupplyPrice.get(1, TimeUnit.SECONDS));
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("Price returned after " 
			+ ((System.nanoTime() - start) / 1_000_000) + " msecs");
	}

}
