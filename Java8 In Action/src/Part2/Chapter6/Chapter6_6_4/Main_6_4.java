package Part2.Chapter6.Chapter6_6_4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Part2.Chapter6.Chapter6_6_4.entity.Dish;

/*
 * 6.4 분할
 * 
 * 분할은 "분할 함수(partitioning function)"라 불리는 프레디케이트를 분류 함수로 사용하는 특수한 그룹화 기능이다.
 * 분할 함수는 불린을 반환하므로 맵의 키 형식은 Boolean이다.
 * 결과적으로 그룹화 맵은 최대(참 아니면 거짓의 값을 갖는) 두 개의 그룹으로 분류 된다.
 */
public class Main_6_4 {

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
		 * 아래 코드는 모든 요리를 채식 요리와 채식이 아닌 요리로 분류한 코드이다.
		 */
		Map<Boolean, List<Dish>> partitionedMenu = menu.stream()
			.collect(Collectors.partitioningBy(Dish::isVegetarian));
		
		System.out.println("6.4 분할 : " +partitionedMenu);		
		System.out.println("6.4 분할(채식 요리): " + partitionedMenu.get(true));
		System.out.println("6.4 분할(채식이 아닌 요리): " + partitionedMenu.get(false));
		
		Predicate<Dish> isVegetarian = Dish::isVegetarian;
		
		System.out.println("6.4 분할(filter 메서드 사용 - 채식 요리) : " + menu.stream()
			.filter(isVegetarian)
			.collect(Collectors.toList()) );
		
		System.out.println("6.4 분할(filter 메서드 사용 - 채식이 아닌 요리) : " + menu.stream()
			.filter(isVegetarian.negate())
			.collect(Collectors.toList()) );
		
		/*
		 * 6.4.1 분할의 장점
		 * 
		 * 분할 함수가 반환하는 참, 거짓 두 가지 요소의 스트림 리스트를 모두 유지한다는 것이 분할의 장점이다.
		 * 다음 코드에서는 컬렉터를 두 번째 인수로 전달할 수 있는 오버로드된 버전의 partitioningBy 메서드도 있다.
		 */
		System.out.println("6.4.1 분할의 장점 : " + menu.stream()
			.collect(Collectors.partitioningBy(Dish::isVegetarian
				, Collectors.groupingBy(Dish::getType)) ));
		
		/*
		 * 채식 요리와 채식이 아닌 요리 각각 그룹에서 칼로리가 높은 요리.
		 */
		System.out.println("6.4.1 분할의 장점 : " + menu.stream()
			.collect(Collectors.partitioningBy(Dish::isVegetarian
				, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)) ));
		
		/*
		 * 6.4.2 숫자를 소수와 비소수로 분할하기
		 * 
		 * 정수 n을 인수로 받아서 2에서 n까지의 자연수를 소수(prime)와 비소수(nonprime)로 나누는 코드를 구현하자.
		 */
		System.out.println("6.4.2 숫자를 소수와 비소수로 분할하기 : " + IntStream.rangeClosed(2, 100).boxed()
			.collect(Collectors.partitioningBy(candidate -> isPrime(candidate)) ));
		
		/*
		 * Collectors 클래스의 정적 팩토리 메서드
		 * 
		 * 팩토리 메서드 : toList
		 * 반환 형식 : List<T>
		 * 사용 예제 : 스트림의 모든 항목을 리스트로 수집.
		 * 활용 예 : List<Dish> dishes = menuStream.collect(Collectors.toList());
		 * 
		 * 팩토리 메서드 : toSet
		 * 반환 형식 : Set<T>
		 * 사용 예제 : 스트림의 모든 항목을 중복이 없는 집합으로 수집.
		 * 활용 예 : Set<Dish> dishes = menuStream.collect(Collectors.toSet());
		 * 
		 * 팩토리 메서드 : toCollection
		 * 반환 형식 : Collection<T>
		 * 사용 예제 : 스트림의 모든 항목을 공급자가 제공하는 컬렉션으로 수집.
		 * 활용 예 : Collection<Dish> dishes = menuStream.collect(Collectors.toCollection(), ArrayList::new);
		 * 
		 * 팩토리 메서드 : counting 
		 * 반환 형식 : Long
		 * 사용 예제 : 스트림의 항목 수 계산.
		 * 활용 예 : Long howManyDishes = menuStream.collect(Collectors.counting());
		 * 
		 * 팩토리 메서드 : summingInt 
		 * 반환 형식 : Integer
		 * 사용 예제 : 스트림의 항목에서 정수 프로퍼티값을 더함.
		 * 활용 예 : int totalCalories = menuStream.collect(Collectors.sumingInt(Dish::getCalories));
		 * 
		 * 팩토리 메서드 : averagingInt 
		 * 반환 형식 : Double
		 * 사용 예제 : 스트림 항목의 정수 프로퍼티의 평균값 계산.
		 * 활용 예 : double avgCalories = menuStream.collect(Collectors.averagingInt(Dish:getCalories));
		 * 
		 * 팩토리 메서드 : summarizingInt 
		 * 반환 형식 : IntSummaryStatistics
		 * 사용 예제 : 스트림 내의 항목의 최대값, 최소값, 합계 평균 등의 정수 정보 통계를 수집.
		 * 활용 예 : IntSummaryStatistics menuStatistics = menuStream.collect(Collectors.summarizingInt(Dish::getCalories));
		 * 
		 * 팩토리 메서드 : joining 
		 * 반환 형식 : String
		 * 사용 예제 : 스트림의 각 항목에 toString 메서드를 호출한 결과 문자열을 연결.
		 * 활용 예 : String shortMenu = menuStream.map(Dish::getName).collect(Collectors.joining(", "));
		 * 
		 * 팩토리 메서드 : maxBy 
		 * 반환 형식 : Optional<T>
		 * 사용 예제 : 주어진 비교자를 이용해서 스트림의 최대값 요소를 Optional로 감싼 값을 반환. 스트림에 요소가 없을 때는 Optional.empty()을 반환.
		 * 활용 예 : Optional<Dish> fattest = menuStream.collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)));
		 * 
		 * 팩토리 메서드 : minBy 
		 * 반환 형식 : Optional<T>
		 * 사용 예제 : 주어진 비교자를 이용해서 스트림의 최소값 요소를 Optional로 감싼 값을 반환. 스트림에 요소가 없을 때는 Optional.empty()을 반환.
		 * 활용 예 : Optional<Dish> fattest = menuStream.collect(Collectors.minBy(Comparator.comparingInt(Dish::getCalories)));
		 * 
		 * 팩토리 메서드 : reducing 
		 * 반환 형식 : 리듀싱 연산에서 형식을 결정
		 * 사용 예제 : 누적자를 초기값으로 설정한 다음에 BinaryOperator로 스트림의 각 요소를 반복적으로 누적자와 합쳐 스트림을 하나의 값으로 리듀싱.
		 * 활용 예 : int totalCalories = menuStream.collect(Collectors.reducing(0, Dish::getCalories, Integer::sum));
		 * 
		 * 팩토리 메서드 : collectingAndThen 
		 * 반환 형식 : 변환 함수가 형식을 결정
		 * 사용 예제 : 다른 컬렉터를 감싸고 그 결과에 변환 함수를 적용
		 * 활용 예 : int howManyDishes = menuStream.collect(Collectors.collectingAndThen(Collectors.toList(), List::size));
		 * 
		 * 팩토리 메서드 : groupingBy 
		 * 반환 형식 : Map<K, List<T>>
		 * 사용 예제 : 하나의 프로퍼티값을 기준으로 스트림의 항목을 그룹화하며 기준 프로퍼티 값을 결과 맵의 키로 사용.
		 * 활용 예 : Map<Dish.Type, List<Dish>> dishesByType = menuStream.collect(Collectors.groupingBy(Dish::getType));
		 * 
		 * 팩토리 메서드 : partitioningBy 
		 * 반환 형식 : Map<Boolean, List<T>>
		 * 사용 예제 : 프레디케이트를 스트림의 각 항목에 적용한 결과로 항목을 분할.
		 * 활용 예 : Map<Boolean, List<Dish>> vegetarianDishes = menuStream.collect(Collectors.partitioningBy(Dish::isVegetarian));
		 */
		
	}
	
	public static boolean isPrime(int candidate) {
		int candidateRoot = (int) Math.sqrt((double) candidate);
		
		return IntStream.rangeClosed(2,  candidateRoot)
			.noneMatch(i -> candidate % i == 0);
	}

}
