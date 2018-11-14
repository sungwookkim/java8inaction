package Part2.Chapter6.Chapter6_6_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import Part2.Chapter6.Chapter6_6_5.entity.Dish;

/*
 * 6.5 Collector 인터페이스
 * 
 * Collector 인터페이스는 리듀싱 연산(즉, 컬렉터)을 어떻게 구현할지 제공하는 메서드 집합으로 구성된다.
 * 
 * Collector 인터페이스
 * public interface Collector<T, A, R> {
 * 		Supplier<A> supplier();
 * 		BiConsumer<A, T> accumulator();
 * 		Function<A, R> finisher();
 * 		BinarayOperator<A> cambiner();
 * 		Set<Characteristics> characteristics();
 * }
 * 
 * 위 코드는 다음처럼 설명할 수 있다.
 * 		- T는 수집될 스트림 항목의 제네릭 형식이다.
 * 		- A는 누적자, 즉 수집 과정에서 중간 결과를 누적하는 객체의 형식이다.
 * 		- R은 수집 연산 결과 객체의 형식이다.
 * 		(항상 그런 것은 아니지만 대개 컬렉터 형식)
 */
public class Main_6_5 {

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
		
		System.out.println("Collectors.toList : " + menu.stream().collect(Collectors.toList()) );
		System.out.println("ToListCollector : " + menu.stream().collect(new ToListCollector<Dish>()) );
		
		/*
		 * 컬렉터 구현을 만들지 않고도 커스텀 수집 수행하기
		 * 
		 * IDENTITY_FINISH 수집 연산에서는 Collector 인터페이스를 완전히 새로 구현하지 않고도 같은 결과를 얻을 수 있다.
		 * Stream은 세 함수(supplier, accumulator, combiner)를 인수로 받는 collect 메서드를 오버로드하며 각각의 메서드는
		 * Collector 인터페이스의 메서드가 반환하는 함수와 같은 기능을 한다.
		 * 
		 * 아래 코드가 좀 더 간결하고 축약되어 있지만 가독성은 떨어진다.
		 * 적절한 클래스로 커스텀 컬렉터를 구현하는 편이 중복을 피하고 재사용성을 높이는데 도움이 된다. 
		 * 또한 Characteristics를 전달할 수 없다. 즉, 두 번째 collect 메서드는 IDENTITY_FINISH와 CONCURRENT지만 
		 * UNORDERED는 아닌 컬렉터로만 동작한다.
		 */
		System.out.println("인수 사용 : " + menu.stream().collect(ArrayList::new, List::add, List::addAll));
	}

}

/*
 * 6.5.1 Collector 인터페이스의 메서드 살펴보기
 * 
 * 네 개의 메서드는 collect 메서드에서 실행하는 함수를 반환하는 반면, 다섯 번째 메서드 characteristics는
 * collect 메서드가 어떤 최적화(병렬화 같은)를 이용해서 리듀싱 연산을 수행할 것인지 결정하도록 돕는
 * 힌트 특성 집합을 제공한다.
 * 
 * 실제로 collect가 동작하기 전에 다른 중간 연산과 파이프라인을 구성할 수 있게 해주는 게으른 특성 그리고 
 * 병렬실행 등도 고려해야 하므로 스트림 리듀싱 기능 구현은 생각보다 복잡하다.
 */
class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

	/*
	 * supplier 메서드 : 새로운 결과 컨테이너 만들기
	 */
	@Override
	public Supplier<List<T>> supplier() {
		/*
		 * supplier 메서드는 빈 결과로 이루어진 Supplier를 반환해야 한다.
		 * 즉, supplier는 수집 과정에서 빈 누적자 인스턴스를 만드는 파라미터가 없는 함수다.
		 * 
		 * 해당 클래스처럼 누적자를 반환하는 컬렉터에서는 빈 누적자가 비어있는 스트림의 수집 과정의 결과가 될 수 있다.
		 */
		return () -> new ArrayList<T>();
		
		/*
		 * 아래와 같이 하면 더 간결해진다.
		 */
		// return ArrayList::new;
	}
	
	/*
	 * accumulator 메서드 : 결과 컨테이너에 요소 추가하기 
	 */
	@Override
	public BiConsumer<List<T>, T> accumulator() {
		/*
		 * accumulator 메서드는 리듀싱 연산을 수행하는 함수를 반환한다.
		 * 스트림에서 n번째 요소를 탐색할 때 두 인수, 즉 누적자(스트림의 첫 n-1개 항목을 수집한 상태)와 n번째 요소를 함수에 적용한다.
		 * 함수의 반환값은 void, 즉 요소를 탐색하면서 적용하는 함수에 의해 누적자 내부 상태가 바뀌므로 누적자가 어떤 값일지 단정할 수 없다.
		 * 
		 * 해당 클래스에서 accumulator가 반환하는 함수는 이미 탐색한 항목을 포함하는 리스트에 현재 항목을 추가하는 연산을 수행한다.
		 */
		return (List<T> list, T item) -> list.add(item);
		
		/*
		 * 아래와 같이 하면 더 간결해진다.
		 */
		// return List::add; 
	}

	/*
	 * finisher 메서드 : 최종 변환값을 결과 컨테이너로 적용하기
	 */
	@Override
	public Function<List<T>, List<T>> finisher() {
		/*
		 * finisher 메서드는 스트림 탐색을 끝내고 누적자 객체를 최종 결과로 변환하면서 누적 과정을 끌낼 때 호출할 함수를 반환해야 한다.
		 * 때로는 해당 클래스처럼 누적자 객체가 이미 최종 결과인 상황도 있다.
		 * 
		 * 이런 때는 변환 과정이 필요하지 않으므로 finisher 메서드는 항등 함수를 반환한다.
		 * 
		 * supplier, accumulator, finisher의 세 가지 메서드로도 순차적 스트림 리듀싱 기능을 수행할 수 있다. 
		 */
		return (list) -> list;
		
		/*
		 * 아래와 같이 하면 더 간결해진다.
		 */
		//return Function.identity();
	}

	/*
	 * combiner 메서드 : 두 결과 컨테이너 병합 
	 */
	@Override
	public BinaryOperator<List<T>> combiner() {
		/*
		 * combiner는 스트림의 서로 다른 서브파트를 병렬로 처리할 때 누적자가 이 결과를 어떻게 처리할지 정의한다.
		 * toList의 combiner는 스트림의 두 번째 서브파트에서 수집한 항목 리스트를 첫 번째 서브파트 결과 리스트 뒤에 추가하면 되기 때문에
		 * 비교적 쉽게 구현할 수 있다.
		 */
		return (list1, list2) -> {
			list1.addAll(list2);
			
			return list1;
		};
	}
	
	/*
	 * Characteristics 메서드는 컬렉터의 연산을 정의하는 Characteristics 형식의 불변 집합을 반환한다.
	 */
	@Override
	public Set<Characteristics> characteristics() {
		/*
		 * Characteristics는 스트림을 병렬로 리듀스할 것인지 그리고 병렬로 리듀스한다면 어떤 최적화를 선택해야 할지 힌트를 제공한다.
		 * 
		 * UNORDERED
		 * 리듀싱 결과는 스트림 요소의 방문 순서나 누적 순서에 영향을 받지 않는다.
		 * 
		 * CONCURRENT
		 * 다중 스레드에서 accumulator 함수를 동시에 호출할 수 있으며 이 컬렉터는 스트림의 병렬 리듀싱을 수행할 수 있다.
		 * 컬렉터의 플래그에 UNORDERED를 함께 설정하지 않았다면 데이터 소스가 정렬되어 있지 않은(집합처럼 요소의 순서에 무의미한)상황에서만 
		 * 병렬 리듀싱을 수행할 수 있다.
		 * 
		 * IDENTITY_FINISH
		 * finisher 메서드가 반환하는 함수는 단순히 identity를 적용할 뿐이므로 이를 생략할 수 있다.
		 * 따라서 리듀싱 과정의 최종 결과로 누적자 객체를 바로 사용할 수 있다.
		 * 또한 누적자 A를 결과 R로 안전하게 형변환할 수 있다.
		 * 
		 * 해당 클래스는 스트림의 요소를 누적하는데 사용한 리스트가 최종 결과 형식이므로 추가 변환이 필요없다.
		 * 그러므로 IDENTITY_FINISH다. 하지만 리스트의 순서는 상관이 없으므로 UNORDERED다. 그리고 마지막으로 CONCURRENT다.
		 * 하지만 이미 설명했듯이 요소의 순서가 무의미한 데이터 소스여야 병렬로 실행할 수 있다.
		 */
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT));
	}


	
}
