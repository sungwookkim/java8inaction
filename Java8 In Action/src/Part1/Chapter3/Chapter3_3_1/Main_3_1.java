package Part1.Chapter3.Chapter3_3_1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import Part1.Chapter3.Chapter3_3_1.entity.AppleEntity;

/*
 * 3.1 람다란 무엇인가?
 * 
 * 람다 표현식은 메서드로 전달할 수 있는 익명 함수를 단순화한 것이라고 할 수 있다.
 * 람다 표현식은 이름은 없지만
 * 		- 파라미터 리스트
 * 		- 바디
 * 		- 반환 형식
 * 		- 발생할 수 있는 예외 리스트
 * 는 가질 수 있다.
 * 
 * 람다의 특징으로
 * 		익명
 * 			보통의 메서드와 달리 이름이 없으므로 익명이라 표현한다.
 * 		함수
 * 			람다는 메서드처럼 특정 클래스에 종속되지 않으므로 함수라고 부른다.
 * 			하지만 메서드처럼 파라미터 리스트, 바디, 반환 형식, 가능한 예외 리스트를 포함한다.
 *		전달
 *			람다 표현식을 메서드 인수로 전달하거나 변수로 저장할 수 있다.
 *		간결성
 *			익명 클래스처럼 많은 코드를 구현할 필요가 없다.
 *
 * 람다(lambda)라는 용어는 람다 미적분학 학계에서 개발한 시스템에서 유래했다.
 * 람다 표현식이 중요한 이유는 코드를 전달하는 과정에서 자질구레한 코드가 많이 생긴다. 
 * 그러나 람다를 이용하면 간결한 방식으로 코드를 전달할 수 있다.
 */
public class Main_3_1 {
	public static void appleComparator(List<AppleEntity> inventory) {
		/*
		 * 익명 클래스를 이용한 정렬.
		 */
		Comparator<AppleEntity> byWeight = new Comparator<AppleEntity>() {

			@Override
			public int compare(AppleEntity o1, AppleEntity o2) {
				return o1.getWeight().compareTo(o2.getWeight());
			}
		};
		
		inventory.sort(byWeight);
		
		inventory.stream()
			.forEach((AppleEntity apple) -> System.out.println("[Anonymous Class - Ascending] color : " + apple.getColor() 
			+ ", weight : " + apple.getWeight() ));
		
		/*
		 * 람다 표현식으로 이용한 정렬.
		 * 
		 * 람다는 세 부분으로 이루어진다.
		 * 		(AppleEntity o1, AppleEntity o2) : 람다 파라미터
		 * 		-> : 화살표
		 * 		o2.getWeight().compareTo(o1.getWeight()) : 람다 바디 
		 * 			- 파라미터 리스트
		 * 				Comparator의 compare 메서드의 파라미터
		 *			- 화살표
		 *				화살표(->)는 람다의 파라미터 리스트와 바디를 구분한다.
		 *			- 람다의 바디
		 *				두 사과의 무게를 비교한다. 람다의 반환값에 해당하는 표현식이다.
		 * 
		 * 아래는 자바 8에서 지원하는 다섯 가지 람다 표현식 예제다.
		 * 		- (String s) -> s.length()
		 * 			String 형식의 파라미터 하나를 가지며 int를 반환한다.
		 * 			람다 표현식에서는 return이 함축되어 있으므로 return 문을
		 * 			명시적으로 사용하지 않아도 된다.
		 * 
		 * 		- (AppleEntity apple) -> apple.getWeight() > 150
		 * 			AppleEntity 형식의 파라미터 하나를 가지며
		 * 			boolean(사과가 150그램보다 무거운지 결정)을 반환한다.
		 * 
		 * 		- (int x, int y) -> {
		 * 			System.out.println("Result : ");
		 * 			System.out.println(x + y);
		 * 		}
		 *		int 형식의 파라미터를 두 개 가지며 리턴값이 없다(정확히 void 리턴)
		 *		이 예제에서 볼 수 있듯이 람다 표현식은 여러 행의 문장을 포함할 수 있다.
		 *	
		 *		- () -> 42
		 *			파라미터가 없으며 int를 반환한다.
		 *
		 *		- (AppleEntity o1, AppleEntity o2) -> o2.getWeight().compareTo(o1.getWeight())
		 *			AppleEntity 형식의 파라미터를 두 개 가지며 int(두 사과의 무게 비교 결과)를 반환한다.
		 *
		 * 자바 설계자는 이미 C#이나 스칼라 같은 비슷한 기능을 가진 다른 언어와 비슷한 문법을 
		 * 자바에 적용하기로 했다. 아래는 람다의 기본 문법이다.
		 * 		(parameters) -> expression
		 * 또는 다음처럼 표현할 수 있다.(중괄호 이용)
		 * 		(parameters) -> { expression }
		 * 
		 * 아래는 람다 예제와 사용 사례 리스트를 보여준다.
		 *		- 불린 표현식 : (List<String> list) -> list.isEmpty()
		 *		- 객체 생성 : () -> new AppleEntity()
		 *		- 객체에서 소비 : 
		 *			(AppleEntity apple) -> {
		 *				System.out.println(apple.getWeight());
		 *			}
		 *		- 객채에서 선택/추출 : (String s) -> s.length();
		 *		- 두 값을 조합 : (int a, int b) -> a * b
		 *		- 두 객체 비교 :
		 *			(AppleEntity o1, AppleEntity o2) -> o2.getWeight().compareTo(o1.getWeight())
		 */
		byWeight = (AppleEntity o1, AppleEntity o2) -> o2.getWeight().compareTo(o1.getWeight());
		
		inventory.sort(byWeight);
		
		inventory.stream()
		.forEach((AppleEntity apple) -> System.out.println("[Lambda - Descending] color : " + apple.getColor() 
		+ ", weight : " + apple.getWeight() ));
	}
	
	public static void main(String[] args) {
		List<AppleEntity> inventory = Arrays.asList(new AppleEntity("green", 100)
				, new AppleEntity("red", 120)
				, new AppleEntity("green", 150));
		
		appleComparator(inventory);

	}

}
