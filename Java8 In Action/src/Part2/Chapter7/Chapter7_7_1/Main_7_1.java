package Part2.Chapter7.Chapter7_7_1;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/*
 * 7.1 병렬 스트림
 * 
 * 컬렉션에서 parallelStream을 호출하면 병렬 스트림(parallel stream)이 생성된다.
 * 병렬 스트림이란 각각의 스레드에서 처리할 수 있도록 스트림 요소를 여러 청크로 분할한 스트림이다.
 * 따라서 병렬 스트림을 이용하면 모든 멀티코어 프로세서가 각각의 청크를 처리하도록 할당할 수 있다.
 */
public class Main_7_1 {

	public static long meansureSumPerf(Function<Long, Long> adder, long n) {
		long fastest = Long.MAX_VALUE;
		
		for(int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			long sum = adder.apply(n);
			long duration = (System.nanoTime() - start) / 1_000_000;
			
			System.out.println("Result : " + sum);
			
			if(duration < fastest) {
				fastest = duration;
			}			
		}
		
		return fastest;
	}
	
	public static void main(String[] args) {
		/*
		 * 7.1.1 순차 스트림을 병렬 스트림으로 변환하기
		 */
		System.out.println("7.1.1 순차 스트림을 병렬 스트림으로 변환하기 - 전통 자바 : " + ParallelStream.iterativeSum(100));
		System.out.println("7.1.1 순차 스트림을 병렬 스트림으로 변환하기 - 순차 : " + ParallelStream.sequentialSum(100));		
		System.out.println("7.1.1 순차 스트림을 병렬 스트림으로 변환하기 - parallel : " + ParallelStream.parallelSum(100));
		
		/*
		 * 병렬 스트림에서 사용하는 스레드 풀 설정
		 * 
		 * 스트림의 parallel 메서드에서 병렬로 작업을 수행 하는 스레드 생성, 생성 갯수, 그리고 그 과정을 어떻게 커스텀마이징할 수 있는지
		 * 궁금할 것이다.
		 * 
		 * 병렬 스트림은 내부적으로 ForkJoinPool을 사용한다
		 * 기본적으로 ForkJoinPool은 프로세스 수, 즉 Runtime.getRuntime().availableProcessors()가 반환하는 값에 상응하는 스레드를 갖는다.
		 * 
		 * System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", 12);
		 * 
		 * 위 코드는 전역 설정 코드이므로 이후의 모든 병렬 스트림 연산에 영향을 준다.
		 * 현재는 하나의 병렬 스트림에 사용할 수 있는 특정한 값을 지정할 수 없다.
		 * 일반적으로 기기의 프로세서 수와 같으므로 특별한 이유가 없다면 FokJoinPool의 기본값을 그대로 사용하는것을 권장한다.
		 */
		
		/*
		 * 7.1.2 스트림 성능 측정
		 * 
		 * 병렬화를 이용하면 순차나 반복 형식에 비해 성능이 더 좋아질 것이라 추측할 것이다.
		 * 성능을 최적화의 세 가지 황금 규칙인 첫째도 측정, 둘째도 측정, 셋째도 측정에 따라 성능을 측정해 보자.
		 */

		/*
		 * 고전적인 for 루프를 사용한 반복 버전이 생각보다 빠르다는 점도 고려해야 한다.
		 * for 루프는 저수준으로 작동하며 기본값을 박싱하거나 언박싱할 필요가 없으므로 수행 속도가 빠르다.
		 */
		System.out.println("7.1.2 스트림 성능 측정 - 전통 자바 : " + meansureSumPerf(ParallelStream::iterativeSum, 10_000_000));
		System.out.println("7.1.2 스트림 성능 측정 - 순차 : " + meansureSumPerf(ParallelStream::sequentialSum, 10_000_000));
		
		/*
		 * 병렬 버전이 순차 버전보다 늦게 동작하는 이유는
		 * 		iterate가 박싱된 객체를 생성하므로 이를 다시 언박싱하는 과정이 필요하다.
		 * 		iterate가 병렬로 실행될 수 있도록 독립적인 청크로 분할하기가 어렵다.
		 * 
		 * iterate는 본질적으로 순차적이라 청크로 분할하기가 어렵다.
		 * 이와 같은 상황 때문에 리듀싱 연산이 수행되지 않는다.
		 * 리듀싱 과정을 시작하는 시점에 전체 숫자 리스트가 준비되지 않았으므로 스트림을 병렬로 처리할 수 있도록
		 * 청크로 분할할 수 없다.
		 * 스트림이 병렬로 처리되도록 지시했고 각각의 합계가 다른 스레드에서 수행되었지만 결국 순차처리 방식과
		 * 크게 다른 점이 없으므로 스레드를 할당하는 오버헤드만 증가하게 된다. 
		 */
		System.out.println("7.1.2 스트림 성능 측정 - parallel : " + meansureSumPerf(ParallelStream::parallelSum, 10_000_000));
		
		/*
		 * 더 특화된 메서드 이용
		 * 
		 * iterate가 LongStream.rangeClosed 비해 다음과 같은 장점을 제공한다.
		 * 		LongStream.rangeClosed는 기본형 long을 직접 사용하므로 박싱과 언방식 오버헤드가 사라진다.
		 * 		LongStream.rangeClosed는 쉽게 청크로 분할할 수 있는 숫자 범위를 생상한다.
		 *		예를 들어 1-20 범위의 숫자를 각각 1-5, 6-10, 11-15, 16-20 범위의 숫자로 분할할 수 있다.
		 *
		 * 기존 iterate 팩토리 메서드로 생성한 순차 버전에 비해 이 예제의 순차 스트림 처리 속도가 더 빠르다.
		 * 특화되지 않은 스트림을 처리할 때는 오토박싱, 언박싱 등의 오버헤드를 수반하기 때문이다.
		 * 상황에 따라서 어떤 알고리즘을 병렬화하는 것보다 적절한 자료구조를 선택하는 것이 더 중요하다.
		 */
		System.out.println("더 특화된 메서드 이용 - 순차 : " + meansureSumPerf(ParallelStream::rangeSum, 10_000_000));
		System.out.println("더 특화된 메서드 이용 - parallel : " + meansureSumPerf(ParallelStream::newParallelSum, 10_000_000));

		/*
		 * 7.1.3 병렬 스트림의 올바른 사용법
		 */
		System.out.println("7.1.3 병렬 스트림의 올바른 사용법 - 잘못된 예 : " + meansureSumPerf(ParallelStream::sideEffectParallelSum, 10_000_000));
		
		/*
		 * 7.1.4 병렬 스트림 효과적으로 사용하기
		 * 
		 * '천 개 이상의 요소가 있을 대만 병렬 스트림을 사용하라'와 같이 양을 기준으로 병렬 스트림 사용을 결정ㅎ하는 것은 적절하지 않다.
		 * 그래도 어떤 상황에서 병렬 스트림을 사용할 것인지 약간의 수량적 힌트를 정하는 것이 도움이 될 때도 있다.
		 * 
		 * - 확신이 서지 않는다면 직접 측정하다. 
		 * 병렬 스트림의 수행 과정은 투명하지 않을 때가 많다.
		 * 따라서 순차 스트림과 병렬 스트림 중 어떤 것이 좋을지 모르겠다면 적절한 벤치마크로 직접 성능을 축정하는 것이 바람직하다.
		 * 
		 * - 박싱을 주의하라.
		 * 자동 박싱과 언방식은 성능을 크게 저하시킬 수 있는 요소다. 자바 8은 박싱 동작을 피할 수 있도록 기본형 특화 스트림
		 * (IntStream, LongStream, DoubleStream)을 제공한다. 따라서 되도록이면 기본형 특화 스트림을 사용하는 것이 좋다.
		 * 
		 * - 순차 스트림보다 병렬 스트림에서 성능이 떨어지는 연산이 있다
		 * limit나 findFirst처럼 요소의 순서에 의존하는 연산을 병렬 스트림에서 수행하려면 비싼 비용을 치러야 한다.
		 * 예를 들어 findAny는 요소의 순서와 상관없이 연산하므로 findFirst보다 성능이 좋다. 정렬된 스트림에 unordered를
		 * 호출하면 비정렬된 스트림을 얻을 수 있다. 스트림에 N개 요소가 있을 때 요소의 순서가 상관없다면(예를 들어 소스가 리스트라면)
		 * 비정렬된 스트림에 limit를 호출하는 것이 효율적이다.
		 * 
		 * - 스트림에서 수행하는 전체 파이프라인 연산 비용을 고려하라.
		 * 처리해야 할 요소 수가  N이고 하나의 요소를 처리하는 데 드는 비용을 Q라 하면 전체 스트림 파이프라인 처리 비용은 N*Q로 예상할 수 있다.
		 * Q가 높아진다는 것은 병렬 스트림으로 성능을 개선할 수 있는 가능성이 있음을 의미한다.
		 * 
		 * - 소량의 데이터에서는 병렬 스트림이 도움 되지 않는다.
		 * 소량의 데이터를 처리하는 상황에서는 병렬화 과정에서 생기는 부가 비용을 상쇄할 수 있을 만큼의 이득을 얻지 못하기 때문이다.
		 * 
		 * - 스트림을 구성하는 자료구조가 적절한지 확인하라.
		 * 예를 들어 ArrayList를 LinkedList보다 효율적으로 분할할 수 있다. LinkedList를 분할하려면 모든 요소를 탐색해야 하지만
		 * ArrayList는 요소를 탐색하지 않고도 리스트를 분할할 수 있기 때문이다.
		 * 또한 range 택토리 메서드로 만든 기본형 스트림도 쉽게 분해할 수 있다.(Spliterator를 구현해서 분해 과정을 완벽하게 제어할 수 있다.)
		 * 
		 * - 스트림의 특성과 파이프라인의 중간 연산이 스트림의 특성을 어떻게 바꾸는지에 따라 분해 과정의 성능이 달라질 수 있다.
		 * 예를 들어 SIZED 스트림은 정확히 같은 크기의 두 스트림으로 분할할 수 있으므로 효과적으로 병렬 처리 할 수 있다.
		 * 반면 필터 연산이 있으면 스트림의 길이를 예측할 수 없으므로 효과적으로 스트림을 병렬 처리할 수 있을지 알 수 없게 된다.
		 * 
		 * - 최종 연산의 병합 과정(예를 들면 Collector의 combiner 메서드) 비용을 살펴보라.
		 * 병합 과정의 비용이 비싸다면 병렬 스트림의 얻은 성능의 이익이 서브스트림의 부분결과를 합치는 과정에서 상쇄할 수 있다.
		 */
		
		/*
		 * 스트림 소스의 분해성 
		 * 소스 : ArrayList
		 * 분해성 : 훌륭함
		 * 
		 * 소스 : LinkedList
		 * 분해성 : 나쁨
		 * 
		 * 소스 : IntStream.range
		 * 분해성 : 훌륭함
		 * 
		 * 소스 : Stream.iterate
		 * 분해성 : 나쁜
		 * 
		 * 소스 : HashSet
		 * 분해성 : 젛음
		 * 
		 * 소스 : TreeSet
		 * 분해성 : 좋음
		 */
		
	}

	static class ParallelStream {
		public static long sequentialSum(long n) {
			return Stream.iterate(1L, i -> i + 1)
				.limit(n)
				.reduce(0L, Long::sum);
		}

		public static long iterativeSum(long n) {
			long result = 0;
			
			for(long i = 1L; i <= n; i++) {
				result += i;
			}
			
			return result;
		}

		public static long parallelSum(long n) {
			/*
			 * 순차 스트림에 parallel 메서드를 호출하면 기존의 함수형 리듀싱 연산(숫자 합계 계산)이 병렬로 처리된다.
			 * 스트림이 여러 청크로 분할이 되고 리듀싱 연산을 여러 청크에 병렬로 수행할 수 있다.
			 * 마지막으로 리듀싱 연산으로 생성된 부분결과를 다시 리듀싱 연산으로 합쳐서 전체 스트림의 리듀싱 결과를 도출한다.
			 * 
			 * 사실 순차 스트림에 parallel을 호출해도 스트림 자체에는 아무 변화도 일어나지 않는다.
			 * 내부적으로는 parallel을 호출하면 이후 연산이 병렬로 수행해야 함을 의미하는 불린 플래그가 설정된다.
			 * 반대로 sequential로 병렬 스트림을 순차 스트림으로 바꿀 수 있다.
			 * 이 두 메서드를 이용하면 어떤 연산은 병렬로 어떤 연산은 순차로 실행할지 제어할 수 있다.
			 * 
			 * stream.parallel()
			 * 		.filter(...)
			 * 		.sequential()
			 * 		.map(...)
			 * 		.parallel()
			 * 		.reduce();
			 * 
			 * parallel과 sequential 두 메서드 중 최종적으로 호출된 메서드가 전체 파이프라인에 영향을 미친다.
			 * 위 예제에서는 파이프라인의 마지막 호출은 parallel이므로 파이프라인은 전체적으로 병렬로 실행된다.
			 */
			return Stream.iterate(1L, i -> i + 1)
				.limit(n)
				.parallel()
				.reduce(0L, Long::sum);
		}
		
		public static long rangeSum(long n) {
			return LongStream.rangeClosed(1, n).reduce(0L, Long::sum);
		}
		
		public static long newParallelSum(long n) {		
			return LongStream.rangeClosed(1, n)
				.parallel()
				.reduce(0L, Long::sum);
		}
		
		public static long sideEffectParallelSum(long n) {
			/*
			 * 아래 코드는 본질적으로 순차 실행할 수 있도록 구현되어 있기 때문에 병렬로 실행하면 참사가 발생된다.
			 * 특히 total을 접근할 때마다(다수의 스레드에서 동시에 데이터에 접근하는) 데이터 레이스 문제가 발생된다.
			 * 동기화로 문제를 해결하면 결국 병렬화라는 특성이 없어져 버린다.
			 */
			Accumulator accumulator = new Accumulator();
			LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
			
			return accumulator.total;
			
		}
	}
}

class Accumulator {
	/*
	 * 다수의 쓰레드가 하나의 total 변수를 참조 하려 하기 때문에 데이터 레이스 문제 발생 및
	 * 여러 스레드가 동시에 add 메서드를 실행하기 때문에 올바른 결과값이 나오질 않는다. 
	 */
	public long total = 0;
	
	/*
	 * 얼핏 보면 아토믹 연산(atomic operation) 같지만 total += value 아토믹 연산이 아니다.
	 * 결국 여러 스레드에서 공유하는 객체의 상태를 바꾸는 forEach 블록 내부에서 add 메서드를 호출하면서
	 * 이 같은 문제가 발생된다.
	 */
	public void add(long value) { total += value; } 
}
