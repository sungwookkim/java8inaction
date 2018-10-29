package Part1.Chapter3.Chapter3_3_6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import Part1.Chapter3.Chapter3_3_6.entity.AppleEntity;
import Part1.Chapter3.Chapter3_3_6.entity.Fruit;
import Part1.Chapter3.Chapter3_3_6.entity.OrangeEntity;

/*
 * 3.6 메서드 레퍼런스
 * 
 * 메서드 레퍼런스를 이용하면 기존의 메서드 정의를 재활용해서 람다처럼 전달할 수 있다.
 * 때로는 람다 표현식보다 메서드 레퍼런스를 사용하는 것이 더 가독성이 좋으며 자연스러울수 있다.
 * 
 * 3.6.1 요약
 * 메서드 레퍼런스는 특정 메서드만을 호출하는 람다의 축약형이라고 생각할 수 있다.
 * 예를 들어 람다가 '이 메서드를 직접 호출해'라고 명령한다면 메서드를 어떻게 호출해야 하는지 설명을
 * 참조하기보다는 메서드명을 직접 참조하는 것이 편리하다.
 * 실제로 메서드 레퍼런스를 이용하면 기존에 구현된 메서드를 람다 표현식을 만들수 있다.
 * 이때 명시적으로 메서드명을 참조함으로써 가독성을 높일수 있다.
 * 메서드 레퍼런스는 메서드명 앞에 구분자(::)를 붙이는 방식으로 메서드 레퍼런스를 활용할 수 있다.
 * 
 * 람다와 메서드 레퍼런스 단축 표현 예제
 * 람다 : (AppleEntity a) -> a.getWeight()
 * 메서드 레퍼런스 단축 표현 : AppleEntity::getWeight
 * 
 * 람다 : () -> Thread.currentThread().dumpStack()
 * 메서드 레퍼런스 단축 표현 : Thread.currentThread()::dumpStack
 * 
 * 람다 : (str, i) -> str.substring(i)
 * 메서드 레퍼런스 단축 표현 : String::substring
 * 
 * 람다 : (String s) -> System.out.println(s)
 * 메서드 레퍼런스 단축 표현 : System.out::println
 * 
 * 메서드 레퍼런스를 새로운 기능이 아니라 하나의 메서드를 참조하는 람다를 편리하게 표현할 수 있는 문법으로 간주할 수 있다.
 */
public class Main_3_6 {

	public static Fruit giveMeFruit(Map<String, Function<Integer, Fruit>> map, String fruit, Integer weight) {
		return map.get(fruit.toLowerCase()).apply(weight);		
	}
	
	public static List<AppleEntity> map(List<Integer> list , Function<Integer, AppleEntity> f) {
		List<AppleEntity> result = new ArrayList<>();
		
		for(Integer e : list) {
			result.add(f.apply(e));
		}
		
		return result;
	}

	public static void main(String[] args) {
		List<AppleEntity> inventory = Arrays.asList(new AppleEntity("green", 100)
				, new AppleEntity("red", 200)
				, new AppleEntity("blue", 300));

		/*
		 * 람다 표현식으로 정렬.
		 */
		inventory.sort((a1, a2) -> a2.getWeight().compareTo(a1.getWeight()));
		inventory.stream().forEach((appleEntity) -> System.out.println("[Lambda - Descending] color : " 
				+ appleEntity.getColor() + ", weight : " + appleEntity.getWeight() ));

		/*
		 * java.util.Comparator.comparing와 메서드 레퍼런스를 이용한 정렬.
		 */
		inventory.sort(Comparator.comparing(AppleEntity::getWeight));
		inventory.stream().forEach((appleEntity) -> System.out.println("[Comparator.comparing - Ascending] color : " 
				+ appleEntity.getColor() + ", weight : " + appleEntity.getWeight() ));

		/*
		 * 정적 메서드 레퍼런스
		 */
		Function<String, Integer> f = (str) -> Integer.parseInt(str);
		System.out.println("정적 메서드 람다 표현식 : " + f.apply("1234"));
		
		f = Integer::parseInt;
		System.out.println("정적 메서드 레퍼런스 표현식: " + f.apply("7894"));

		/*
		 * 다양한 형식의 인스턴스 메서드 레퍼런스
		 */
		f = (str) -> str.length();
		System.out.println("인스턴스 메서드 람다 표현식 : " + f.apply("kim sung"));
		
		f = String::length;
		System.out.println("인스턴스 메서드 레퍼런스 표현식 : " + f.apply("kim sung wook"));
		
		/*
		 * 기존 객체의 인스턴스 메서드 레퍼런스
		 */	
		List<AppleEntity> appleInventory = new ArrayList<AppleEntity>() {
			private static final long serialVersionUID = 1L;
			{
				add(new AppleEntity("blue1", 100));
				add(new AppleEntity("blue2", 200));
				add(new AppleEntity("blue3", 300));
			}
		};

		Function<AppleEntity, Boolean> appleEntityFunction = appleInventory::add;
		appleEntityFunction.apply(new AppleEntity("white2", 500));
		
		appleInventory.stream().forEach((appleEntity) -> System.out.println("기존 객체의 인스턴스 메서드 레퍼런스 : color : " 
				+ appleEntity.getColor() + ", weight : " + appleEntity.getWeight() ));
		
		/*
		 * 3.6.2 생성자 레퍼런스
		 * 
		 * ClassName::new처럼 클래스명과 new 키워드를 이용해서 기존 생성자의 레퍼런스를 만들수 있다.
		 * 이것은 정적 메서드의 레퍼런스를 만드는 방법과 비슷하다.
		 */

		Supplier<AppleEntity> c1 = AppleEntity::new;
		AppleEntity a1 = c1.get();
		System.out.println("3.6.2 생성자 레퍼런스 Supplier(메서드 레퍼런스) : " + a1);
		
		/*
		 * 위 예제는 아래 코드와 같다. 
		 */
		c1 = () -> new AppleEntity();
		a1 = c1.get();
		System.out.println("3.6.2 생성자 레퍼런스 Supplier(람다 표현식) : " + a1);
		
		/*
		 * AppleEntity(Integer weight)라는 시그너처를 갖는 생성자는 Function 인터페이스의 시그너처와 같다.
		 * 따라서 다음과 같이 구현이 가능하다.
		 */
		Function<Integer, AppleEntity> c2 = AppleEntity::new;
		AppleEntity a2 = c2.apply(110);
		System.out.println("3.6.2 생성자 레퍼런스 Function(메서드 레퍼런스) : " + a2);
		
		c2 = (Integer weight) -> new AppleEntity(weight);
		a2 = c2.apply(220);
		System.out.println("3.6.2 생성자 레퍼런스 Function(람다 표현식) : " + a2);
		
		/*
		 * 다음 코드에서 Integer를 포함하는 리스트의 각 요소를 map 메서드를 이용해서 AppleEntity 생성자로 전달한다.
		 * 결과적으로 다양한 무게를 포함하는 사과 리스트가 만들어진다.
		 */
		List<Integer> weights = Arrays.asList(7, 3, 4, 10);
		List<AppleEntity> apples = map(weights, AppleEntity::new);
		
		apples.stream().forEach((AppleEntity a) -> System.out.println("3.6.2 생성자 레퍼런스 map 메서드 : weight = " 
				+ a.getWeight() ));
		
		/*
		 * AppleEntity(String, Integer)처럼 두 인수를 갖는 생성자는 BiFunction인터페이스와 같은 시그너처를 가지므로
		 * 다음처럼 할 수 있다.
		 */
		BiFunction<String, Integer, AppleEntity> c3 = AppleEntity::new;
		AppleEntity a3 = c3.apply("red", 100);
		System.out.println("3.6.2 생성자 레퍼런스 BiFunction(메서드 레퍼런스) : " + a3);
		
		/*
		 * 인스턴스화하지 않고도 생성자에 접근할 수 있는 기능을 다양한 상황에 응용할 수 있다.
		 * 예를 들어 Map으로 생성자와 문자열값을 관련시킬 수 있다.
		 * 그리고 String과 Integer가 주어졌을 때 다양한 무게를 갖는 여러 종류의 과일을 만드는 메서드를
		 * 만들수 있다.
		 */
		Map<String, Function<Integer, Fruit>> map = new HashMap<String, Function<Integer,Fruit>>(){
			private static final long serialVersionUID = 1L;

			{
				put("apple", AppleEntity::new);
				put("orange", OrangeEntity::new);
			}
		};
		
		System.out.println("giveMeFruit 메서드(AppleEntity 객체) : " + giveMeFruit(map, "apple", 100).getWeight());
		System.out.println("giveMeFruit 메서드(OrangeEntity 객체) : " + giveMeFruit(map, "orange", 200).getWeight());
		
	}

}
