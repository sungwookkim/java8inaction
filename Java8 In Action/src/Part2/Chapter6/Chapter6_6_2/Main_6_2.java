package Part2.Chapter6.Chapter6_6_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Part2.Chapter6.Chapter6_6_2.entity.Dish;

/*
 * 6.2 리듀싱과 요약
 * 
 * 컬렉터(Stream.collect 메서드의 인수)로 스트림의 항목을 컬렉션으로 재구성할 수 있다.
 * 좀 더 일반적으로 컬렉터로 스트림의 모든 항목을 하나의 결과로 합칠수 있다.
 * 트리를 구성하는 다수준 맵, 메뉴의 칼로리 합계를 가리키는 단순한 정수 등 다양한 형식으로
 * 결과를 도출될 수 있다.
 */
public class Main_6_2 {

	public static void main(String[] args) {
		List<Dish> menu = Arrays.asList(
			new Dish("pork", false, 800, Dish.Type.MEAT)	
			, new Dish("beef", false, 700, Dish.Type.MEAT)				
			, new Dish("chicken", false, 400, Dish.Type.MEAT)
			, new Dish("french fries", true, 530, Dish.Type.OTHER)
			, new Dish("rice", true, 350, Dish.Type.OTHER)
			, new Dish("season fruit", true, 120, Dish.Type.OTHER)
			, new Dish("pizza", true, 550, Dish.Type.OTHER)
			, new Dish("prawns", false, 300, Dish.Type.FISH)
			, new Dish("salmon", false, 450, Dish.Type.FISH));
		
		/*
		 * 첫 번째 예제로 counting() 이라는 팩토리 메서드가 반환하는 컬렉터로 메뉴에서 요리 수를 계산한다.
		 * counting 컬렉터는 다른 컬렉터와 함께 사용할 때 위력을 발휘한다.
		 */
		System.out.println("6.2 리듀싱과 요약 - Collectors.counting 사용 : " + menu.stream().collect(Collectors.counting()) );
		
		/*
		 * 아래처럼 불필요한 과정을 생략할 수 있다.
		 */
		System.out.println("6.2 리듀싱과 요약 - count 메서드 사용 : " + menu.stream().count() );
		
		/*
		 * 6.2.1 스트림값에서 최대값과 최소값 검색
		 * 
		 * 메뉴에서 칼로리가 가장 높거나 낮은 요리를 찾는다고 가정하자. 
		 * Collectors.maxBy, Collectors.minBy 두 개의 메서드를 이용할 수 있다.
		 * 두 컬렉터는 스트림의 요소를 비교하는데 사용할 Comparator를 인수로 받는다.
		 * 
		 * Optional<Dish>는 만약 menu가 비어있다면 그 어떤 요리도 반환되지 않을 것이다.
		 * 자바 8은 값을 포함하거나 포함하지 않을 수 있는 컨테이너 Optional을 제공한다.
		 * 
		 * 또한 스트림에 있는 객체의 숫자 필드의 합계나 평균 등을 반환하는 연산에도 리듀싱 기능이
		 * 자주 사용된다. 이러한 연산을 요약(summarization)연산이라 부른다.
		 */
		Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
		
		Optional<Dish> mostCalorieDish = menu.stream().collect(Collectors.maxBy(dishCaloriesComparator));
		System.out.print("6.2.1 스트림값에서 최대값과 최소값 검색- Collectors.maxBy 사용 : ");
		System.out.println("이름 = " + mostCalorieDish.get() + ", 칼로리 = " + mostCalorieDish.get().getCalories() );
		
		mostCalorieDish = menu.stream().collect(Collectors.minBy(dishCaloriesComparator));
		System.out.print("6.2.1 스트림값에서 최대값과 최소값 검색 - Collectors.minBy 사용 : ");
		System.out.println("이름 = " + mostCalorieDish.get() + ", 칼로리 = " + mostCalorieDish.get().getCalories() );
		
		/*
		 * 6.2.2 요약 연산
		 * 
		 * Collectors 클래스는 Collectors.summingInt라는 특별한 요약 팩토리 메서드를 제공한다.
		 * summingInt는 객체를 int로 매핑하는 함수를 인수로 받고 인수로 전달된 함수는 객체를 int로
		 * 매핑한 컬렉터를 반환한다.
		 * 그리고 summingInt가 collect 메서드로 전달되면 요약 작업을 수행한다.
		 * 
		 * 다음은 메뉴 리스트의 총 칼로리를 계산하는 코드이다.
		 */
		System.out.println("6.2.2 요약 연산 - Collectors.summingInt : " 
			+ menu.stream().collect(Collectors.summingInt(Dish::getCalories)) );	
		
		/*
		 * Collectors.summingLong과 Collectors.summingDouble도 summingInt와 같은 방식으로 동작하며
		 * 각각 long 또는 double 형식의 데이터로 요약한다는 점만 다르다.
		 * 
		 * 이러한 단순 합계 외 평균값 계산 등의 연산도 요약 기능으로 제공된다.
		 * 즉, Collectors.averagingInt, Collectors.averagingLong, Collectors.averagingDouble 등
		 * 다양한 숫자 집합의 평균을 계산할 수 있다.
		 */
		System.out.println("6.2.2 요약 연산 - Collectors.averagingInt : " 
			+ menu.stream().collect(Collectors.averagingInt(Dish::getCalories)) );
		
		/*
		 * 간혹 위 연산 중 두 개 이상의 연산을 한번에 수행해야 할 때도 있다.
		 * 이런 상황에서는 팩토리 메서드 summarizingInt가 반환하는 컬렉터를 사용할 수 있다. 
		 * 
		 * int뿐 아니라 long이나 double에 대응하는 Collectors.summarizingLong, Collectors.summarizingDouble 메서드와
		 * 관련된 LongSummaryStatistics, DobuleSummaryStatistics 클래스도 있다.
		 */
		IntSummaryStatistics menuStatistics = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
		System.out.println("6.2.2 요약 연산 - Collectors.summarizingInt : " + menuStatistics);
		
		/*
		 * 6.2.3 문자열 연결
		 * 
		 * 컬렉터에서 joining 팩토리 메서드를 이용하면 스트림의 각 객체에 toString 메서드를 호출해서
		 * 추출한 모든 문자열을 하나의 문자열로 연결해서 반환한다.
		 * 
		 * 아래 코드는 메뉴의 모든 요리명을 연결하는 코드다.
		 */
		System.out.println("6.2.3 문자열 연결 - joining : " 
			+ menu.stream().map(Dish::getName).collect(Collectors.joining()));
		
		/*
		 * joining 메서드는 내부적으로 StringBuilder를 이용해서 문자열을 만든다.
		 * 연결된 두 요소 사이에 구분 문자열을 넣을 수 있도록 오버로드된 joining 팩토리 메서드도 있다.
		 */
		System.out.println("6.2.3 문자열 연결 - joining : " 
			+ menu.stream().map(Dish::getName).collect(Collectors.joining(", ")));
		
		/*
		 * 6.2.4 범용 리듀싱 요약 연산
		 * 
		 * 모든 컬렉터는 reducing 팩토리 메서드도 정의할 수 있다.
		 * 즉, 범용 Collectors.reducing으로도 구현할 수 있다.
		 * 이전까지 범용 팩토리 메서드 대신 특화된 컬렉터를 사용한 이유는 프로그래밍적 편의성 때문이다.
		 * 
		 * 아래 코드는 reducing 메서드로 메뉴의 모든 칼로리 합계이다.
		 */
		System.out.println("6.2.4 범용 리듀싱 요약 연산 - 모든 칼로리 합계 : " 
			+ menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j) ));
		
		/*
		 * reducing은 세 개의 인수를 받는다.
		 * 		- 첫 번째 인수는 리듀싱 연산의 시작값이거나 스트림에 인수가 없을 때 반환값.
		 * 		(숫자 합계에서는 인수가 없을 때 반환값으로 0이 적합하다.)
		 * 		- 두 번째 인수는 요리를 칼로리 정수로 변환할 때 사용한 변환 함수이다.
		 * 		- 세 번째 인수는 같은 종류의 두 항목을 하나의 값으로 더하는 BinaryOperator다.
		 * 
		 * 다음 코드 처럼 한 개의 인수를 가진 reducing을 이용해서 칼로리가 가장 높은 요리를 찾는 방법이다.
		 */
		System.out.println("6.2.4 범용 리듀싱 요약 연산 - 칼로리가 가장 높은 요리 : " 
			+ menu.stream().collect(Collectors.reducing((d1, d2) 
				-> d1.getCalories() > d2.getCalories()? d1 : d2)).get() );
		
		/*
		 * 한 개의 인수를 갖는 reducing 팩터리 메서드는 세 개의 인수를 갖는 reducing 메서드에서
		 * 스트림의 첫 번째 요소를 시작 요소, 즉 첫 번째 인수로 받으며 자신을 그대로 반환하는 
		 * 항등함수(identity function)를 두 번째 인수로 받는 상황에 해당한다.
		 * 
		 * 즉, 한 개의 인수를 갖는 reducing 컬렉터는 시작값이 없으므로 빈 스트림이 넘겨졌을 때
		 * 시작값이 설정되지 않는 상황이 생기므로 Optional<Dish> 객체를 반환한다.
		 */

		/*
		 * collect와 reduce
		 * 
		 * 위 코드는 의미론적인 문제와 실용적인 문제 등 두 가지 문제가 발생한다.
		 * collect 메서드는 도출 하려는 결과를 누적하는 컨테이너를 바꾸도록 설계된 메서드인 반면
		 * reduce는 두 값을 하나로 도출하는 불변형 연산이라는 점에서 의미론적인 문제가 일어난다.
		 * 아래 코드에 reduce메서드는 누적자로 사용된 리스트를 변환시키므로 reduce를 잘못 활용한 예이다.
		 * 여러 스레드가 동시에 같은 데이터 구조체를 고치면 리스트 자체가 망가지므로 리듀싱 연산을 병렬로
		 * 수행할 수 없다는 점도 문제다.
		 * 이 문제를 해결하려면 매번 새로운 리스트를 할당해야 하고 따라서 객체를 할당하느라 성능이 저하 될 것이다.
		 * 가변 컨테이너 관련 작업이면서 병렬성을 확보하려면 collect 메서드로 리듀싱 연산을 구현하는 것이 바람직 한다.
		 */
		Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
		System.out.println(stream.reduce(new ArrayList<Integer>()
			, (List<Integer> l, Integer e) -> {
				l.add(e);
				return l;
			}
			,(List<Integer> l1, List<Integer> l2) -> {
				l1.addAll(l2);
				return l1;
			}));
		
		/*
		 * 컬렉션 프레임워크 유연성 : 같은 연산도 다양한 방식으로 수행할 수 있다.!
		 * '6.2.4 범용 리듀싱 요약 연산 - 모든 칼로리 합계' 예제에서 람다 표현식 대신
		 * Integer클래스의 sum 메서드 레퍼런스를 이용하면 코드를 좀 더 단순화 할 수 있다.
		 */
		System.out.println("모든 칼로리 합계(sum 메서드 레퍼런스) : " + menu.stream()
			.collect(Collectors.reducing(0, Dish::getCalories, Integer::sum))); 
		
		System.out.println(menu.stream()
			.collect(
				/*
				 * counting 컬렉터도 세 개의 인수를 갖는 reducing 팩토리 메서드를 이용해서
				 * 구현할 수 있다.
				 */
				counting()
			).longValue() 
		);
		
		/*
		 * 자신의 상황에 맞는 최적의 해법 선택
		 * 
		 * 스트림 인터페이스에서 직접 제공하는 메서드를 이용하는 것보다 컬렉터를 이용하는 코드가
		 * 더 복잡하다는 사실도 보여준다.
		 * 코드가 좀 더 복잡한 대신 재사용성과 커스터마이즈 가능성을 제공하는 높은 수준의 추상화와
		 * 일반화를 얻을 수 있다.
		 * 
		 * 문제를 해결할 수 있는 다양한 해결 방법을 확인한 다음에 가장 일반적으로 문제에 특화된 해결책을
		 * 고르는 것이 바람직하다. 
		 */
	}

	public static <T> Collector<T, ?, Long> counting() {
		/*
		 * 스트림의 Long 객체 형식의 요소를 1로 변환한 다음에 모두 더할 수 있다.
		 */
		return Collectors.reducing(0L, e -> 1L, Long::sum);
	}
}
