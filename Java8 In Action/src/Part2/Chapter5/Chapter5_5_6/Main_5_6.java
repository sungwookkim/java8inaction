package Part2.Chapter5.Chapter5_5_6;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import Part2.Chapter5.Chapter5_5_4.entity.Dish;

/*
 * 5.6 숫자형 스트림
 * 
 * int calories = menu.stream()
 * 		.map(Dish::getCalories)
 * 		.reduce(0, Integer::sum);
 * 
 * 위 코드는 메뉴의 칼로리 합계를 구하는 코드이다.
 * 그러나 사실 위 코드에는 박싱 비용이 숨어있다.
 * 내부적으로 합계를 계산하기 전에 Integer를 기본형으로 언박싱해야 한다.
 * 
 * 숫자 스트림을 효율적으로 처리할 수 있게 기본형 특화 스트림(primitive stream specialzation)을 제공한다. 
 */
public class Main_5_6 {

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
		 * 5.6.1 기본형 특화 스트림
		 * 
		 * 자바 8에서는 3가지 기본형 특화 스트림을 제공한다.
		 * 박싱 비용을 피할 수 있도록 'int에 특화된 IntStream', 'double에 특화된 DoubleStream', 'long 요소에 특화된 LongStream'
		 * 을 제공한다. 
		 * 각각의 인터페이스는 숫자 스트림의 합계를 계산할 수 있는 sum, 최대값 요소를 검색하는 max 같이 자주 사용하는 숫자 관련
		 * 리듀싱 연산 수행 메서드를 제공한다.
		 * 또한 필요할 때 다시 객체 스트림으로 복원하는 기능도 제공한다.
		 * 특화 스트림은 오직 박싱과정에서 일어나는 효율성과 관련 있으며 스트림에 추가 기능을 제공하진 않는다는 사실을 기억하자.		  
		 */
		
		/*
		 * mapToInt 메서드는 각 요리에서 모든 칼로리(Integer 형식)를 추출한 다음에 IntStream을 반환한다.
		 * (Stream<Integer>가 아님)
		 * 따라서 IntStream 인터페이스에서 제공하는 sum 메서드를 사용할 수 있다.
		 * sum 메서드는 스트림이 비어있으면 기본값 0을 반환한다.
		 * IntStream은 max, min, average 등 다양한 유틸리티 메서드를 지원한다.
		 */
		System.out.println("5.6.1 기본형 특화 스트림 - mapToInt : " + menu.stream()
			.mapToInt(Dish::getCalories)
			.sum());
		
		/*
		 * 객체 스트림으로 복원하기
		 * 
		 * boxed 메서드를 이용하면 특화 스트림을 일반 스트림으로 변환할 수 있다.
		 */
		// 스트림을 숫자 스트림으로 변환.
		IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
		System.out.println("[IntStream] 객체 스트림으로 복원하기 : " + intStream);
		
		// 숫자 스트림에서 boxed 메서드를 이용해서 스트림으로 변환.
		Stream<Integer> streamInteger = menu.stream().mapToInt(Dish::getCalories).boxed();
		System.out.println("[Stream<Integer>] 객체 스트림으로 복원하기 : " + streamInteger);
		
	}

}
