package Part1.Chapter3.Chapter3_3_5;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;

import Part1.Chapter3.Chapter3_3_5.entity.AppleEntity;

/*
 * 3.5 형식 검사, 형식 추론, 제약
 * 
 * 람다 표현식 자체에는 람다가 어떤 함수형 인터페이스를 구현하는지의 정보가 포함되어 있지 않다.
 * 따라서 람다 표현식을 더 제대로 이해하려면 람다의 실제 형식을 파악해야 한다. 
 */
public class Main_3_5 {

	public static List<AppleEntity> filter(List<AppleEntity> inventory, Predicate<AppleEntity> p) {
		List<AppleEntity> result = new ArrayList<>();
		
		for(AppleEntity apple : inventory) {
			if(p.test(apple)) {
				result.add(apple);
			}			
		}
		
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		List<AppleEntity> inventory = Arrays.asList(new AppleEntity("red", 100)
				, new AppleEntity("green", 200));
		
		/*
		 * 3.5.1 형식 검사
		 * 
		 * 람다가 사용되는 콘텍스트(context)를 이용해서 람다의 형식(type)을 추론할 수 있다.
		 * 어떤 콘텍스트(예를 들면 람다가 전달될 메서드 파라미터나 람다가 할당되는 변수 등)
		 * 에서 기대되는 람다 표현식의 형식을 "대상 형식(target type)"이라고 부른다.
		 * 
		 * 위 람다 표현식을 사용할 때 다음과 같은 순서로 형식 확인 과정이 진행된다.
		 * 1. filter 메서드의 선언을 확인.
		 * 2. filter 메서드는 두 번째 파라미터로 Predicate<AppleEntity> 형식(대상 형식)을 기대한다.
		 * 3. Predicate<AppleEntity>은 test라는 한 개의 추상 메서드를 정의하는 함수형 인터페이스이다.
		 * 4. test 메서드는 AppleEntity을 받아 boolean을 반환하는 함수 디스크립터를 묘사한다.
		 * 5. filter 메서드로 전달된 인수는 이와 같은 요구사항을 만족해야 한다.
		 * 
		 * 위 예제에서 람다 표현식은 AppleEntity을 인수로 받아 boolean을 반환하므로 유효한 코드이다.
		 * 람다 표현식이 예외를 던질 수 있다면 추상 메서드도 같은 예외를 던질 수 있도록 throws로 선언해야 한다.
		 * 
		 * 람다 표현식의 형식 검사 과정의 재구성
		 * filter(inventory, (AppleEntity a) -> a.getWeight() > 150)	
		 * 		1. 람다가 사용된 콘텍스트는 무엇인가? 우선 filter의 정의를 확인한다.	
		 * filter(inventory, Predicate<AppleEntity> p)	
		 * 		2. 대상 형식은 Predicate<AppleEntity>이다.	
		 * 대상 형식
		 * 		3. Predicate<AppleEntity>인터페이스의 추상 메서드는 무엇인가?	
		 * boolean test(AppleEntity appleEntity)	
		 * 		4. AppleEntity을 인수로 받아 boolean을 반환하는 test 메서드다.	
		 * AppleEntity -> boolean	
		 * 		5. 함수 디스크립터는 AppleEntity -> boolean이므로 람다의 시그너처와 일치한다.
		 * 		람다도 AppleEntity을 인수로 받아 boolean을 반환하므로 코드 형식 검사가 완료된다.
		 */
		filter(inventory
				, (AppleEntity a) -> a.getWeight() > 150)
			.stream()
			.forEach((AppleEntity a) -> System.out.println("3.5.1 형식 검사 : " 
				+ a.getColor() +" apple - " + a.getWeight() + " weight." ));
		/*
		 * 3.5.2 같은 람다, 다른 함수형 인터페이스
		 * 
		 * 대상 형식이라는 특징 때문에 같은 람다 표현식이더라도 호환되는 추상 메서드를 가진 다른 함수형 인터페이스로 사용될 수 있다.
		 * 예를 들어 Callable과 PrivilegedAction 인터페이스는 () -> T와 같은 메서드 시그너처를 가지고 있다.
		 * 따라서 아래 할당문은 모두 유효한 코드이다.
		 */
		Callable<Integer> c = () -> 42;
		System.out.println("Callable : " + c.call());
		PrivilegedAction<Integer> p = () -> 52;
		System.out.println("PrivilegedAction : " + p.run());
		
		/* 아래 코드와 같이 하나의 람다 표현식을 다양한 함수형 인터페이스에 사용할 수 있다.*/
		Comparator<AppleEntity> c1 = (AppleEntity a1, AppleEntity a2) -> a1.getWeight().compareTo(a2.getWeight());
		System.out.println("Comparator : " + c1.compare(new AppleEntity("green", 200), new AppleEntity("red", 100)));
		
		ToIntBiFunction<AppleEntity, AppleEntity> c2 = (AppleEntity a1, AppleEntity a2) -> a1.getWeight().compareTo(a2.getWeight());
		System.out.println("ToIntBiFunction : " + c2.applyAsInt(new AppleEntity("green", 100), new AppleEntity("red", 200)));

		BiFunction<AppleEntity, AppleEntity, Integer> c3 = (AppleEntity a1, AppleEntity a2) -> a1.getWeight().compareTo(a2.getWeight());
		System.out.println("BiFunction : " + c3.apply(new AppleEntity("green", 100), new AppleEntity("red", 200)));;
		
		/*
		 * 다이아몬드 연산자
		 * 주어진 클래스 인스턴스 표현식을 두 개 이상의 다양한 콘텍스트에 사용할 수 있다.
		 * 이때 인스턴스 표현식의 형식 인수는 콘텍스트에 의해 추론된다.
		 * 		List<String> listOfStrings = new ArrayList<>();
		 * 		List<Integer> listOfIntegers = new ArrayList<>();
		 * 
		 * 특별한 void 호환 규칙
		 * 람다의 바디에 일반 표현식이 있으면 void를 반환하는 함수 디스크립터와 호환된다.(물론 파라미터 리스트도 호환되어야 함.)
		 * 예를 들어 다음 두 행의 예제에서 List의 add 메서드는 Consumer 콘텍스트(T -> void)가 기대하는 void 대신 boolean을 반환하지만
		 * 유효한 코드이다.
		 * 		// Predicate는 불린 반환값을 갖는다.
		 * 		Predicate<String> p = s -> list.add(s);
		 * 		// Consumer는 void 반환값을 갖는다.
		 * 		Consumer<String> b = s -> list.add(s);
		 */
		
		/*
		 * 3.5.3 형식 추론
		 * 
		 * 자바 컴파일러는 람다 표현식이 사용된 콘텍스트(대상 형식)을 이용해서 람다 표현식과 관련된 함수형 인터페이스를 추론한다.
		 * 즉, 대상 형식을 이용해서 함수 디스크립터를 알 수 있으므로 컴파일러는 람다의 시그너처도 추론할 수 있다.
		 * 결과적으로 컴파일러는 람다 표현식의 파라미터 형식에 접근할 수 있으므로 람다 문법에서는 이를 생략할 수 있다.
		 */
		filter(inventory, (a) -> "green".equals(a.getColor()) )
			.stream()
			.forEach((a) -> System.out.println("3.5.2 형식 추론 : " 
					+ a.getColor() +" apple - " + a.getWeight() + " weight." ));
		
		/*
		 * 여러 파라미터를 포함하는 람다 표현식에서는 코드 가독성 향상이 두드러진다. 
		 * 다음은 Comparator 객체를 만드는 코드다.
		 */
		// 형식 추론을 하지 않음.
		Comparator<AppleEntity> notTypeResaoning = (AppleEntity a1, AppleEntity a2) -> a1.getWeight().compareTo(a2.getWeight());
		System.out.println("notTypeResaoning : " + notTypeResaoning.compare(new AppleEntity("red", 300), new AppleEntity("blue", 400)));
		
		// 형식 추론 함.
		Comparator<AppleEntity> typeResaoning = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight());
		System.out.println("typeResaoning : " + typeResaoning.compare(new AppleEntity("red", 500), new AppleEntity("blue", 400)));
		
		/*
		 * 3.5.4 지역 변수 사용
		 * 
		 * 람다 표현식에서는 익명 함수가 하는 것처럼 자유 변수(free variable - 파라미터로 넘겨진 변수가 아닌 외부에서 정의된 변수)를
		 * 활용할 수 있다. 이와 같은 동작을 람다 캡쳐링(capturing lambda)이라고 한다.
		 * 아래 예는 portNumber 변수를 캡처하는 람다이다.
		 */
		int portNumber = 80;
		Runnable r = () -> System.out.println("capturing lambda : " + portNumber);
		r.run();
		
		/*
		 * 하지만 자유 변수에도 약간의 제약이 있다. 인스턴스 변수와 정적 변수를 캡처(자신의 바디에서 참조할 수 있도록)할 수 있다.
		 * 하지만 그러려면 지역 변수는 명시적으로 final로 선언되어 있어야 하거나 실질적으로 final로 선언된 변수와 똑같이 사용되어야 한다.
		 * 즉, 람다 표현식은 한 번만 할당할 수 있는 지역 변수를 캡처할 수 있다.(참고 : 인스턴스 변수 캡처는 final 지역 변수 this를 캡처하는 것과 마찬가지이다.)
		 */
		int errorPortNumber = 81;
		Runnable r1 = () -> System.out.println("errorPortNumber lambda : " + errorPortNumber);
		/* 아래 변수의 재할당 하는 부분을 주석 해제 하면 위 람다 표현식에서 에러가 발생한다. */
		//errorPortNumber = 82;
		r1.run();
		
		/*
		 * 지역 변수의 제약
		 * 지역 변수의 이런 제약은 인스턴스 변수와 지역 변수는 태생이 다른데 부터 발생한다.
		 * 인스턴스 변수는 힙에 저장되는 반면 지역 변수는 스택에 위치한다. 람다에서 지역 변수에 바로 접근할 수 있다는
		 * 가정 하에 람다가 스레드에서 실행된다면 변수를 할당한 스레드가 사라져서 변수 할당이 해제되었음에도 람다를 실행하는
		 * 스레드에서는 해당 변수에 접근하려 할 수 있다.
		 * 따라서 원래 변수에 접근을 허용하는 것이 아니라 자유 변수의 복사본을 제공한다.
		 * 따라서 복사본의 값이 바뀌지 않아야 하므로 지역 변수에는 한 번만 값을 할당해야 한다는 제약이 생긴 것이다.
		 * 
		 * 또한 지역 변수의 제약 때문에 외부 변수를 변화시키는 일반적인 명령형 프로그래밍 패턴(병렬화를 방해는 요소)에 제동을 걸 수 있다.
		 */
		
		/*
		 * 클로저
		 * 원칙적으로 클로저란 함수의 비지역 변수를 자유롭게 참조할 수 있는 함수의 인스턴스를 가리킨다.
		 * 예를 들어 클로저를 다른 함수의 인수로 전달할 수 있고 클로저는 클로저 외부에 정의된 변수의 값에 접근하고 값을 바꿀 수 있다.
		 * 자바 8의 람다와 익명 클래스는 클로저와 비슷한 동작을 수행한다.
		 * 람다와 익명 클래스 모두 메서드의 인수로 전달될 수 있으며 자신의 외부 영역의 변수에 접근할 수 있다.
		 * 다만 람다와 익명 클래스는 람다가 정의된 메서드의 지역 변수 값은 바꿀 수 없다.
		 * 람다가 정의된 메서드의 지역 변수값은 final 변수여야 한다. 덕분에 람다는 변수가 아닌 값에 국한되어 어떤 동작을 수행한다는 사실이
		 * 명확해진다.
		 * 이전에도 설명한 것 처럼 지역 변수값은 스택에 존재하므로 자신을 정의한 스레드와 생존을 같이 해야 하며 따라서 지역 변수는 final이어야 한다.
		 * 가변 지역 변수를 새로운 스레드에서 캡처할 수 있다면 안전하지 않는 동작을 수행할 가능성이 생긴다.(인스턴스 변수는 스레드가 공유하는 힙에
		 * 존재하므로 특별한 제약이 없다.)
		 */
	}

}
