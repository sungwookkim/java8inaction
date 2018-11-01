package Part2.Chapter5.Chapter5_5_2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Part2.Chapter5.Chapter5_5_1.entity.Dish;

/*
 * 5.2 매핑
 * 
 * 특정 객체에서 특정 데이터를 선택하는 작업은 데이터 처리 과정에서 자주 수행되는 연산이다.
 * 예를 들어 SQL의 테이블에서 특정 열만 선택할 수 있다.
 * 스트림 API의 map과 flatMap 메서드는 특정 데이터를 선택하는 기능을 제공한다.
 */
public class Main_5_2 {

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
		 * 5.2.1 스트림의 각 요소에 함수 적용하기
		 * 
		 * 스트림은 함수를 인수로 받는 map 메서드를 지원한다.
		 * 인수로 제공된 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑된다.
		 * (기존 값을 "고친다(modify)"라기 보단 "새로운 버전을 만든다"라는 개념에 가까우므로
		 * "변환(transforming)"에 가까운 "매핑(mapping)"이라는 단어를 사용한다.)
		 * 
		 * getName은 문자열을 반환하므로 map 메서드의 출력 스트림은 Stream<String>형식을 갖는다.
		 */
		menu.stream()
			.map(Dish::getName)
			.collect(Collectors.toList())
			.forEach(s -> System.out.println("Dish Name : " + s));

		/*
		 * 단어 리스트가 주어졌을 때 각 단어가 포함하는 글자 수의 리스트를 반환한다 가정해보자.
		 * map을 이용해서 각 요소에 적용할 함수는 단어를 인수로 받아서 길이를 반환한다.
		 */
		Arrays.asList("Java8", "Lambdas", "In", "Action").stream()
			.map(String::length)
			.collect(Collectors.toList())
			.forEach(len -> System.out.println("String Length : " + len));
		
		/*
		 * 아래는 각 요리명의 길이를 구하는 코드이다.
		 */
		menu.stream()
			.map(Dish::getName)
			.map(String::length)
			.collect(Collectors.toList())
			.forEach(len -> System.out.println("Dish Name Length : " + len));
		
		/*
		 * 5.2.2 스트림 평면화
		 * 
		 * 아래는 리스트에 있는 각 단어를 문자로 매핑한 다음에 distinct로 중복된 문자를 필터링 예제이다.
		 * 그러나 중복된 문자 필터링은 되지 않을것이다.
		 * 이유는 map에 전달된 람다(word -> word.split(""))가 각 단어의 String[](문자열 배열)을 
		 * 반환하기 때문이다. 
		 * 따라서 map의 반환 스트림도 Stream<String[]>이다. 그러나 우리가 원하는 스트림 형식은
		 * Stream<String>이여야 한다. 
		 */
		Arrays.asList("Hello", "World").stream()
			// map에서 Stream<String[]> 스트림을 반환한다.  
			.map(word -> word.split(""))
			// 반환된 스트림이 Stream<String[]>이기 때문에 distinct가 되질 않는다.
			.distinct()
			.collect(Collectors.toList())
			.forEach(word -> {
				String str = "";

				for(int i = 0, len = word.length; i < len; i++) {
					str += word[i];
				}

				System.out.println(str);
			});
		
		/*
		 * 위 상황의 해결하기 위해서는 flatMap메서드를 사용하면 된다. 
		 * flatMap은 각 배열을 스트림이 아니라 스트림의 콘텐츠로 매핑한다.
		 * 즉, map(Arrays::stream)과 달리 flatMap은 하나의 평면화된 스트림을 반환한다.
		 * 
		 * flatMap 메서드는 스트림의 각 값을 다른 스트림으로 만든 다음에 모든 스트림을 
		 * 하나의 스트림으로 연결하는 기능을 수행한다.
		 */
		System.out.println(Arrays.asList("Hello", "World").stream()
			// map에서 Stream<String[]> 스트림을 반환한다.
			.map(word -> word.split(""))
			/*
			 * flatMap을 이용해서 하나의 평면화된 스트림을 반환한다.
			 * 즉 Stream<String[]> 스트림이 반환된다.
			 */
			.flatMap(Arrays::stream)
			.distinct()
			.collect(Collectors.toList()));

	}

}
