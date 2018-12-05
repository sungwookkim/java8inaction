package Part3.Chapter11.Chapter11_11_2.App;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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

	public Future<Double> getPriceSupplyAsync(String product) {
		/*
		 * supplyAsync 메서드는 Supplier를 인수로 받아서 CompletableFuture를 반환한다.
		 * CompletableFutre는 Supplier를 실행해서 비동기적으로 결과를 생성한다.
		 * ForkJoinPool의 Executor 중 하나가 Supplier를 실행할 것이다.
		 * 
		 * 하지만 두 번째 인수로 Executor를 받는 다른 오버로드 버전을 통해서 Executor를 지정할 수 있다.
		 * 결국 모든 다른 CompletableFuture의 팩토리 메서드에 Executor를 선택적으로 전달할 수 있다.
		 * 
		 * 에러 및 예외 관리는 getPriceAsync 메서드의 try ~ catch와 같은 방법으로 관리 한다.
		 */
		return CompletableFuture.supplyAsync(() -> calculatePrice(product));
	}
	
	public Future<Double> getPriceAsync(String product) {
		// 계산 결과를 포함할 CompletableFuture를 생성한다.
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();
		
		new Thread(() ->  {
			/*
			 * 예외가 발생하면 해당 스레드에만 영향을 미친다.
			 * 즉, 에러가 발생해도 가격 계산은 계속 진행되며 일의 순서가 꼬인다.
			 * 결과적으로 클라이언트는 get 메서드가 반환될 때까지 영원히 기다리게 될 수도 있다.
			 * 
			 * 영구 대기 문제는 get 메서드의 타임아웃값을 받는 get 메서드를 사용해서 해결할 수 있다.
			 * 그래야 문제가 발생했을 때 클라이언트가 영원히 블록되지 않고 TimeoutExcetion을 받을 수 있다.
			 */
			try {
				// 다른 스레드에서 비동기적으로 계산을 수행한다.
				double price = calculatePrice(product);
				// 오랜 시간이 걸리는 계산이 완료되면 Future에 값을 설정한다.
				futurePrice.complete(price);				
			} catch(Exception e) {
				/*
				 * 에러가 발생 되면 왜 났는지 알 수가 없기 때문에 completeExceptionally 메서드를 이용해서
				 * CompletableFuture 내부에서 발생한 예외를 클라이언트로 전달해줘야 한다.
				 */
				futurePrice.completeExceptionally(e);				
			}
		}).start();
		
		// 계산 결과가 완료되길 기다리지 않고 Future를 반환한다.
		return futurePrice;
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
