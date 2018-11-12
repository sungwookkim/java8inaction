package Part2.Chapter6.Chapter6_6_2.Quiz;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Part2.Chapter6.Chapter6_6_2.entity.Dish;

/*
 * 퀴즈 6-1
 * 리듀싱으로 문자열 연결하기. 
 */
public class Quiz_6_1 {

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
		 * 아래 joining 컬렉터를 reducing 컬렉터로 올바르게 바꾼 코드를 모두 선택하시오.
		 */
		String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining());
		System.out.println(shortMenu);

		
		/*
		 * 1
		 */
		System.out.println(menu.stream()
			.map(Dish::getName)
			.collect(Collectors.reducing((s1, s2) -> s1 + s2) )
			.get() );
		
		/*
		 * 2
		 */
/*		System.out.println(menu.stream()			
			.collect(Collectors.reducing((d1, d2) -> d1.getName() + d2.getName()) )
			.get() );*/
		
		/*
		 * 3
		 */
		System.out.println(menu.stream()				
			.collect(Collectors.reducing("", Dish::getName, (s1, s2) -> s1 + s2) ) );
		
		/*
		 * 정답은 1, 3번이다.
		 * 
		 * 1. 원래의 joining 컬렉터처럼 각 요리를 요리명으로 변환한 다음에 문자열을 누적자로 사용해서
		 * 문자열 스트림을 리듀스하면서 요리명을 하나씩 연결한다.
		 * 
		 * 2. reducing은 BinaryOpearator<T>, 즉 BiFunction<T, T, T>를 인수로 받는다.
		 * 즉, reducing은 두 인수를 받아 같은 형식을 반환하는 함수를 인수로 받는다.
		 * 하지만 2번 람다 표현식은 두 개의 요리(Dish 객체)를 인수로 받아 문자열을 반환한다.
		 * 
		 * 3. 빈 문자열을 포함하는 누적자를 이용해서 리듀싱 과정을 시작하며, 스트림의 요리를 방문하면서
		 * 각 요리를 요리명으로 변환한 다음에 누적자로 추가한다.
		 * 세 개의 인수를 갖는 reducing은 누적자 초기값을 설정할 수 있으므로 Optional을 반환할 필요가 없다. 
		 */
		
	}
}
