package Part1.Chapter2.Chapter2_2_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Part1.Chapter2.Chapter2_2_2.entity.AppleEntity;
import Part1.Chapter2.Chapter2_2_2.filters.impl.AppleHeavyColorPredicate;
import Part1.Chapter2.Chapter2_2_2.filters.impl.AppleHeavyWeightPredicate;
import Part1.Chapter2.Chapter2_2_2.filters.inter.ApplePredicate;

/*
 * 2.2 동작 파라미터화
 * 2.1절에서 파라미터를 추가하는 방법이 아닌 변화하는 요구사항에 좀 더 유연하게 대응할 수 있게
 * 전체을 보면 우리의 선택 조건은 
 * 		사과의 어떤 속성에 기초해서 불린값을 반환(예를 들어 사과가 녹색인가? 150그램 이상인가?)하는 방법
 * 을 결정 할 수 있다.
 * 이와 같은 동작을 프레디케이트라고 한다.
 * 
 * 먼저 선택 조건을 결정하는 인터페이스를 만들자.
 * (Part1.Chapter2.Chapter2_2_2.filters.inter.ApplePredicate)
 * 
 * 그리고 
 * 		- 무거운 사과만 선택
 * 		- 녹색 사과만 선택
 * 과 같은 필터 기능을 위해 해당 인터페이스의 구현체를 만든다.
 * 		Part1.Chapter2.Chapter2_2_2.filters.impl.AppleHeavyWeightPredicate
 * 			무거운 사과만 선택 하는 필터. 			
 *		Part1.Chapter2.Chapter2_2_2.filters.impl.AppleHeavyColorPredicate
 *			녹색 사과만 선택하는 필터.
 * 
 * 위 조건에 따라 filter 메서드가 다르게 동작 하는데 이를 전략 디자인 패턴(strategy design pattern)이라고 부른다.
 * 전략 디자인 패턴은 
 * 		각 알고리즘을 캡슐화하는 알고리즘 패밀리를 정의해둔 다음에 런타임에 알고리즘을 선택하는 기법이다.
 * 우리 예제에서는 ApplePredicate가 알고리즘 패밀리고 AppleHeavyWeightPredicate, AppleHeavyColorPredicate가 전략이다.
 * */
public class Main_2_2 {

	/*
	 * 2.2.1 네 번째 시도 : 추상적 조건으로 필터링(이 정도면 갑님한테 돈을 더 요구 할수도 있을거 같다..)
	 */
	public static List<AppleEntity> filterApples(List<AppleEntity> inventory, ApplePredicate p) {
		List<AppleEntity> result = new ArrayList<>();
		
		for(AppleEntity apple : inventory) {
			/*
			 * 해당 예제에서 가장 중요한 구현은 test 메서드 이다.
			 * filterApples 메서드의 새로운 동작을 정의하는 것이기 때문이다.
			 * 메서드는 객체만 인수로 받으므로 test 메서드를 ApplePredicate 객체로 감싸서 전달해야 한다.
			 * test 메서드를 구현하는 객체를 이용해서 불린 표현식 등을 전달할 수 있으므로 
			 * 이는 '코드를 전달'할 수 있는것이나 다름없다.
			 */
			if(p.test(apple)) {
				result.add(apple);
			}
		}
		
		return result;
	}
	public static void main(String[] args) {
		List<AppleEntity> inventory = Arrays.asList(new AppleEntity("green", 100)
				, new AppleEntity("red", 120)
				, new AppleEntity("green", 150));
		
		/*
		 * 이렇게 동작 파라미터화, 즉 메서드가 다양한 동작(또는 전략)을 받아서 내부적으로 다양한 동작을 수행할 수 있다.
		 * 이렇게 함으로써 filterApples 메서드 내부에서
		 * 		컬렉션을 반복하는 로직과 컬렉션의 각 요소에 적용할 동작을(예제에서는 프레디케이트)분리
		 * 한다는 점에서 소프트웨어 엔지니어링적으로 큰 이득을 얻는다.
		 */
		filterApples(inventory, new AppleHeavyColorPredicate()).stream()
			.forEach((apple) -> System.out.println("[AppleHeavyColorPredicate] color : " + apple.getColor() + ", weight : " + apple.getWeight()) );

		filterApples(inventory, new AppleHeavyWeightPredicate()).stream()
			.forEach((apple) -> System.out.println("[AppleHeavyWeightPredicate] color : " + apple.getColor() + ", weight : " + apple.getWeight()) );
	}

}
