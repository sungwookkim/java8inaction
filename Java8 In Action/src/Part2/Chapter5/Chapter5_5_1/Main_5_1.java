package Part2.Chapter5.Chapter5_5_1;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Part2.Chapter5.Chapter5_5_1.entity.Dish;

/*
 * 5.1 필터링과 슬라이싱 
 */
public class Main_5_1 {

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
		 * 5.1.1 프레디케이트로 필터링
		 * 
		 * filter 메서드는 프레디케이트(불린을 반환하는 함수)를 인수로 받아서 프레디케이트와 일치하는 모든 요소를
		 * 포함하는 스트림을 반환한다.
		 */
		menu.stream()
			.filter(Dish::isVegetarian)
			.collect(Collectors.toList())
			.forEach(d -> System.out.println("5.1.1 프레디케이트로 필터링 : 이름 = " + d.getName() + ", 채식주의 = " + d.isVegetarian() ));
				
		/*
		 * 5.1.2 고유 요소 필터링
		 * 
		 * 스트림은 고유 요소로 이루어진 스트림을 반환하는 distinct라는 메서드도 지원한다.
		 * (고유 여부는 스트림에서 만든 객체의 hashCode, equals로 결정)
		 * 아래는 짝수를 선택하고 중복을 필터링하는 코드 이다.
		 */
		List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
		numbers.stream()
			.filter(i -> i % 2 == 0)
			.distinct()
			.forEach((i) -> System.out.println("5.1.2 고유 요소 필터링 : " + i));
			
		/*
		 * 5.1.3 스트림 축소
		 * 
		 * 스트림은 주어진 사이즈 이하의 크기를 갖는 새로운 스트림을 반환하는 limit(n) 메서드를 지원한다.
		 * 아래는 300칼로리 이상의 세 요리를 선택해서 리스트를 가져오는 코드이다.
		 */
		menu.stream()
			.filter(d -> d.getCalories() > 300)
			.limit(3)
			.collect(Collectors.toList())
			.forEach(d -> System.out.println("5.1.3 스트림 축소 : 이름 = " + d.getName() + ", 칼로리 = " + d.getCalories() ));
		/*
		 * 정렬되지 않는 스트림(예를 들면 소스가 Set)에도 limit를 사용할 수 있다. 소스가 정렬되어 있지 않다면
		 * limit의 결과도 정렬되지 않는 상태를 반환한다.
		 */
		
		/*
		 * 5.1.4 요소 건너뛰기
		 * 
		 * 스트림은 처음 n개 요소를 제외한 스트림을 반환하는 skip(n) 메서드를 지원한다.
		 * n개 이하의 요소를 포함하는 스트림에 skip(n)을 호출하면 빈 스트림이 반환된다.
		 * 
		 * 아래 코드는 300칼로리 이상의 처음 두 요리를 건너뛴 다음에 300칼로리가 넘는 나머지 요리를 반환한다.
		 */
		menu.stream()
			.filter(d -> d.getCalories() > 300)
			.skip(2)
			.collect(Collectors.toList())
			.forEach(d -> System.out.println("5.1.4 요소 건너뛰기 : 이름 = " + d.getName() + ", 칼로리 = " + d.getCalories() ));
	}

}
