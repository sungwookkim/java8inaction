package Part1.Chapter3.Chapter3_3_7;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import Part1.Chapter3.Chapter3_3_7.entity.AppleEntity;
import Part1.Chapter3.Chapter3_3_7.impl.AppleEntityComparator;

/*
 * 3.7 람다, 메서드, 레퍼런스 활용하기!
 */
public class Main_3_7 {

	public static void main(String[] args) {
		List<AppleEntity> inventory = Arrays.asList(new AppleEntity("red", 300)
				, new AppleEntity("green", 120)
				, new AppleEntity("blue", 100));
		
		/*
		 * 3.7.1 1단계 : 코드 전달
		 * 자바 8의 List API에서 제공하는 sort메서드에 정렬 전략을 전달. 
		 */
		inventory.sort(new AppleEntityComparator());
		inventory.stream().forEach((AppleEntity a) -> System.out.println("3.7.1 코드 전달 : 색깔 = " 
				+ a.getColor() + ", 무게 = " + a.getWeight() ));
		
		/*
		 * 3.7.2 2단계 : 익명 클래스 사용
		 */
		inventory.sort(new Comparator<AppleEntity>() {
			@Override
			public int compare(AppleEntity o1, AppleEntity o2) {			
				return o2.getWeight().compareTo(o1.getWeight());
			}
		});
		inventory.stream().forEach((AppleEntity a) -> System.out.println("3.7.2 익명 클래스 사용 : 색깔 = " 
				+ a.getColor() + ", 무게 = " + a.getWeight() ));
		
		/*
		 * 3.7.3 3단계 : 람다 표현식 사용
		 * 
		 * 람다 표현식으로 경량화된 문법을 이용해서 코드를 전달.
		 * 함수형 인터페이스를 기대하는 곳 어디에서나 람다 표현식을 이용할 수 있다.
		 * 함수형 인터페이스란 오직 하나의 추상 메서드를 정의하는 인터페이스이다.
		 * Comparator의 함수 디스크립터는 (T, T) -> int다.
		 * 우리 같은 경우에는(AppleEntity, AppleEntity) -> int로 표현할 수 있다. 
		 */
		/*
		 *  람다 표현식이 사용된 콘텍스트를 활용해서 람다의 파라미터 형식을 추론할 수 있다.
		 */
		inventory.sort((o1, o2) -> o1.getWeight().compareTo(o2.getWeight() ));
		inventory.stream().forEach((AppleEntity a) -> System.out.println("3.7.3 람다 표현식 사용 : 색깔 = " 
				+ a.getColor() + ", 무게 = " + a.getWeight() ));

		inventory.sort(Comparator.comparing((AppleEntity a) -> a.getWeight() ));
		inventory.stream().forEach((AppleEntity a) -> System.out.println("3.7.3 람다 표현식 사용(Comparator.comparing) : 색깔 = " 
				+ a.getColor() + ", 무게 = " + a.getWeight() ));
		
		/*
		 * 3.7.4 4단계 : 메서드 레퍼런스 사용 
		 */
		inventory.sort(Comparator.comparing(AppleEntity::getWeight));
		inventory.stream().forEach((AppleEntity a) -> System.out.println("3.7.4 메서드 레퍼런스 사용 : 색깔 = " 
				+ a.getColor() + ", 무게 = " + a.getWeight() ));
	}

}
