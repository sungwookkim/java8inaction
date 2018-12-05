package Part3.Chapter11.Chapter11_11_4;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Part3.Chapter11.Chapter11_11_4.App.Discount;
import Part3.Chapter11.Chapter11_11_4.App.ExchangeService;
import Part3.Chapter11.Chapter11_11_4.App.ExchangeService.Money;
import Part3.Chapter11.Chapter11_11_4.App.Quote;
import Part3.Chapter11.Chapter11_11_4.App.Shop;

/*
 * 11.4 비동기 작업 파이프라인 만들기
 */
public class Main_11_4 {
	final static List<Shop> shops = Arrays.asList(
		new Shop("BestPrice")
		, new Shop("LetsSaveBig")
		, new Shop("MyFavoriteShop")
		, new Shop("BuyItAll")		
	);

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
	 * 11.4.2. 할인 서비스 사용
	 * 
	 * Discount는 원격 서비스이므로 1초 지연이 되어있다.
	 */
	public static List<String> findPrices(String product) {
		/*
		 * 세 개의 map 연산을 상점 스트림에 파이프라인으로 연결해서 결과를 얻었다.
		 * 
		 * 1. 각 상점을 요청한 제품의 가격과 할인 코드로 변환한다.
		 * 2. 이들 문자열을 파싱해서 Quote 객체를 생성한다.
		 * 3. 원격 Discount 서비스에 접근해서 최종 할인가격을 계산하고 가격에 대응하는 상점 이름을 포함하는 문자열을 반환한다.
		 */
		return shops.stream()
			// 각 상점에서 할인전 가격 얻기.
			.map(shop -> shop.getPrice(product))
			// 상점에서 반환한 문자열을 Quote 객체에 변환.
			.map(Quote::parse)
			// Discount 서비스를 이용해서 각 Quote에 할인을 적용.		
			.map(Discount::applyDiscount)
			.collect(Collectors.toList());
	}
	
	public static List<String> findFutureExecutorPrices(String product) {
		List<CompletableFuture<String>> priceFuture = shops.stream()
			.map(shop ->CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
			/*
			 * 상점에서 반환한 문자열을 Quote 객체로 변환.
			 * 
			 * 파싱 동작에서는 원격 서비스나 I/O가 없으므로 원하는 즉시 지연 없이 동작을 수행한다.
			 * thenApply 메서드는 CompletableFuture가 끝날 떄까지 블록하지 않는다.
			 * 즉, CompletableFuture가 동작을 완전히 완료한 다음에 thenApply 메서드로 전달된 람다 표현식을
			 * 적용할 수 있다.
			 * 따라서 CompletableFuture<String>을 CompletableFuture<Quote>로 변환된다.
			 */
			.map(prevFuture -> prevFuture.thenApply(Quote::parse))
			/*
			 * 결과 Future를 다른 비동기 작업과 조합해서 할인 코드를 적용.
			 * 
			 * 상점에서 받은 할인전 가격에 원격 Discount 서비스에서 제공하는 할인율을 적용한다.
			 * 이번에는 원격 실행이 포함되므로 이전 두 변환과 다르며 동기적으로 작업을 수행해야 한다.
			 * 
			 * 자바 8의 CompletableFuture API는 두 비동기 연산을 파이프라인으로 만들 수 있도록
			 * thenCompose 메서드를 제공한다. 
			 * 이전 CompletableFuture에 thenCompose 메서드를 호출하고 Function에 넘겨주는 식으로
			 * CompletableFutur를 조합할 수 있다.
			 * Function은 이전 CompletableFuture 반환 결과를 인수로 받고 다음 CompletableFuture를 반환하는데
			 * 다음 CompletableFuture는 이전 CompletableFuture의 결과를 계산의 입력으로 사용한다. 
			 */
			.map(nextFuture -> nextFuture.thenCompose(quote -> {
				return CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor);
			}))
			.collect(Collectors.toList());
				
		return priceFuture.stream()
			.map(CompletableFuture::join)
			.collect(Collectors.toList());
		
		/*
		 * thenCompose 메서드로 Async로 끝나는 버전이 존재한다.
		 * thenCompose 메서드는 이전 작업을 수행한 스레드와 같은 스레드에서 작업을 실행함을 의미하고
		 * thenComposeAsync 메서드는 다른 스레드에서 실행되도록 스레드 풀로 작업을 제출한다.
		 * 위 코드에서 두 번째 CompletableFuture의 결고는 첫 번째 CompletableFuture에 의존하므로
		 * 두 CompletableFuture를 하나로 조합하든 thenComposeAsync 메서드를 사용하든 최종결과나
		 * 개괄적인 실행시간에는 영향을 미치지 않는다.
		 * 따라서 스레드 오버헤드가 적게 발생하면서 효율성이 좀 더 좋은 thenCompose 메서드를 사용했다.
		 */
	}
	
	public static Double findFutureExecutorExchange(Shop shop, String product) {
		/*
		 * 11.4.4. 독립 CompletableFuture와 비독립 CompletableFuture 합치기
		 * 
		 * 독립적으로 실행된 두 개의 CompletableFuture 결과를 합쳐야 하는 상황 있을수 있고
		 * 첫 번째 CompletableFuture의 동작 완료와 관계없이 두 번째 CompletableFuture를 실행할 수 있어야 한다.
		 * 
		 * 이런 상황에서는 thenCombine 메서드를 사용한다. thenCompose와 마찬가지로 Async 버전이 존재한다.
		 */
		CompletableFuture<Double> futurePriceInUSD = CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor)
			.thenApply(Quote::parse)
			.thenCombine(CompletableFuture.supplyAsync(() -> ExchangeService.getRate(Money.EUR))
				, (price, rate) -> price.getPrice() * rate);

		return Stream.of(futurePriceInUSD)
			.map(CompletableFuture::join)
			.collect(Collectors.toList())
			.get(0);
	}

	public static Stream<CompletableFuture<String>> findPriceStream(String product) {
		return shops.stream()
			.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
			.map(prevFuture -> prevFuture.thenApply(Quote::parse))
			.map(nextFuture -> nextFuture.thenCompose(quote -> 
				CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)) );
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		long start = System.nanoTime();
		
		System.out.println(findPrices("myPhone27S"));
		System.out.println("findPrices Done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
		
		start = System.nanoTime();
		System.out.println(findFutureExecutorPrices("myPhone27S"));
		System.out.println("findFutureExecutorPrices Done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
		
		start = System.nanoTime();
		System.out.println(shops.get(0).getShopName() + " exchange : " +findFutureExecutorExchange(shops.get(0), "myPhone27S"));
		System.out.println("findFutureExecutorExchange Done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
		
		System.out.println("thenAccept ::::: ");
		long streamStart = System.nanoTime();

		/*
		 * 11.5.1 최저가격 검색 애플리케이션 리팩토링
		 * 
		 * findPriceStream 메서드 내부에서 세 가지 map 연산을 적용하고 반환하는 스트림에 네 번째 map 연산을 적용하자.
		 * 새로 추가한 연산은 단순하게 각 CompletableFuture에 동작을 등록한다.
		 * CompletableFuture에 등록된 동작은 CompletableFuture의 계산이 끝나면 값을 소비한다. 
		 */
		CompletableFuture[] futures = findPriceStream("myPhone")
			/*
			 * 자바 8의 CompletableFuture API는 thenAccept라는 메서드를 제공한다.
			 * thenAccept 메서드는 연산 결과를 소비하는 Consumer를 인수로 받는다.
			 * 우리 예제에서는 할인 서비스에서 반환하는 문자열이 값이다. 이 문자열은 상점 이름과 할인율을 적용한 제품의 가격을 포함한다.
			 * 
			 * thenAccept도 thenCompose, thenCombine 메서드와 마찬가지로 thenAcceptAsync라는 Async 버전이 존재한다.
			 * thenAcceptAsync 메서드는 CompletableFuture가 완료된 스레드가 아니라 새로운 스레드를 이용해서 Consumer를 실행한다.
			 * 불필요한 콘텍스트 변경은 피하는 동시에 CompletableFuture가 완료되는 즉시 응답하는 것이 좋으므로 thenAcceptAsync를 사용하지 않는다.
			 * (오히려 thenAcceptAsync를 사용하면 새로운 스레드를 이용할 수 있을 때까지 기다려야 하는 상황이 일어날 수 있다.)
			 * 
			 * thenAccept 메서드는 CompletableFuture가 생성한 결과를 어떻게 소비할지 미리 지정했기 때문에 CompletableFuture<Void>를 반환한다.
			 * 이제 CompletableFuture<Void>가 동작을 끝낼 때까지 딱히 할 수 있는 일이 없다.
			 */
			.map(f -> f.thenAccept(s -> 
				System.out.println(s + " (done in " + ((System.nanoTime() - streamStart) / 1_000_000) + " msecs)")) )
			.toArray(size -> new CompletableFuture[size]);

		/*
		 * 팩토리 메서드 allOf는 CompletableFuture 배열을 입력으로 받아 CompletableFuture<Void>를 반환한다.
		 * 전달된 모든 CompletableFuture가 완료되어야 CompletableFuture<Void>가 완료된다.
		 * 따라서 allOf 메서드가 반환하는 CompletableFuture에 join을 호출하면 원래 스트림의 모든 CompletableFuture의 실행 완료를
		 * 기다릴 수 있다.
		 * 
		 * 이를 이용해서 최저가격 검색 애플리케이션은 '모든 상점이 결과를 반환했거나 타임아웃되었음' 같은 메시지를 사용자에게
		 * 보여줌으로써 사용자는 추가로 가격 정보를 기다리지 않다도 된다는 사실을 보여줄 수 있다.
		 * 
		 * 반면 배열의 CompletableFuture 중 하나의 작업이 끝나길 기달리는 상황도 있을 수 있다.
		 * (예를 들어 두 개의 환율 서버에 동시 접근했을 때 한 서버의 응답만 있어도 되는 경우)
		 * 이때는 팩토리 메서드 anyOf를 사용한다.
		 * anyOf 메서드는 CompletableFuture 배열을 입력으로 받아서 CompletableFuture<Object>을 반환한다.
		 * CompletableFuture<Object>는 처음으로 완료한 CompletableFuture의 값으로 동작을 완료한다.
		 */
		CompletableFuture.allOf(futures).join();

		System.out.println("All shops have now responded in " + ((System.nanoTime() - streamStart) / 1_000_000) + " msecs");
	}

}
