package Part2.Chapter5.Chapter5_5_3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import Part2.Chapter5.Chapter5_5_1.entity.Dish;

/*
 * 5.3 검색과 매칭
 * 
 * 특정 속성이 데이터 집하에 있는지 여부를 검색하는 데이터 처리도 자주 사용된다.
 * 스트림 API는 allMatch, anyMatch, noneMatch, findFirst, findAny 등 다양한 유틸리티 메서드를
 * 제공한다.
 */
public class Main_5_3 {

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
		 * 5.3.1 프레디케이트가 적어도 한 요소와 일치하는지 확인
		 * 
		 * 프레디케이트가 주어진 스트림에서 적어도 한 요소와 일치하는지 확인할 때
		 * anyMatch 메서드를 이용한다.
		 * 다음 코드는 menu에 채식요리가 있는지 확인하는 예제이다. 
		 */
		if(menu.stream().anyMatch(Dish::isVegetarian)) {
			System.out.println("The menu is (somewhat) vegetarian friendly!!");
		}
		
		/*
		 * 5.3.2 프레디케이트가 모든 요소와 일치하는지 검사
		 * 
		 * allMatch 메서드는 anyMatch와 달리 스트림의 모든 요소가 주어진 프레디케이트와 일치하는지
		 * 검사한다.
		 * 다음 코드는 건강식(모든 요리가 1000칼로리 이하면 건강식으로 간주)인지 확인하는 코드다.
		 */
		if(menu.stream().allMatch(d -> d.getCalories() < 1000)) {
			System.out.println("모든 식단이 건강식단 입니다!");
		}
		
		/*
		 * noneMatch
		 * noneMatch는 allMatch와 반대 연산을 수행한다.
		 * 즉, noneMatch는 주어진 프레디케이트와 일치하는 요소가 없는지 확인한다. 
		 */
		if(menu.stream().noneMatch(d -> d.getCalories() >= 1000)) {
			System.out.println("건강 식단이라서 먹을게 없네요.");
		}		
		/*
		 * anyMatch, allMatch, noneMatch 세 가지 메서드는 쇼트서킷 기법.
		 * 즉, 자바의 &&, ||와 같은 연산을 활용한다.
		 */
		
		/*
		 * 쇼트서킷 평가
		 * 때로는 전체 스트림을 처리하지 않았더라도 결과를 반환할 수 있다.
		 * 예를 들어 여러 and 연산으로 연결된 커다란 불린 표현식을 평가한다고 가정하자.
		 * 표현식에서 하나라도 거짓이라는 결과가 나오면 나머지 표현식의 결과와 상관없이
		 * 전체 결과도 거짓이 된다.
		 * 이러한 상황을 "쇼트서킷"이라고 한다.
		 * 
		 * allMatch, noneMatch, findFirst, findAny 등의 연산은 모든 스트림의 요소를 처리하지
		 * 않고도 결과를 반환할 수 있다. 
		 * 이는 원하는 요소를 찾으면 즉시 결과를 반환할 수 있기 때문이다.
		 * 마찬가지로 스트림의 모든 요소를 처리할 필요 없이 주어진 크기의 스트림을 생성하는
		 * limit도 쇼트서킷 연산이다.
		 */
		
		/*
		 * 5.3.3 요소 검색
		 * 
		 * findAny 메서드는 현재 스트림에서 임의의 요소를 반환한다.
		 * findAny 메서드를 다른 스트림 연산과 연결해서 사용할 수 있다.
		 * 다음 코드는 filter와 findAny를 이용해서 채식요리를 선택한다.
		 * 
		 * 스트림 파이프라인은 내부적으로 단일 과정으로 실행할 수 있도록 최적화된다.
		 * 즉, 쇼트서킷을 이용해서 결과를 찾는 즉시 실행을 종료한다.
		 */
		Optional<Dish> dish = menu.stream()
			.filter(Dish::isVegetarian)
			.findAny();

		/*
		 * Optional이란?
		 * Optional<T>클래스(java.util.Optional)는 값의 존재나 부재 여부를 표현하는 컨테이너 클래스다.
		 * 이전 예제에서 findAny 메서드는 아무 요소도 반환하지 않을 수 있다.
		 * null은 쉽게 에러를 일으킬 수 있으므로 자바 8 라이브러리 설계자는 Optional<T>라는 기능을 만들었다.
		 * 		- isPresend()는 Optional이 값을 포함하면 참(true)을 반환하고, 값을 포함하지 않으면
		 * 		거짓(false)을 반환한다.
		 * 
		 *  	- ifPresend(Consumer<T> block)은 값이 있으면 주어진 블럭을 실행한다.
		 *  	Consumer 함수형 인터페이스에넌 T형식의 인수를 받으며 void를 반환하는 람다를 전달할 수 있다.
		 *  
		 *  	- T get()은 값이 존재하면 값을 반환하고, 값이 없으면 NoSuchElementException을 일으킨다.
		 *  
		 *  	- T orElse(T other)는 값이 있으면 값을 반환하고, 값이 없으면 기본값을 반환한다. 	
		 */
		
		// 아래 코드 Optional<Dish>에서는 요리명이 null인지 검사할 필요가 없다.
		dish.ifPresent(d -> System.out.println("채식 요리를 선택한 사람 : " + d.getName() ));
		
		/*
		 * 5.3.4 첫 번째 요소 찾기
		 * 
		 * 리스트 또는 정렬된 데이터로부터 생성된 스트림은 논리적인 아이템 순서가 정해져 있을 수 있다.
		 * 이런 스트림에서 첫 번째 요소를 찾으려면 어떻게 해야 할까?
		 * 아래 코드는 숫자 리스트에서 3으로 나누어떨어지는 첫 번째 제곱값을 구하는 예이다.
		 */
		System.out.println("5.3.4 첫 번째 요소 찾기 : " + Arrays.asList(1, 2, 3, 4, 5).stream()
				.map(x -> x * x)
				.filter(x -> x % 3 == 0)
				.findFirst()
				.get() );
		
		/*
		 * findfirst와 findAny는 언제 사용하나?
		 * 두 가지 메서드가 필요한 이유는 바로 병렬성 때문이다.
		 * 병렬실행에서 첫 번째 요소를 찾기 어렵다. 따라서 요소의 반환 순서가 상관없다면
		 * 병렬 스트림에서는 제약이 적은 findAny를 사용한다.
		 */
	}
}
