package Part1.Chapter2.Chapter2_2_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Part1.Chapter2.Chapter2_2_3.entity.AppleEntity;
import Part1.Chapter2.Chapter2_2_3.filters.inter.ApplePredicate;
import Part1.Chapter2.Chapter2_2_3.filters.inter.Predicate;

/*
 * 2.3.1 익명 클래스
 * 
 * 익명 클래스는 자바의 지역 클래스(local class)와 비슷한 개념이다.
 * 말그대로 이름이 없는 클래스다.
 * 익명 클래스를 이용하면 클래스 선언과 인스턴스화를 동시에 할 수 있다.
 * 즉, 즉석에서 필요한 구현을 만들어서 사용할 수 있다.
 */
public class Main_2_3 {

	public static List<AppleEntity> filterApples(List<AppleEntity> inventory, ApplePredicate p) {
		List<AppleEntity> result = new ArrayList<>();
		
		for(AppleEntity apple : inventory) {
			if(p.test(apple)) {
				result.add(apple);
			}
		}
		
		return result;
	}
	
	/*
	 * 이게 진짜 끝판왕이다. 자세한 설명은 생략..이 아니라 main 메서드를 참고하도록 한다.
	 */
	public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> result = new ArrayList<>();
		
		for(T e : list) {
			if(p.test(e)) {
				result.add(e);
			}
		}
		
		return result;
	}

	public static void main(String[] args) {
		List<AppleEntity> inventory = Arrays.asList(new AppleEntity("green", 100)
				, new AppleEntity("red", 120)
				, new AppleEntity("green", 150));
		
		/*
		 * 2.3.2 다섯 번째 시도 : 익명 클래스 사용(이 정도까지 했으면 그냥 내가 사장 하는게 나을듯 싶다...)
		 * 
		 * 익명 클래스로도 아직 부족한 점이 있다.(저자는 언제쯤 만족할건지 궁금하다..)
		 * 1. 객체 생성 구문과 메서드 작성 부분 때문에 코드가 많은 공간을 차지한다.
		 * 2. 많은 프로그래머가 익명 클래스의 사용에 익숙하지 않다.
		 * 
		 * 코드의 장황함(verbosity)은 나쁜 특성이다. 
		 * 장황한 코드는 구현하고 유지보수하는데 시간이 오래 걸린다.
		 * 한눈에 이해할 수 있는 코드가 좋은 코드다.(당연한 말인거 같은데..)
		 */
		filterApples(inventory, new ApplePredicate() { // ApplePredicate는 인터페이스이다.
			@Override
			public boolean test(AppleEntity appleEntity) {
				return "red".equals(appleEntity.getColor());
			}
		})
		.stream()
		.forEach((AppleEntity apple) -> System.out.println("[Anonymous Class] apple Color : " + apple.getColor()
			+ ", apple weight : " + apple.getWeight() ));

		/*
		 * 2.3.3 여섯 번째 시도 : 람다 표현식 사용
		 * 
		 * 람다 표현식을 사용하면 아래와 같이 간단(?)하게 재구현 할 수 있다.
		 */
		filterApples(inventory, (AppleEntity apple) -> "red".equals(apple.getColor()) )
			.stream()
			.forEach((AppleEntity apple) -> System.out.println("[Lambda] apple Color : " + apple.getColor()
				+ ", apple weight : " + apple.getWeight() ));
		
		/*
		 * 2.3.4 일곱 번째 시도 : 리스트 형식으로 추상화(여기까지 온거면 볼 장 다 본거 같다..)
		 * 
		 * 현재 filterApples는 Apple과 관련된 동작만 수행한다.
		 * 하지만 Apple 이외의 다양한 물건에서 필터링이 작동하도록 리스트 형식을 추상화할 수 있다.
		 * (왠지 느낌이 이번이 진짜 끝판왕 같다.. 레알루다가..)
		 */
		filter(inventory, (AppleEntity apple) -> "red".equals(apple.getColor()) )
			.stream()
			.forEach((AppleEntity apple) -> System.out.println("[Last Boss] apple Color : " + apple.getColor()
				+ ", apple weight : " + apple.getWeight() ));

		filter(Arrays.asList(0, 1, 2, 3, 4, 5), (Integer i) -> i % 2 == 0 )
			.stream()
			.forEach((Integer i) -> System.out.println("[Last Boss] even number : " + i));
	}

}
