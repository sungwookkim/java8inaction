package Part3.Chapter11.Chapter11_11_3;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

import Part3.Chapter11.Chapter11_11_3.App.Shop;

/*
 * 11.3 비블록 코드 만들기
 */
public class Main_11_3 {

	/*
	 * 동기 API를 이용해서 최저가격 검색 애플리케이션을 개발해야 한다.
	 * 다음과 같이 상점 리스트가 있다고 가정하자.
	 */
	final static List<Shop> shops = Arrays.asList(
		new Shop("BestPrice")
		, new Shop("LetsSaveBig")
		, new Shop("MyFavoriteShop")
		, new Shop("BuyItAll")		
	);

	/*
	 * 상점 수보다 많은 스레드를 생성해봐야 사용할 가능성이 전혀 없으므로 상점 수보다 많은 스레드를 갖는건 낭비다.
	 * 한 상점에 하나의 스레드가 할당될 수 있도록 즉, 가격 정보를 검색하려는 상점 수만큼 스레드를 갖도록 Executor를
	 * 설정한다. 스레드 수가 너무 많으면 서버가 크래시될 수 있으므로 하나의 Executor에서 사용할 스레드의 최대 개수는
	 * 100 이하로 설정하는 것이 좋다.
	 */
	final static Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 2000), new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			/*
			 * 생성한 풀은 '데몬 스레드(daemon thread)'를 포함한다. 자바에서 일반 스레드가 실행 중이면
			 * 자바 프로그램은 종료되지 않는다. 따라서 어떤 이벤트를 한없이 기다리면서 종료되지 않는
			 * 일반 스레드가 있으면 문제가 될 수 있다.
			 * 반면 데몬 스레드는 자바 프로그램이 종료될 때 강제로 실행이 종료될 수 있다. 두 스레드 성능은 같다.
			 */
			t.setDaemon(true);
			
			return t;
		}
	});
	
	/*
	 * 제품명을 입력하면 상점 이름과 제품가격 문자열 정보를 포함하는 List를 반환하는 메서드.
	 */
	public static List<String> findPrices(String product) {
		return shops.stream()
			.map(shop -> String.format("%s price is %.2f", shop.getShopName(), shop.getPrice(product)) )
			.collect(Collectors.toList());
	}
	
	public static List<String> findParallelPrices(String product) {
		/*
		 * 11.3.1 병렬 스트림으로 요청 병렬화하기  
		 */
		return shops.parallelStream()
			.map(shop -> String.format("%s price is %.2f", shop.getShopName(), shop.getPrice(product)) )
			.collect(Collectors.toList());
	}
	
	public static List<String> findFuturePrices(String product) {
		/*
		 * 11.3.2 CompletableFuture로 비동기 호출 구현하기
		 */
		/*
		 * CompletableFuture를 포함하는 리스트 List<CompletableFuture<String>>를 얻을 수 있다.
		 * 리스트의 CompletableFuture는 각각 계산 결과가 끝난 상점의 이름 문자열을 포함한다.
		 */
		List<CompletableFuture<String>> priceFutures = shops.stream()
			.map(shop -> {
				return CompletableFuture.supplyAsync(() -> String.format("%s price is %.2f", shop.getShopName(), shop.getPrice(product)) );
			})
			.collect(Collectors.toList());
		
		/*
		 * 리스트의 모든 CompletableFuture에 join을 호출해서 모든 동작이 끝나기를 기다린다.
		 * join 메서드는 Future 인터페이스의 get 메서드와 같은 의미를 갖는다.
		 * 다만 join은 아무 예외도 발생시키지 않는다는 점이 다르다.
		 * 따라서 try ~ catch로 감쌀 필요가 없다.
		 */
		return priceFutures.stream()
			.map(CompletableFuture::join)
			.collect(Collectors.toList());
		
		/*
		 * 두 map 연산을 하나의 스트림 처리 파이프라인으로 처리하지 않고 두 개의 스트림 파이프라인으로 처리한 이유는
		 * 스트림 연산은 게으른 특성이 있으므로 하나의 파이프라인으로 연산을 처리 했다면 모든 가격 정보 요청이
		 * 동기적, 순차적으로 이루어지는 결과가 된다.
		 * CompletableFuture로 각 상점의 정보를 요청할 때 기존 요청 작업이 완료되어야 join이 결과를 반환하면서
		 * 다음 상점으로 정보를 요청할 수 있기 때문이다.
		 */		

		/*		
			shops.stream()
				.map(shop -> {
					return CompletableFuture.supplyAsync(() -> String.format("%s price is %.2f", shop.getShopName(), shop.getPrice(product)) );
				})
				.map(CompletableFuture::join)
				.collect(Collectors.toList());
		*/		
	}

	public static List<String> findFutureExecutorPrices(String product) {		
		List<CompletableFuture<String>> priceFutures = shops.stream()
			.map(shop -> {
				return CompletableFuture.supplyAsync(() -> {
					return String.format("%s price is %.2f", shop.getShopName(), shop.getPrice(product));	
				}, executor );
			})
			.collect(Collectors.toList());
		
		return priceFutures.stream()
				.map(CompletableFuture::join)
				.collect(Collectors.toList());
	}
	public static void main(String[] args) {
		long start = System.nanoTime();
		/*
		 * 네 개의 상점에서 가격을 검색하는 동안 각각 1초의 대기시간이 있으므로 전체 가격 검색 결과는 4초 정도 걸린다.
		 */
		System.out.println(findPrices("myPhone27S"));
		System.out.println("findPrices Done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");

		start = System.nanoTime();
		System.out.println(findParallelPrices("myPhone27S"));
		System.out.println("findParallelPrices Done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
		
		start = System.nanoTime();
		System.out.println(findFuturePrices("myPhone27S"));
		System.out.println("findFuturePrices Done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
				
		start = System.nanoTime();
		System.out.println(findFutureExecutorPrices("myPhone27S"));
		System.out.println("findFutureExecutorPrices Done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
		
		/*
		 * 스트림 병렬화와 CompletableFuture 병렬화
		 * 
		 * - I/O가 포함되지 않은 계산 중심의 동작을 실행할 때는 스트림 인터페이스가 구현하기 간단하며 효율적일 수 있다.
		 * (모든 스레드가 계산 작업을 수행하는 상황에서는 프로세서 코어 수 이상의 스레드를 가질 필요가 없다.)
		 * 
		 * - I/O를 기다리는 작업을 병렬로 실행할 때는 CompletableFuture가 더 많은 유연성을 제공하며 대기/계산(W/C)의
		 * 비율에 적합한 스레드 수를 설정할 수 있다. 특히 스트림의 게으른 특성 때문에 스트림에서 I/O를 실제로 언제 처리할지
		 * 예측하기 어려운 문제도 있다.
		 */
	}
}

