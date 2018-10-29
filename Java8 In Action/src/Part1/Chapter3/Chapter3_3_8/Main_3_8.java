package Part1.Chapter3.Chapter3_3_8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import Part1.Chapter3.Chapter3_3_8.entity.AppleEntity;
import Part1.Chapter3.Chapter3_3_8.letter.Letter;

/*
 * 3.8 람다 표현식을 조합할 수 있는 유용한 메서드
 * 
 * 자바 8 API의 몇몇 함수형 인터페이스는 다양한 유틸리티 메서드를 포함한다.
 * 간단한 여러 개의 람다 표현식을 조합해서 복잡한 람다 표현식을 만들 수 있다.
 * 예를 들어 두 프레디케이트를 조합해서 두 프레디케이트의 or 연산을 수행하는 커다란 프레디케이트를 만들수 있다.
 * 또한 한 함수의 결과가 다른 함수의 입력이 되도록 두 함수를 조합할 수도 있다.
 * 이와 같이 가능한 이유는 함수형 인터페이스의 디폴트 메서드(default method) 때문이다.
 * (디폴트 메서드는 추상 메서드가 아니므로 함수형 인터페이스 정의에 어긋나지 않는다.) 
 */
public class Main_3_8 {

	public static void main(String[] args) {
		List<AppleEntity> inventory = Arrays.asList(new AppleEntity("green", 140)
				, new AppleEntity("red", 200)
				, new AppleEntity("green", 200)
				, new AppleEntity("blue", 300)
				, new AppleEntity("red", 100));
		
		/*
		 * 3.8.1 Comparator 조합
		 * 
		 * 정적 메서드 Comparator.comparing을 이용해서 비교에 사용할 키를 추출하는 Function 기반의 Comparator를
		 * 반환할 수 있다. 
		 * 		Comparator<AppleEntity> c = Comparator.comparing(AppleEntity::getWeight);
		 */
		/* 
		 * 역정렬
		 * 사과의 무게를 내림차순으로 정렬하고 싶다면? 다른 Comparator 인스턴스를 만들 필요가 없다.
		 * 인터페이스 자체에 주어진 비교자의 순서를 뒤바꾸는 reverse라는 디폴트 메서드를 제공하기 떄문이다.
		 * 아래 코드와 같이 처음 비교자 구현을 그대로 재사용해서 사과의 무게를 기준으로 역정렬할 수 있다.
		 */
		inventory.sort(Comparator.comparing(AppleEntity::getWeight).reversed());
		inventory.stream().forEach((appleEntity) -> System.out.println("3.8.1 Comparator 조합(reversed 메서드 사용) color : " 
				+ appleEntity.getColor() + ", weight : " + appleEntity.getWeight() ));
		
		/*
		 * Comparator 연결
		 * 무게가 같다면 색깔로 사과를 정렬할 수 있다.
		 * thenComparing 메서드로 두 번재 비교자를 만들수 있다.
		 * thenComparing은 함수를 인수로 받아 첫 번째 비교자를 이용해서 두 객체가 같다고 판단되면
		 * 두 번째 비교자에 객체를 전달한다.
		 */
		inventory.sort(Comparator.comparing(AppleEntity::getWeight).reversed()
			.thenComparing(Comparator.comparing(AppleEntity::getColor)) );
		inventory.stream().forEach((appleEntity) -> System.out.println("3.8.1 Comparator 조합(thenComparing 메서드 사용) color : " 
				+ appleEntity.getColor() + ", weight : " + appleEntity.getWeight() ));
		
		/*
		 * 3.8.2 Predicate 조합
		 * 
		 * Predicate 인터페이스는 복잡한 프레디케이트를 만들수 있도록 negate, and, or 세 가지 메서드를 제공한다. 
		 */
		inventory.stream().forEach((AppleEntity a) -> {
			Predicate<AppleEntity> redApple = (AppleEntity a1) -> "red".equals(a1.getColor());
			Predicate<AppleEntity> gram150Up = (AppleEntity a1) -> a1.getWeight() > 150;

			/*
			 * 예를 들어 "빨간색이 아닌 사과"처럼 특정 프레디케이트를 반전시킬 때 negate메서드를 사용할 수 있다.
			 */
			Predicate<AppleEntity> notRedApple = redApple.negate();

			if(notRedApple.test(a)) {
				System.out.println("3.8.2 Predicate 조합(빨간색이 아닌 사과 - negate) color :" + a.getColor() + ", weight : " + a.getWeight());
			}
			
			/*
			 * and 메서드를 이용해서 "빨간색이 아니면서 무거운 사과"를 선택하도록 두 람다를 조합할 수 있다.
			 */
			Predicate<AppleEntity> notRedAndHeavyApple = notRedApple.and(gram150Up);
			
			if(notRedAndHeavyApple.test(a)) {
				System.out.println("3.8.2 Predicate 조합(빨간색이 아니면서 무거운 사과 - and) color :" + a.getColor() + ", weight : " + a.getWeight());				
			}			
			
			/*
			 * or을 이용해서 "빨간색이면서 무거운(150그램이상) 사과 또는 그냥 녹색 사과"인 경우의 조합
			 * 왼쪽에서 오른쪽으로 연결된다.
			 */
			Predicate<AppleEntity> redAndHeavyAppleOrGreen = redApple
					.and(gram150Up)
					.or((AppleEntity a1) -> "green".equals(a1.getColor()));

			if(redAndHeavyAppleOrGreen.test(a)) {
				System.out.println("3.8.2 Predicate 조합(빨간색이면서 무거운 사과 또는 녹색 사과 - or) color :" + a.getColor() + ", weight : " + a.getWeight());				
			}			
		} );
		
		/*
		 * 3.8.3 Function 조합
		 * 
		 * Function 인터페이스는 Function 인스턴스를 반환하는 andThen, compose 두 가지 디폴트 메서드를 제공한다.
		 * addThen 메서드는 주어진 함수를 먼저 적용한 결과를 다른 함수의 입력으로 전달하는 함수를 반환한다.
		 * 예를 들어 숫자를 증가(x -> x + 1)시키는 f라는 함수가 있고, 숫자에 2를 곱하는 g라는 함수가 있다고 가정하면
		 * f와 g를 조립해서 숫자를 증가시킨 뒤 결과에 2를 곱하는 h라는 함수를 만들 수 있다.
		 */
		Function<Integer, Integer> f = (x) -> x + 1;
		Function<Integer, Integer> g = (x) -> x * 2;
		// 수학적으로 write g(f(x)) 또는 (g o f)(x)라고 표현.		
		System.out.println("3.8.3 Function 조합(andThen 메서드): " + f.andThen(g).apply(1));
		
		/*
		 * compose 메서드는 인수로 주어진 함수를 먼저 실행한 다음에 그 결과를 외부 함수의 인수로 제공한다.
		 * 즉, f.andThen(g)에서 andThen 대신에 compose를 사용하면 g(f(x))가 아니라 f(g(x))라는 수식이 된다.
		 */
		// 수학적으로 write f(g(x)) 또는 (f o g)(x)라고 표현.
		System.out.println("3.8.3 Function 조합(andThen 메서드): " + f.compose(g).apply(1));
		
		/*
		 * 아래 코드는 문자열로 구성된 편지 내용을 변환하는 다양한 유틸 메서드이다.
		 * 여러 유틸 메서드를 조합해서 다양한 변환 파이프라인을 만들 수 있다.
		 */
		Function<String, String> addHeader = Letter::addHeader;
		
		System.out.println("3.8.3 Function 조합 : " + addHeader
				.andThen(Letter::checkSpelling)
				.andThen(Letter::addFooter)
				.apply("kim sungw wook - labda") );
		
		/*
		 * 철자 검사는 빼고 헤더와 푸터만 추가하는 파이프라인도 만들 수 있다.
		 */
		System.out.println("3.8.3 Function 조합 : " + addHeader
			.andThen(Letter::addFooter)
			.apply("kim sungw wook - labda") );
	}

}
