package Part2.Chapter6.Chapter6_6_3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import Part2.Chapter6.Chapter6_6_3.entity.Dish;

/*
 * 6.3 그룹화
 * 
 * 데이터 집합을 하나 이상의 특성으로 분류해서 그룹화하는 연산도 데이터베이스에서 많이 수행되는 작업이다. 
 * 자바 8의 함수형을 이용하면 가독성 있는 한 줄의 코드로 그룹화를 구현할 수 있다.
 */
public class Main_6_3 {

	public enum CaloricLevel {DIET, NORMAL, FAT}

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
		 * 아래 코드는 고기를 포함한 그룹, 생성을 포함한 그룹, 나머지 그룹으로 메뉴화한 코드이다.
		 * 
		 * 스트림의 각 요리에서 Dish.Type과 일치하는 모든 요리를 추출하는 함수를 groupingBy 메서드로 전달했다.
		 * 이 함수를 기준으로 스트림이 그룹화하므로 이를 "분류 함수(classification function)"라고 부른다. 
		 */
		System.out.println(menu.stream()
			.collect(Collectors.groupingBy(Dish::getType)) );

		/*
		 * 아래 코드 같이 400칼로리 이하를 'diet'로 400 ~ 700칼로리를 'normal'로 700 칼로리 초과를 'fat' 요리로
		 * 분류한다고 가정하면 Dish 클래스에는 이러한 연산에 필요한 메서드가 없으므로 메서드 레퍼런스를 분류 함수로
		 * 사용할 수 없다. 따라서 메서드 레퍼런스 대신 람다 표현식으로 필요한 로직을 구현할 수 있다.
		 */
		System.out.println(menu.stream()
			.collect(Collectors.groupingBy(dish -> {
				if(dish.getCalories() <= 400) {
					return CaloricLevel.DIET;
				} else if(dish.getCalories() <= 700) {
					return CaloricLevel.NORMAL;
				} else {
					return CaloricLevel.FAT;
				}				
			})) );
		
		/*
		 * 6.3.1 다수준 그룹화
		 * 
		 * 두 인수를 받는 팩토리 메서드 Collectors.groupingBy를 이용해서 항목을 다수준으로 그룹화 할 수 있다.
		 * Collectors.groupingBy는 일반적인 분류 함수와 컬렉터를 인수로 받는다.
		 * 즉, 바깥 쪽 groupingBy 메서드에 스트림의 항목을 분류할 두 번째 기준을 정의하는 내부 groupingBy를
		 * 전달해서 두 수준으로 스트림의 항목을 그룹화할 수 있다.
		 * 
		 * 보통 groupingBy의 연산을 버킷(bucket - 물건을 담을 수 있는 양동이) 개념으로 생각하면 쉽다.
		 * 첫 번째 groupingBy는 각 키의 버킷을 만든다. 그리고 준비된 각각의 버킷을 서버스트림 컬렉터로
		 * 채워가기를 반복하면서 n수준 그룹화를 달성한다.
		 */
		System.out.println(menu.stream()
			.collect(Collectors.groupingBy(
				/*
				 * 외부 맵은 첫 번째 수준의 분류 함수에서 분류한 키값(fish, meat, other)를 갖는다.
				 */
				Dish::getType
				/*
				 * 그리고 외부 맵의 값은 두 번째 수준의 분류 함수의 기준 'normal', 'diet', 'fat'을 키값을 갖는다.
				 * 최종적으로 두 수준의 맵은 첫 번째 키와 두 번째 키의 수준에 부합하는 요소 리스트를 값(salmon, pizza)으로 갖는다.
				 * 다수준 그릅화 연산은 다양한 수준으로 확장할 수 있따.
				 * 즉, n수준 그룹화의 결과는 n수준 트리 구조로 표현되는 n수준 맵이 된다.
				 */
				, Collectors.groupingBy(dish -> {
					if(dish.getCalories() <= 400) {
						return CaloricLevel.DIET;
					} else if(dish.getCalories() <= 700) {
						return CaloricLevel.NORMAL;
					} else {
						return CaloricLevel.FAT;
					}				
				})
			)
		));
				
		/*
		 * 6.3.2 서브그룹으로 데이터 수집
		 * 
		 * 첫 번쨰 groupingBy로 넘겨주는 컬렉터의 형식은 제한이 없다.
		 * 예를 들어 다음 코드처럼 groupingBy 컬렉터에 두 번째 인수로 counting 컬렉터를 전달해서
		 * 메뉴에서 요리의 수를 종류별로 계산할 수 있다.
		 * 
		 * 분류 함수 한 개의 인수를 갖는 groupingBy(f)는 사실 groupingBy(f, toList())의 축약형이다.
		 */
		System.out.println("6.3.2 서브그룹으로 데이터 수집 : " + menu.stream()
			.collect(Collectors.groupingBy(Dish::getType, Collectors.counting())) );
		
		/*
		 * 요리의 종류를 분류하는 컬렉터로 메뉴에서 가장 높은 칼로리를 가진 요리를 아래와 같이 재구현할 수 있다.
		 */
		System.out.println("6.3.2 서브그룹으로 데이터 수집 : " + menu.stream()
				.collect(Collectors.groupingBy(Dish::getType
					, Collectors.maxBy(Comparator.comparingInt(Dish::getCalories))) ));
		
		/*
		 * 컬렉터 결과를 다른 형식에 적용하기
		 * 
		 * 위 코드에서 마지막 그룹화 연산에서 맵의 모든 값을 Optional로 감쌀 필요가 없기 때문에
		 * Optional을 삭제 할 수 있다.
		 * 즉, 다음 처럼 팩토리 메서드 Collectors.collectingAndThen으로 컬렉터가 반환한 결과를
		 * 다른 형식으로 활용할 수 있다.
		 * 
		 * 펙토리 메서드 collectingAndThen은 적용할 컬렉터와 변환 함수를 인수로 받아 다른 컬렉터를 반환한다.
		 * 반환되는 컬렉터는 기존 컬렉터의 래퍼 역할을 하며 collect의 마지막 과정에서 변환 함수로 자신이 반환하는
		 * 값을 매핑한다.
		 * 
		 * 아래 예제는 maxBy로 만들어진 컬렉터가 감싸지는 컬렉터며 변환 함수 Optional::get으로 반환된 Optional에
		 * 포함된 값을 추출한다.
		 * 
		 * 이미 언급했듯이 리듀싱 컬렉터는 Optional.empty()를 반환하지 않으므로 안전한 코드이다.
		 */
		System.out.println("6.3.2 서브그룹으로 데이터 수집 : " + menu.stream()
			.collect(Collectors.groupingBy(
				/*
				 * 요리의 종류에 따라 메뉴 스트림을 서브스트림으로 그룹화 한다.
				 */
				Dish::getType
				/*
				 * Collectors.collectingAndThen 컬렉터는 세 번째 컬렉터 maxBy를 감싼다.
				 */
				, Collectors.collectingAndThen(
					Collectors.maxBy(Comparator.comparingInt(Dish::getCalories))
					/*
					 * 리듀싱 컬렉터가 서브스트림에 연산을수행한 결과에 collectingAndThen의 Optional::get
					 * 변환 함수가 적용된다.
					 */					
					, Optional::get
				)
			)
		) );
		
		/*
		 * groupingBy와 함께 사용하는 다른 컬렉터 예제
		 * 
		 * 일반적으로 스트림에서 같은 그룹으로 분류된 모든 요소에 리듀싱 작업을 수행할 때는
		 * 팩토리 메서드 groupingBy에 두 번째 인수로 전달한 컬렉터를 사용한다.
		 * 
		 * 예를 들어 메뉴에 있는 모든 요리의 칼로리 합계를 구하려고 만든 컬렉터를 재사용할 수 있다.
		 */
		System.out.println("groupingBy와 함께 사용하는 다른 컬렉터 예제 : " + menu.stream()
			.collect(Collectors.groupingBy(
				Dish::getType
				, Collectors.summingInt(Dish::getCalories) )
			) );
		
		/*
		 * 이 외에도 mapping 메서드로 만들어진 컬렉터도 groupingBy와 자주 사용된다.
		 * mapping 메서드는 스트림의 인수를 변환하는 함수와 변환 함수의 결과 객체를 누적하는 컬렉터를
		 * 인수로 받는다.
		 * mapping은 입력 요소를 누적하기 전에 매핑 함수를 적용해서 다양한 형식의 객체를 주어진 형식의
		 * 컬렉터에 맞게 변환하는 역할을 한다.
		 * 다음 코드처럼 groupingBy와 mapping 컬렉터를 합친 기능이다.
		 */
		System.out.println("groupingBy와 함께 사용하는 다른 컬렉터 예제 : " + menu.stream()
			.collect(Collectors.groupingBy(
				Dish::getType
				, Collectors.mapping(
				// 스트림의 인수를 변환하는 함수
				dish -> {
					if(dish.getCalories() <= 400) {
						return CaloricLevel.DIET;
					} else if(dish.getCalories() <= 700) {
						return CaloricLevel.NORMAL;
					} else {
						return CaloricLevel.FAT;
					}
				}
				// 변환 함수의 결과 객체를 누적하는 컬렉터.
				, Collectors.toCollection(HashSet::new) )
				/*, Collectors.toSet() )*/
			)
		) );
	}

}
