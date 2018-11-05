package Part2.Chapter5.Chapter5_5_4.Quiz;

import java.util.Arrays;
import java.util.List;

import Part2.Chapter5.Chapter5_5_4.entity.Dish;

public class Quiz_5_3 {

	static public void main(String[] args) {
		/*
		 * 퀴즈 5-3 리듀스
		 *  map과 reduce 메서드를 이용해서 스트림의 요리 개수를 계산하시오.
		 */
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
		 * 스트림의 각 요소를 1로 매핑한 다음에 reduce로 이들의 합계를 계산하는 방식.
		 * map과 reduce를 연결하는 기법을 맵 리듀스(map-reduce)패턴이라 하며,
		 * 쉽게 병렬화하는 특징 덕분에 구글이 웹 검색에 적용하면서 유명해졌다.
		 */
		System.out.println("맵-리듀스 패턴 : " + menu.stream()
			.map(d -> 1)
			.reduce(0, (a, b) -> a + b));
		
		System.out.println("count 메서드 : " + menu.stream().count());
			
	}
}
