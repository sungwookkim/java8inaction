package Part1.Chapter3.Chapter3_3_4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/*
 * 3.4 함수형 인터페이스 사용.
 * 
 * 함수형 인터페이스는 오직 하나의 추상 메서드를 지정한다.
 * 함수형 인터페이스의 추상 메서드는 람다 표현식의 시그너처를 묘사하고 추상 메서드 시그너처를 함수 디스크립터(function descriptor)라고 한다.
 * 다양한 람다 표현식을 사용하려면 공통의 함수 디스크립터를 기술하는 함수형 인터페이스 집합이 필요하다.
 */
public class Main_3_4 {
	
	/*
	 * 3.4.1 Predicate
	 * 
	 * java.util.function.Predicate<T> 인터페이스는 test라는 추상 메서드를 정의하며
	 * test는 제네릭 형식 T의 객체를 인수로 받아 boolean을 반환한다.
	 */
	public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> result = new ArrayList<>();
		
		for(T s : list) {
			if(p.test(s)) {
				result.add(s);
			}
		}

		return result;
	}
	
	/*
	 * 3.4.2 Consumer
	 * 
	 * java.util.function.Consumer<T> 인터페이스는 제네릭 형식 T 객체를 받아서, void를 반환하는 accept라는 추상 메서드를 정의한다.
	 * T 형식의 객체를 인수로 받아서 어떤 동작을 수행하고 싶을 때 Consumer 인터페이스를 사용할 수 있다.
	 */
	public static <T> void forEach(List<T> list, Consumer<T> c) {
		for(T s : list) {
			c.accept(s);
		}
	}
	
	/*
	 * 3.4.3 Function
	 * 
	 * java.util.function.Function<T, R>인터페이스는 제네릭 형식 T를 인수로 받아서 제네릭 형식 R 객체를 반환하는 apply라는 추상 메서드를 정의한다.
	 * 입력을 출력으로 매핑하는 람다를 정의할 때 Function 인터페이스를 활용할 수 있다.
	 * 
	 * 아래 예제는 String 리스트를 인수로 받아 각 String의 길이를 포함하는 Intger 리스트로 반환하는 예 이다.
	 */
	public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
		List<R> result = new ArrayList<>();
		
		for(T s : list) {
			result.add(f.apply(s));
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		/*
		 * 3.4.1 Predicate
		 * Predicate의 test 메서드 시그너쳐는 인수로 제너릭 형식을 받고 반환값을 boolean으로 반환한다.
		 * 해당 시그너처와 동일하게 
		 * 		(String s) - 인수는 제너릭 형식이기 때문에 어떤 형태로 인수를 전달하여도 상관없다.
		 * 		!s.isEmpty - boolean으로 반환값을 하였기 때문에 test 메서드 시그너처와 동일하다.
		 * 하였기 때문에 람다 표현식으로 올바른 표현식이다. 
		 */
		Predicate<String> nonEmptyStrPredicate = (String s) -> !s.isEmpty();		
		System.out.println("Predicate : " + filter(Arrays.asList("kim", "", "sung", "wook", "") , nonEmptyStrPredicate));
		
		/*
		 * 3.4.2 Consumer
		 */
		Consumer<String> nameStrConsumer = (String s) -> System.out.println("Consumer : " + s);
		forEach(Arrays.asList("kim", "sung", "wook"), nameStrConsumer);
		
		/*
		 * 3.4.3 Function
		 */
		Function<String, Integer> strLenFunction = (String s) -> s.length();
		System.out.println("Function : " 
				+ map(Arrays.asList("kim", "sung", "wook", "kim sung", "kim sung wook"), strLenFunction));
		
		/*
		 * 기본형 특화
		 * 
		 * 자바의 모든 형식은 참조형(reference type - Byte, Integer, Object, List 등) 아니면 기본형(primitive type - int, double, byte, char 등)
		 * 에 해당한다. 하지만 제네릭 파라미터는 제네릭의 내부 구현 때문에 참조형만 사용할 수 있다.
		 * 자바에서는 기본형을 참조형으로 변환할 수 있는 기능을 제공한다. 이 기능을 박싱(boxing)이라고 한다.
		 * 반대로 참조형을 기본형으로 변환하는 반대 동작을 언박싱(unboxing)이라고 한다.
		 * 그리고 프로그래머가 편리하게 코드를 구현할 수 있도록 박싱과 언방식으로 자동으로 해주는 오토박싱(autoboxing)이라는 기능도 제공한다.
		 */

		/*
		 * int가 Integer로 박싱이 되는 예제이다.
		 * 하지만 이런 변환 과정은 비용이 소모된다. 박싱한 값은 기본형을 감싸는 래퍼며 힙에 저장된다.
		 * 따라서 박싱한 값은 메모리를 더 소비하며 기본형을 가져올 때도 메모리를 탐색하는 과정이 필요하다.
		 */
		List<Integer> intList = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			intList.add(i);
		}
		
		intList.stream()
			.forEach((Integer i) -> System.out.println("int -> Integer Boxing : " + i));
		
		/*
		 * 자바 8에서는 기본형을 입출력으로 사용하는 상황에서 오토박싱 동작을 피할 수 있도록
		 * 특별한 함수형 인터페이스를 제공한다.
		 * 
		 * 아래 예제는 IntPredicate는 1000이라는 값을 박싱하지 않지만, Predicate<Integer> 중 인수가 1000인 값은
		 * Integer 객체로 박싱한다. 
		 */
		IntPredicate evenNumbers = (int i) -> i % 2 == 0;
		System.out.println("IntPredicate : " + evenNumbers.test(1000));
		
		Predicate<Integer> oddNumbers = (Integer i) -> i % 2 == 1;
		System.out.println("boxing - Predicate<Integer> : " + oddNumbers.test(1000));
		System.out.println("not boxing - Predicate<Integer> : " + oddNumbers.test(new Integer(1001)));
		
		/*
		 * 일반적으로 특정 형식을 입력으로 받는 함수형 인터페이스의 이름 앞에서는 DoublePredicate, IntConsumer,
		 * LongBinaryOperator, IntFunction처럼 형식명이 붙는다.
		 * Function 인터페이스는 ToIntFunction<T>, IntToDoubleFunction 등의 다양한 출력 형식 파라미터를 제공한다.
		 * 
		 * 자바 8의 대표적인 함수형 인터페이스
		 * 함수형 인터페이스 : Predicate<T>
		 *		함수 디스크립터 : T -> boolean
		 *		기본형 특화 : 
		 *			IntPredicate, LongPredicate, DoublePredicate
		 *
		 * 함수형 인터페이스 : Consumer<T>
		 * 		함수 디스크립터 : T -> void
		 * 		기본형 특화 :
		 * 			IntConsumer, LongConsumer, DoubleConsumer
		 * 
		 * 함수형 인터페이스 : Function<T, R> 
		 * 		함수 디스크립터 : T -> R
		 * 		기본형 특화 :
		 * 			IntFunction<R>, IntToDoubleFunction, IntToLongFunction, 
		 * 			LongFunction<R>, LongToDoubleFunction, LongToIntFunction,
		 * 			DoubleFunction<R>, ToIntFunction<T>, ToDoubleFunction<T>,
		 * 			ToLongFunction<T>
		 * 
		 * 함수형 인터페이스 : Supplier<T>
		 * 		함수 디스크립터 : () -> T
		 * 		기본형 특화 :
		 * 			BooleanSupplier, IntSupplier, LongSupplier, DoubleSupplier 			
		 * 
		 * 함수형 인터페이스 : UnaryOperator<T>
		 * 		함수 디스크립터 : T -> T 
		 * 		기본형 특화 :
		 * 			IntUnaryOperator, LongUnaryOperator, DoubleUnaryOperator
		 * 
		 * 함수형 인터페이스 : BinaryOperator<T>
		 * 		함수 디스크립터 : (T, T) -> T
		 * 		기본형 특화 :
		 * 			IntBinaryOperator, LongBinaryOperator, DoubleBinaryOperator
		 * 
		 * 함수형 인터페이스 : BiPredicate<L, R>
		 * 		함수 디스크립터 : (L, R) -> boolean
		 * 		기본형 특화 :		 
		 * 
		 * 함수형 인터페이스 : BiConsumer<T, U> 
		 * 		함수 디스크립터 : (T, U) -> void
		 * 		기본형 특화 :
		 * 			ObjIntConsumer<T>, ObjLongConsumer<T>, ObjDoubleConsumer<T>
		 * 
		 * 함수형 인터페이스 : BiFunction<T, U, R>
		 * 		함수 디스크립터 : (T, U) -> R 
		 * 		기본형 특화 :
		 * 			ToIntBiFunction<T, U>, ToLongBiFunction<T, U>, ToDoubleBiFunction<T, U>
		 * 
		 * 사용 사례, 람다 예제, 사용할 수 있는 함수형 인터페이스 등을 총체적으로 제공한다.
		 * 람다와 함수형 인터페이스 예제
		 * 사용 사례
		 * 		불린 표현
		 * 			람다 예제 : (List<String> list) -> list.isEmpty()
		 * 			대응하는 함수형 인터페이스 : Predicate<List<String>>
		 * 		객체 생성
		 * 			람다 예제 : () -> new ApplyEntity(10)
		 * 			대응하는 함수형 인터페이스 : Supplier<ApplyEntity>
		 * 		객체에서 소비
		 * 			람다 예제 : (ApplyEntity appleEntity) -> System.out.println(a.getWeight())
		 * 			대응하는 함수형 인터페이스 : Consumer<ApplyEntity>
		 * 		객체에서 선택/추출
		 * 			람다 예제 : (String s) -> s.length()
		 * 			대응하는 함수형 인터페이스 : Function<String, Integer> 또는 ToIntFunction<String>
		 * 		두 값 조합
		 * 			람다 예제 : (int a, int b) -> a * b
		 * 			대응하는 함수형 인터페이스 : IntBinaryOperator
		 * 		두 객체 비교
		 * 			람다 예제 : (ApplyEntity a1, ApplyEntity a2) -> a1.getWeight().compareTo(a2.getWeight())
		 * 			대응하는 함수형 인터페이스 : Comparator<ApplyEntity> 또는 
		 * 				BiFunction<ApplyEntity, ApplyEntity, Integer> 또는
		 * 				ToIntBiFunction<ApplyEntity, ApplyEntity>
		 */
		
	}

}
