package Part2.Chapter6.Chapter6_6_4.Quiz;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Part2.Chapter6.Chapter6_6_4.entity.Dish;

/*
 * 퀴즈 6-4
 * partitioningBy 사용
 */
public class Quiz_6_4 {

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
		 * groupingBy 컬렉터와 마찬가지로 partitioningBy 컬렉터도 다른 컬렉터와 조합해서 사용할 수 있다.
		 * 특히 두 개의 partitioningBy 컬렉터를 이용해서 다수준 분할을 수행할 수 있다.
		 * 다음 코드의 다수준 분할 결과를 예측해보자.
		 */
		
		System.out.println(menu.stream()
			.collect(Collectors.partitioningBy(Dish::isVegetarian
				, Collectors.partitioningBy(d -> d.getCalories() > 500)) ));
		
		/*
		 * partitioningBy는 불린을 반환하는 함수, 즉 프레디케이트를 요구하므로 컴파일되지 않는 코드다.
		 * Dish::getType은 프레디케이트로 사용할 수 없는 메서드 레퍼런스다.
		 */
		/*		
		System.out.println(menu.stream()
			.collect(Collectors.partitioningBy(Dish::isVegetarian
				, Collectors.partitioningBy(Dish::getType)) ));
		*/
		
		System.out.println(menu.stream()
			.collect(Collectors.partitioningBy(Dish::isVegetarian
				, Collectors.counting()) ));

	}

}
