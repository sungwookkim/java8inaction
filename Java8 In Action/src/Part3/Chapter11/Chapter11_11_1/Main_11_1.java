package Part3.Chapter11.Chapter11_11_1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/*
 * 11.1 Future
 * 
 * 자바 5부터는 미래의 어느 시점에 결과를 얻는 모델에 활용할 수 있도록 Future 인터페이스를 제공하고 있다.
 * 비동기 계산을 모델링하는데 Future를 이용할 수 있으며, Future는 계산이 끝났을 때 결과에 접근할 수 있는 레퍼런스를 제공한다.
 * 시간이 걸릴수 있는 작업을 Future 내부로 설정하면 호출자 스레드가 결과를 기달리는 동안 다른 유용한 작업을 수행할 수 있다.
 * Futhre는 저수준의 스레드에 비해 직관적으로 이해하기 쉽다는 장점이 있다.
 * Future를 이용하려면 시간이 오래 걸리는 작업을 Callable 객체 내부로 감싼 다음에 ExecutorService에 제출해야 한다. 
 */
public class Main_11_1 {

	public static void main(String[] args) {
		/*
		 * ExecutorService에서 제공하는 스레드가 시간이 오래 걸리는 작업을 처리하는 동안 우리 스레드로 다른 작업을 동시에 실행할 수 있다.
		 * 다른 작업을 처리하다가 시간이 오래 걸리는 작업의 결과가 필요한 시점이 되었을 때 Future의 get 메서드로 결과를 가져올 수 있다.
		 * get 메서드를 호출했을 때 이미 계산이 완료되어 결과가 준비되었다면 즉시 결과를 반환하지만 결과가 준비되지 않았다면
		 * 작업이 완료될 때까지 우리 스레드를 블록시킨다.
		 */
		System.out.println(new examFuture().futureExecute());
		
		/*
		 * 11.1.1 Future 제한
		 * 
		 * 여러 Future의 결과가 있을 때 이들의 의존성은 표현하기가 어렵다.
		 * 즉 '오래 걸리는 A라는 계산이 끝나면 그 결과를 다른 오래 걸리는 계산 B로 전달하시오. 그리고 B의 결과가 나오면 다른 질의의 결과와
		 * B의 결과를 조합하시오'와 같은 요구사항을 쉽게 구현할 수 있어야 한다.
		 * 
		 * 따라서 다음과 같은 선언형 기능이 필요하다.
		 * - 두 개의 비동기 계산 결과를 하나로 합친다. 두 가지 결과 계산은 서로 독립적일 수 있으며 또는 두 번째 결과가 첫 번째 결과에
		 * 의존하는 상황일 수 있다.
		 * 
		 * - Future 집합이 실행하는 모든 태스크의 완료를 기다린다.
		 * 
		 * - Future 집합에서 가장 빨리 완료되는 태스크를 기달렸다가 결과를 얻는다.
		 * (여러 태스크가 다양한 방식으로 같은 결과를 구하는 상황.)
		 * 
		 * - 프로그램적으로 Future를 완료시킨다.(비동기 동작에 수동으로 결과 제공.)
		 * 
		 * - Future 완료 동작에 반응한다.
		 * (결과를 기다리면서 블록되지 않고 결과가 준비되었다는 알림을 받은 다음에 Future의 결과로 원하는 추가 동작을 수행할 수 있음.)
		 * 
		 * 지금까지 설명한 기능을 선언형으로 이용할 수 있도록 자바 8에서 새로 제공하는 CompletableFuture 클래스(Future 인터페이스 구현체)
		 * 가 있다. Stream과 CompletableFuture는 비슷한 패턴이다. 즉 람다 표현식과 파이프라이닝을 활용한다.
		 * 따라서 Future와 CompletableFuture의 관계를 Collection과 Stream의 관계에 비유할 수 있다. 
		 */
		
		/*
		 * 11.1.2 CompletbleFuture로 비동기 애플리케이션 만들기
		 * 
		 * 어떤 제품이나 서비스를 이용해야 하는 상황의 가정에서 예산을 줄일 수 있도록 여러 온라인상점 중 가장 저렴한 가격을 제시하는 상점을
		 * 찾는 애플리케이션을 만든다.
		 * 애플리케이션을 만드는 동안 다음과 같은 기술을 배운다.
		 * 
		 * 첫째 고객에게 비동기 API를 제공하는 방법을 배운다.
		 * 
		 * 둘째 동기 API를 사용해야 할 때 코드를 비블록으로 만드는 방법을 배운다.
		 * 두 개의 비동기 동작을 파이프라인으로 만드는 방법과 두 개의 동작 결과를 하나의 비동기 계산으로 합치는 방법을 배운다.
		 * 예를 들어 온라인상점에서 우리가 사려는 물건에 대응하는 할인 코드를 반환한다고 가정하자. 우리는 다른 원격 할인 서비스에
		 * 접근해서 할인 코드에 해당하는 할인율을 찾아야한다. 그래야 원래 가격에 할인율을 적용해서 최종 결과를 계산할 수 있다.
		 * 
		 * 셋째 비동기 동작의 완료에 대응하는 방법을 배운다.
		 * 모든 상점에서 가격 정보를 얻을 때까지 기다리는 것이 아니라 각 상점에서 가격 정보를 얻을 때마다 즉시 최저가격을 찾는
		 * 애플리케이션을 갱신하는 방법을 설명한다.(그렇지 않으면 서버 다운 등 문제가 발생했을 때 사용자에게 검은 화면만 보여주게 된다.)
		 */

		/*
		 * 동기 API와 비동기 API
		 * 
		 * 동기 API
		 * 전통적인 동기 API는 메서드를 호출한 다음에 메서드의 계산을 완료될 때까지 기다린 다음 메서드가 반환되면 호출자는 반환된 값으로
		 * 계속 다른 동작을 수행한다.
		 * 호출자와 피호출자가 각각 다른 스레드에서 실행되는 상황이었더라도 호출자는 피호출자의 동작 완료를 기달린다.
		 * 이처럼 동기 API를 사용하는 상황을 '블록 호출(blocking call)' 이라고 한다.
		 * 
		 * 비동기 API
		 * 메서드가 즉시 반환되며 끝내지 못한 나머지 작업을 호출자 스레드와 동기적으로 실행될 수 있도록 다른 스레드에 할당한다.
		 * 이와 같은 비동기 API를 사용하는 상황을 '비블록 호출(non-blocking call)'이라고 한다.
		 * 다른 스레드에 할당된 나머지 계산 결과는 콜백 메서드를 호출해서 전달하거나 호출자가 '계산 결과가 끝날 때까지 기달림' 메서드를
		 * 추가로 호출하면서 전달된다.
		 * 주로 I/O 시스템 프로그래밍에서 이와 같은 방식으로 동작을 수행하며 즉, 계산 동작을 수행하는 동안 비동기적으로 디스크 접근을 수행한다.
		 * 그리고 더 이상 수행할 동작이 없으면 디스크 블록이 메모리로 로딩될 때까지 기다린다. 
		 */
	}
}

class examFuture {

	public Double futureExecute() {
		Double result = new Double(0);

		// 스레드 풀에 태스크를 제출하려면 ExecutorService를 생성.
		ExecutorService executor = Executors.newCachedThreadPool();
		
		// Callable을 ExecutorService로 제출.
		Future<Double> future = executor.submit(new Callable<Double>() {
			public Double call() {
				// 시간이 오래 걸리는 작업은 다른 스레드에서 비동기적으로 실행.
				return doSomeLongComputation();
			}
		});
		
		// 비동기 작업을 수행하는 동안 다른 작업을 실행.
		this.doSomeThingElse();
		
		try {
			
			/*
			 * 비동기 작업의 결과를 가져온다. 결과가 준비되어 있지 않으면 호출 스레드가 블록된다.
			 * 최대 1초까지만 기달린다.
			 * 
			 * get 메서드 호출 시 우리 스레드가 블록이 되기 때문에 오래 걸리는 작업이 영원히 끝나지 않으면 
			 * 작업이 끝나지 않는 문제가 생길 수 있으므로 get 메서드를 오버로드해서 우리 스레드가 대기할 최대 타임아웃 시간을
			 * 설정하는 것이 좋다.
			 */
			result = future.get(1, TimeUnit.SECONDS);
		} catch(ExecutionException ee) {
			System.out.println("계산 중 에러가 발생하였습니다.");
		} catch(InterruptedException ie) {
			System.out.println("현재 스레드에서 대기 중 인터럽트가 발생.");
		} catch(TimeoutException te) {
			System.out.println("Future가 완료되기 전에 타임아웃이 발생.");
		}
		
		return result;
	}
	
	private Double doSomeLongComputation() {
		return IntStream.rangeClosed(1, 100_000_000).asDoubleStream().sum();
	}
	
	private void doSomeThingElse() {
		System.out.println("doSomeThingElse method execute!!"); 
	}
}
