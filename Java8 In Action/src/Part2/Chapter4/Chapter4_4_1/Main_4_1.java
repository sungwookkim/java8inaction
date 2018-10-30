package Part2.Chapter4.Chapter4_4_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import Part2.Chapter4.Chapter4_4_1.entity.Dish;

/*
 * 4.1 스트림이란 무엇인가?
 * 
 * 스트림은 자바 API에 새로 추가된 기능으로, 스트림을 이용하면 선언형(즉, 데이터를 처리하는 임시 구현 코드 대신 질의로 표현할 수 있다.)
 * 으로 컬렉션 데이터를 처리할 수 있다. 
 * 일단 스트림이 데이터 컬렉션 반복을 멋지게 처리하는 기능이라고 생각하자. 또한 스트림을 이용하면 멀티 스레드 코드를 구현하지 않아도
 * 데이터를 투명하게 병렬처리할 수 있다. 
 */
public class Main_4_1 {

	static public void main(String[] args) {
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
		 * 스트림이 어떤 유용한 기능을 제공하는지 알기 위해
		 * "저칼로리의 요리명을 반환하고, 칼로리를 기준으로 요리를 정렬"하는 코드를
		 * 자바 7과 자바 8로 나눠서 구현해보겠다.
		 */
		
		/*
		 * 자바 7인 경우.
		 */
		// 저칼로리(400 미만)를 구하는 구현코드.
		List<Dish> lowCaloriDishes = new ArrayList<>();
		for(Dish d : menu) {
			if(d.getCalories() < 400) {
				lowCaloriDishes.add(d);
			}
		}
		
		// 추출된 저칼로리를 칼로리 기준으로 정렬하는 구현코드.
		Collections.sort(lowCaloriDishes, new Comparator<Dish>() {
			@Override
			public int compare(Dish o1, Dish o2) {
				return Integer.compare(o1.getCalories(), o2.getCalories());
			}
		});
		
		// 정렬된 리스트를 처리하면서 요리명 선택.
		List<String> lowCaloriDishesName = new ArrayList<>();
		for(Dish d : lowCaloriDishes) {
			lowCaloriDishesName.add(d.getName());
		}
		
		/*
		 * 위 코드에서는 lowCaloriDishes라는 '가비지 변수'가 사용되었다.
		 * 즉, lowCaloriDishes는 컨테이너 역할만하는 중간 변수다.
		 */
		lowCaloriDishesName.stream().forEach((String name) -> System.out.println("Java7 Dish Name : " + name));
		
		/*
		 * 자바 8인 경우.
		 */
		lowCaloriDishesName = menu 
			.stream()
			// 위 stream 메서드 대신 아래 parallelStream 메서드를 사용하게 되면 멀티코어 아키텍처에서 병렬로 실행할 수 있다.
			//.parallelStream()
			// 400 칼로리 이하의 요리 선택.
			.filter((Dish d) -> d.getCalories() < 400)
			// 칼로리로 요리 정렬.
			.sorted((d1, d2) -> Integer.compare(d1.getCalories(), d2.getCalories()))
			// 요리명 추출.
			.map(Dish::getName)
			// 모든 요리명을 리스트에 저장.
			.collect(Collectors.toList());

		lowCaloriDishesName.stream().forEach((String name) -> System.out.println("Java8 Dish Name : " + name));
		
		/*
		 * 스트림의 새로운 기능이 소프트웨어공학적으로 다양한 이득을 준다.
		 * 		1. 선언형으로 코드를 구현할 수 있다.
		 * 		루프와 if 조건문 등의 제어 블록을 사용해서 어떻게 동작을 구현할지 지정할 필요가 없다.
		 * 		선언형 코드와 동작 파라미터화를 활용하면 변하는 요구사항에 쉽게 대응할 수 있다.
		 * 		즉, 기존 코드를 복사하여 붙여 넣는 방식을 사용하지 않는다.
		 * 
		 * 		2. filter, sorted, map, collect 같은 여러 빌딩 블록 연산을 연결해서 복잡한 데이터 처리 파이프라인을 만들 수 있다.
		 * 		여러 연산을 파이프라인으로 연결해도 여전히 가독성과 명확성이 유지된다.
		 * 		filter 메서드의 결과는 sorted 메서드로, 다시 sorted의 결과는 map 메서드로, map 메서드의 결과는 collect로 연결된다.
		 * 
		 * filter(또는 sorted, map, collect) 같은 연산은 고수준 빌딩 블록(high-level building block)으로 이루어져 있으므로
		 * 특정 스레딩 모델에 제한되지 않고 자유롭게 어떤 상황에서든 사용할 수 있다.
		 * (또한 이들은 내부적으로 멀티코어 아키텍처를 최대한 투명하게 활용할 수 있도록 구현되어 있다.)
		 * 결과적으로 우리는 데이터 처리 과정을 병렬화하면서 스레드와 락을 걱정할 필요가 없다.
		 * 
		 * 자바 8의 스트림 API의 특징을 다음처럼 요약할 수 있다.
		 * - 선언형
		 * 		더 간결하고 가독성이 좋아진다.
		 * - 조립할 수 있음
		 * 		유연성이 좋아진다.
		 * - 병렬화
		 * 		성능이 좋아진다.
		 */		
	}
}
