package Part2.Chapter6.Chapter6_6_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Part2.Chapter6.Chapter6_6_1.entity.Dish;

/*
 * 6.1 컬렉터란 무엇인가?
 */
public class Main_6_1 {

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
		 * 6.1.1 고급 리듀싱 기능을 수행하는 컬렉터
		 * 
		 *  훌륭하게 설계된 함수형 API의 또 다른 장점은 높은 수준의 조합성과 재사용성을 꼽을 수 있다.
		 *  collect로 결과를 수집하는 과정을 간단화하고 유영한 방식으로 정의할 수 있다.
		 *  스트림에 collect를 호출하면 스트림의 요소에(컬렉터로 파라미터화된) 리듀싱 연산이 수행된다.
		 *  
		 *  보통 함수를 요소로 변환 할 때는 컬렉터를 적용하며 최종 결과를 저장하는 자료구조에 값을 누적한다.
		 *  (toList 처럼 데이터 자체를 변환하는 것보다는 데이터 저장 구조를 변환할 때가 많다.)
		 *  
		 *  아래 코드는 Dish.Type 기준으로 음식명을 그룹화 한 코드이다.
		 */
		Map<Dish.Type, List<Dish>> dishResult = new HashMap<>();
		
		for(Dish dish : menu) {
			Dish.Type type = dish.getType();
			
			List<Dish> dishType = dishResult.get(type);
			
			if(dishType == null) {
				dishType = new ArrayList<>();
				dishResult.put(type, dishType);
			}
			
			dishType.add(dish);
		}
		
		System.out.println("명령형 버전 : ");
		System.out.println(dishResult);
		
		System.out.println("스트림 버전 : ");
		System.out.println(menu.stream().collect(Collectors.groupingBy(Dish::getType)));
		
		/*
		 * 6.1.2 미리 정의된 컬렉터
		 * 
		 * groupingBy 같이 Collectors 클래스에서 제공하는 팩토리 메서드의 기능을 설명한다.
		 * Collectors에서 제공하는 메서드의 기능은 크게 세 가지로 구분할 수 있다. 
		 * 		- 스트림 요소를 하나의 값으로 리듀스하고 요약
		 * 		- 요소 그룹화
		 * 		- 요소 분할 		 
		 */
		
	}

}
