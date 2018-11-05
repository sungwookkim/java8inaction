package Part2.Chapter5.Chapter5_5_4;

import java.util.Arrays;
import java.util.List;

/*
 * 5.4 리듀싱
 * 
 * 스트림의 모든 요소를 반복적으로 처리하는 행위에 대한 질의를 리듀싱 연산이라고 한다.
 * (모든 스트림 요소를 처리해서 값으로 도출하는)
 * 
 * 함수형 프로그래밍 언어 용어로는 이 과정이 마치 종이(우리는 스트림)를 작은 조각이
 * 될 때까지 반복해서 접는 것과 비슷하다는 의미로 폴드(fold)라고 부른다.
 */
public class Main_5_4 {

	static public void main(String[] args) {
		List<Integer> numbers = Arrays.asList(4, 5, 3, 9);
		
		/*
		 * 5.4.1 요소의 합
		 * 
		 * reduce 메서드를 보기 전에 for-each를 이용해서 리스트의 숫자 요소를 더하는 코드를 보자.
		 * 		int num = 0;
		 * 		for(int x : numbers) {
		 * 			sum += x;
		 * 		}
		 * numbers의 각 요소는 결과에 반복적으로 더해진다. 리스트에서 하나의 숫자가 남을 때까지
		 * 이 과정은 반복한다. 코드에는 두 개의 파라미터가 사용되었다.
		 * 		- sum 변수의 초기값 0
		 * 		- 리스트의 모든 요소를 조합하는 연산(+) 
		 */
		
		/*
		 * reduce는 두 개의 인수를 갖는다.
		 * 		- 초기값 0
		 * 		- 두 요소를 조합해서 새로운 값을 만드는 BinaryOprator<T>
		 * 
		 * 아래 코드 (a, b) -> a + b 관련해서 reduce 메서드가 처리하는 흐름을 설명 하자면
		 * (처음은 reduce 메서드의 첫 번쨰 인자로 받은 값을 더하면서 시작한다.)
		 * 		1. 0 + 4 = (0, 4) -> 0 + 4;	
		 * 		2. 4 + 5 = (4, 5) -> 4 + 5
		 * 		3. 9 + 3 = (9, 3) -> 9 + 3;
		 * 		4. 12 + 9 = (12, 9) -> 12 + 9;
		 * 흐름과 같는다. 즉,  
		 * 		("람다 표현식에서 반환된 값", "요소") -> "람다 표현식에 반환된 값" + "요소";
		 * 같은 형식으로 인수가 전달되게 된다. 
		 */		
		System.out.println("5.4.1 요소의 합 - 람다 표현식 : " + numbers.stream().reduce(0, (a, b) -> a + b));
		System.out.println("5.4.1 요소의 합 - 메서드 레퍼런스 : " + numbers.stream().reduce(0, Integer::sum));
		
		/*
		 * 초기값 없음
		 * 
		 * 초기값이 없는 reduce도 있다. 그러나 이 reduce는 Optional 객체를 반환한다.
		 * 스트림에 아무 요소도 없는 상황을 생각해보자. 이런 상황이라면 초기값이 없으므로 reduce는
		 * 합계를 반환할 수 없다. 따라서 합계가 없음을 가리킬 수 있도록 Optional 객체로 감싼 결과를 반환한다.
		 */
		System.out.println("초기값 없는 reduce 메서드 : " + numbers.stream().reduce((a, b) -> a + b).get());
		
		/*
		 * 5.4.2 최대값과 최소값
		 *  
		 * reduce 연산은 새로운 값을 이용해서 스트림의 모든 요소를 소비할 때까지 람다를 반복 수행한다.
		 * 아래는 최대값과 최소값을 구하는 코드들이다.
		 */
		// 최대값 구하기
		System.out.println("5.4.2 최대값 - 람다 표현식 : " + numbers.stream().reduce((a, b) -> a < b ? b : a).get());		
		System.out.println("5.4.2 최대값 - 메서드 레퍼런스 : " + numbers.stream().reduce(Integer::max).get());
		
		// 최소값 구하기
		System.out.println("5.4.2 최소값 - 람다 표현식 : " + numbers.stream().reduce((a, b) -> a < b ? a : b).get());		
		System.out.println("5.4.2 최소값 - 메서드 레퍼런스 : " + numbers.stream().reduce(Integer::min).get());
		
		/*
		 * reduce 메서드의 장점과 병렬화
		 * 
		 * reduce를 이용하면 내부 반복이 추상화되면서 내부 구현에서 병렬로 reduce를 실행할 수 있게 된다.
		 * 반복적인 합계에서는 sum 변수를 공유해야 하므로 쉽게 병렬화하기 어렵다.
		 * 강제적으로 동기화시킨다 하더라도 결국 병렬화로 얻어야 할 이득이 스레드 간의 소모적인 경쟁 때문에
		 * 상쇄되어 버리기 때문이다. 
		 */
		
		/*
		 * 아래 코드는 parallelStream 메서드를 이용해서 병렬처리를 수행한다. 하지만 병렬로 실행하려면 대가를 지불해야 한다.
		 * 즉, reduce에 넘겨준 람다의 상태(인스턴스 변수 같은)가 바뀌지 말아야 하며, 연산이 어떤 순서로 실행되더라도
		 * 결과가 바뀌지 않는 구조여야 한다.
		 */
		System.out.println("reduce 메서드의 장점과 병렬화 : " + numbers.parallelStream().reduce(0, Integer::sum));
		
		/*
		 * 스트림 연산 : 상태 없음과 상태 있음
		 * 
		 * map, filter 등은 입력 스트림에서 각 요소를 받아 0 또는 결과를 출력 스트림으로 보낸다.
		 * 따라서(사용자가 제공한 람다나 메서드 레퍼런스가 내부적인 가변 상태를 갖지 않는다는 가정 하에)
		 * 이들은 보통 상태가 없는, 즉 내부 상태를 갖지 않는 연산(stateless operation)이다.
		 * 
		 * 하지만 sorted나 distinct는 filter나 map과는 다르다.
		 * 스트림의 요소를 정렬하거나 중복을 제거하려면 과거의 이력을 알고 있어야 한다.
		 * 즉, 어떤 요소를 출력 스트림으로 추가하려면 "모든 요소가 버퍼에 추가되어 있어야 한다."
		 * 연산을 수행하는데 필요한 저장소 크기는 정해져있지 않기 때문에 데이터 스트림의 크기가 크거나
		 * 무한이라면 문제가 생길수 있다. 따라서 이러한 연산을 내부 상태를 갖는 연산(stateful operation)으로
		 * 간주할 수 있다.
		 */
		
		/*
		 * 중간 연산과 최종 연산
		 * 
		 * 연산 : filter
		 * 형식 : 중간 연산
		 * 반환 형식 : Stream<T>
		 * 사용된 함수형 인터페이스 형식 : Predicate<T> 
		 * 함수 디스크립터 : T -> boolean
		 * 
		 * 연산 : distinct
		 * 형식 : 중간 연산(상태 있는 언바운드)
		 * 반환 형식 : Stream<T>
		 * 사용된 함수형 인터페이스 형식 :  
		 * 함수 디스크립터 :
		 * 
		 * 연산 : skip
		 * 형식 : 중간 연산(상태 있는 바운드)
		 * 반환 형식 : Stream<T> 
		 * 사용된 함수형 인터페이스 형식 : Long 
		 * 함수 디스크립터 : 
		 * 
		 * 연산 : limit
		 * 형식 : 중간 연산(상태 있는 바운드)
		 * 반환 형식 : Stream<T>
		 * 사용된 함수형 인터페이스 형식 : Long 
		 * 함수 디스크립터 : 
		 * 
		 * 연산 : map
		 * 형식 : 중간 연산
		 * 반환 형식 : Stream<R>
		 * 사용된 함수형 인터페이스 형식 : Function<T, R> 
		 * 함수 디스크립터 : T -> R
		 * 
		 * 연산 : flatMap
		 * 형식 : 중간 연산
		 * 반환 형식 : Stream<R>
		 * 사용된 함수형 인터페이스 형식 : Function<T, Stream<R>> 
		 * 함수 디스크립터 : T -> Stream<R>
		 * 
		 * 연산 : sorted
		 * 형식 : 중간 연산(상태 있는 언바운드)
		 * 반환 형식 : Stream<T> 
		 * 사용된 함수형 인터페이스 형식 : Comparator<T> 
		 * 함수 디스크립터 : (T, T) -> int
		 * 
		 * 연산 : anyMatch
		 * 형식 : 최종 연산
		 * 반환 형식 : boolean
		 * 사용된 함수형 인터페이스 형식 : Predicate<T> 
		 * 함수 디스크립터 : T -> boolean
		 * 
		 * 연산 : noneMath
		 * 형식 : 최종 연산
		 * 반환 형식 : boolean
		 * 사용된 함수형 인터페이스 형식 : Predicate<T> 
		 * 함수 디스크립터 : T -> boolean
		 * 
		 * 연산 : allMatch
		 * 형식 : 최종 연산
		 * 반환 형식 : boolean
		 * 사용된 함수형 인터페이스 형식 : Predicate<T> 
		 * 함수 디스크립터 : T ->boolean
		 * 
		 * 연산 : findAny
		 * 형식 : 최종 연산
		 * 반환 형식 : Optional<T> 
		 * 사용된 함수형 인터페이스 형식 : 
		 * 함수 디스크립터 : 
		 * 
		 * 연산 : findFirst
		 * 형식 : 최종 연산
		 * 반환 형식 : Optional<T>
		 * 사용된 함수형 인터페이스 형식 : 
		 * 함수 디스크립터 : 
		 * 
		 * 연산 : forEach
		 * 형식 : 최종 연산
		 * 반환 형식 : void
		 * 사용된 함수형 인터페이스 형식 : Consumer<T> 
		 * 함수 디스크립터 : T -> void
		 * 
		 * 연산 : collect
		 * 형식 : 최종 연산
		 * 반환 형식 : R
		 * 사용된 함수형 인터페이스 형식 : Collector<T, A, R> 
		 * 함수 디스크립터 : 
		 * 
		 * 연산 : reduce
		 * 형식 : 최종 연산(상태 있는 바운드)
		 * 반환 형식 : Optional<T>
		 * 사용된 함수형 인터페이스 형식 : BinaryOperator<T> 
		 * 함수 디스크립터 : (T, T) -> T
		 * 
		 * 연산 : count
		 * 형식 : 최종 연산
		 * 반환 형식 : long
		 * 사용된 함수형 인터페이스 형식 : 
		 * 함수 디스크립터 : 
		 */
	}
	
}
