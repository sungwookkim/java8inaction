package Part2.Chapter6.Chapter6_6_6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * 6.6 커스텀 컬렉터를 구현해서 성능 개선하기
 * 
 * 커스텀 컬렉터로 n까지의 자연수를 소수와 비소수 분할 성능 개선.
 */

/*
 * 1단계 : Collector 클래스 시그너처 정의
 * 
 * 정수로 이루어진 스트림에서 누적자와 최종 결과의 형식이 Map<Boolean, List<Integer>>인 컬렉터를 구현한다.
 * 즉, Map<Boolean, List<Integer>>는 참과 거짓을 키로 소수와 비소수를 구분 짓는다.
 */
class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {
	private <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
		int i = 0;

		for(A item : list) {
			// 리스트의 현재 요소가 프레디케이트를 만족하는지 검사한다.
			if(!p.test(item)) {
				// 프레디케이트를 만족하지 않으면 검사한 항목의 앞쪽에 위치한 서브 리스트를 반환한다.
				return list.subList(0, i);
			}

			i++;
		}
		
		return list;
	}

	private boolean isPrime(List<Integer> primes, int candidate) {
		int candidateRoot = (int) Math.sqrt((double) candidate);
		
		return takeWhile(primes, i -> {
				return i <= candidateRoot;	
			})
			.stream().noneMatch(i -> {
				return candidate % i == 0;
			});
	}
	
	/*
	 * 2단계 : 리듀싱 연산 구현 
	 */
	@Override
	public Supplier<Map<Boolean, List<Integer>>> supplier() {
		/*
		 * 누적자로 사용할 맵을 만들면서 true, false 키와 빈 리스트로 초기화 한다.
		 * 수집 과정에서 빈 리스트에 각각 소수와 비소수를 추가할 것이다.
		 */
		return () -> new HashMap<Boolean, List<Integer>>() {
			private static final long serialVersionUID = 1L;
			
			{
				put(true, new ArrayList<Integer>());
				put(false, new ArrayList<Integer>());
			}
		};
	}

	@Override
	public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
		/*
		 * 지금까지 발견한 소수 리스트(누적 맵의 true 키로 이들 값에 접근할 수 있다)와 소수 여부를 확인하고 싶은 candidate를
		 * 인수로 isPrime 메서드를 호출 했다.
		 * isPrime의 호출 결과로 소수 리스트 또는 비소수 리스트 중 알맞는 리스트로 candidate를 추가한다.
		 */
		return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
			acc.get(isPrime(acc.get(true), candidate)).add(candidate);
		};
	}

	/*
	 * 3단계 : 병렬 실행할 수 있는 컬렉터 만들기(가능하다면)
	 * 
	 * 예제에서는 단순하게 두 번째 맵의 소수 리스트와 비소스 리스트의 모둔 수를 첫 번째 맵에 추가하는 연산이면 충분하다. 
	 */
	@Override
	public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
		/*
		 * 참고로 알고리즘 자체가 순차적이어서 컬렉터를 실제 병렬로 사용할 순 없다.
		 * 따라서 combiner 메서드는 호출될 일이 없으므로 빈 구현으로 남겨두거나 UnsupportedOperationException을 던지도록 구현한다.
		 * 실제로 이 메서드는 사용할 일이 없지만 학습을 목적으로 구현한 것이다.
		 */
		return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
			map1.get(true).addAll(map2.get(true));
			map1.get(false).addAll(map2.get(false));
			
			return map1;
		};
	}

	/*
	 * 4단계 : finisher 메서드와 컬렉터의 characteristics 메서드 
	 */
	@Override
	public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
		/*
		 * accumulator의 형식은 컬렉터 결과 형식과 같으므로 변환 과정이 필요없다. 따라서 항등 함수 identity를 반환한다.
		 */
		return Function.identity();
	}

	@Override
	public Set<Characteristics> characteristics() {
		/*
		 * 해당 커스텀 컬렉터는 CONCURRENT도 아니고 UNORDERED도 아니지만 IDENTITY_FINISH이므로 아래 처럼 구현할 수 있다.
		 */
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH) );
	}
}

public class Main_6_6 {
	public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
		return IntStream.rangeClosed(2, n).boxed()
			.collect(Collectors.partitioningBy(candidate -> {
				int candidateRoot = (int) Math.sqrt((double) candidate);
				
				return IntStream.rangeClosed(2, candidateRoot).noneMatch(i -> candidate % i == 0);
			}));
	}

	public static Map<Boolean, List<Integer>> partitionPrimesCustomCollector(int n) {
		return IntStream.rangeClosed(2, n).boxed()
			.collect(new PrimeNumbersCollector());
	}
	
	public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
		int i = 0;

		for(A item : list) {
			// 리스트의 현재 요소가 프레디케이트를 만족하는지 검사한다.
			if(!p.test(item)) {
				// 프레디케이트를 만족하지 않으면 검사한 항목의 앞쪽에 위치한 서브 리스트를 반환한다.
				return list.subList(0, i);
			}

			i++;
		}
		
		return list;
	}

	public static boolean isPrime(List<Integer> primes, int candidate) {
		int candidateRoot = (int) Math.sqrt((double) candidate);
		
		return takeWhile(primes, i -> {
				return i <= candidateRoot;	
			})
			.stream().noneMatch(i -> {
				return candidate % i == 0;
			});
	}
	
	public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
		/*
		 * collect 메서드의 오버로드를 이용해서 핵심 로직을 구현하는 세 함수를 전달해서 같은 결과를 얻을 수 있다.
		 * 코드는 간결하지만 재사용성은 떨어진다.
		 */		
		return IntStream.rangeClosed(2, n).boxed()
			.collect(
				() -> new HashMap<Boolean, List<Integer>>() {
					private static final long serialVersionUID = 1L;
	
					{
						put(true, new ArrayList<Integer>());
						put(false, new ArrayList<Integer>());
					}
				}
				, (acc, candidate) -> {
					acc.get(isPrime(acc.get(true), candidate)).add(candidate);
				}
				, (map1, map2) -> {
					map1.get(true).addAll(map2.get(true));
					map1.get(false).addAll(map2.get(false));
				}
			);
	}
	
	public static void main(String[] args) {		
		/*
		 * 6.6.2 컬렉터 성능 비교 
		 */		
		long fastest = Long.MAX_VALUE;
		
		// 테스트를 10번 반복한다.
		for(int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			
			// 백만 개의 숫자를 소수와 비소수로 분할한다.
			partitionPrimes(1000000);
			// partitionPrimesCustomCollector(1000000);
			// partitionPrimesWithCustomCollector(1000000);
			
			// duration을 밀리초 단위로 측정한다.
			long duration = (System.nanoTime() - start) / 1000000;
			
			// 가장 빨리 실행되었는지 확인한다.
			if(duration < fastest) {
				fastest = duration;
			}
			
			System.out.println("Fastest execution done in " + fastest +" msecs");
		}
		
		
	}

}