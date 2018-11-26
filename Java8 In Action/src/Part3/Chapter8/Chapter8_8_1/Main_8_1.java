package Part3.Chapter8.Chapter8_8_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Part3.Chapter8.Chapter8_8_1.entity.Dish;
import Part3.Chapter8.Chapter8_8_1.task.Task;

/*
 * 8.1 가독성과 유연성을 개선하는 리팩토링
 * 
 * 람다 표현식은 익명 클래스보다 코드를 좀 더 간결하게 만든다.
 * 동작 파라미터화의 형식을 지원하므로 람다 표현식을 이용한 코드는 더 큰 유연성을 갖출 수 있다.
 * 즉, 람다 표현식을 이용한 코드는 다양한 요구사항 변화에 대응할 수 있도록 파라미터화한다.
 */
public class Main_8_1 {
	
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
		 * 8.1.1 코드 가독성 개선
		 * 
		 * 일반적으로 코드 가독성이 좋다는 것은 '어떤 코드를 다른 사람도 쉽게 이해할 수 있음'을 의미한다.
		 * 즉, 코드 가독성을 개선한다는 것은 우리가 구현한 코드를 다른 사람이 쉽게 이해하고 유지보수할 수 있게
		 * 만드는 것을 의미한다.
		 * 코드 가독성을 높이려면 코드의 문서화를 잘하고, 표준 코딩 규칙을 준수하는 등의 노력을 기울여야 한다.
		 * 
		 * 자바 8에서는 코드 가독성에 도움을 주는 다음과 같은 기능을 새롭게 제공한다.
		 * 		- 코드의 장황함을 줄여 쉽게 이해할 수 있는 코드를 구현할 수 있다.
		 * 		- 메서드 레퍼런스의 스트림 API를 이용해서 코드의 의도(무엇을 수행하려는 것인지)를 쉽게 표현할 수 있다.
		 * 
		 * 8.1.2 익명 클래스를 람다 표현식으로 리팩토링하기
		 * 
		 * 하나의 추상 메서드를 구현하는 익명 클래스는 람다 표현식으로 리팩토링할 수 있다.
		 * 익명 클래스는 코드를 장황하게 만들고 쉽게 에러를 일으킨다.
		 * 람다 표현식을 이용해서 간결하고, 가독성이 좋은 코드를 구현할 수 있다.
		 * 
		 * - 익명 클래스를 사용한 이전 코드
		 * Runnable r1 = new Runnable() {
		 * 		public void run() {
		 * 			System.out.println("Hello");
		 * 		}
		 * }
		 * 
		 * - 람다 표현식을 사용한 최신 코드
		 * Runnable r2 = () -> System.out.println("Hello!!");
		 * 
		 * 하지만 모든 익명 클래스를 람다 표현식으로 변환할 수 있는것은 아니다.
		 * 		1. 익명 클래스에서 사용한 this와 super는 람다 표현식에서 다른 의미를 갖는다.
		 * 		익명 클래스에서 this는 익명 클래스 자신을 가리키지만 람다에서 this는 람다를 감싸는 클래스를 가리킨다.
		 * 		2. 익명 클래스는 감싸고 있는 클래스의 변수를 가릴 수 있다(섀도 변수-shadow variable).
		 * 		하지만 다음 코드에서 처럼 람다 표현식으로는 변수를 가릴 수 없다.
		 * 
		 * 		int a = 10;
		 * 		Runnable r1 = () -> {
		 * 			int a = 2; // 컴파일 에러.
		 * 			System.out.println(a);
		 * 		}
		 * 
		 * 		Runnabel r2 =  new Runnable() { 
		 * 			public void run() {
		 * 				int a = 2; // 모든 것이 잘 작동한다.
		 * 				System.out.println(a);
		 * 			}
		 * 		}
		 * 익명 클래스를 람다 표현식으로 바꾸면 콘텍스트 오버로딩에 따른 모호함이 초래될 수 있다.
		 * 익명 클래스는 인스턴스화할 때 명시적으로 형식이 정해지는 반명 람다의 형식은 콘텍스트에 따라 달라지기 때문이다.
		 */
		
		// Task를 구현하는 익명 클래스르 전달할 수 있다.
		Task.doSomthing(new Task() {			
			@Override
			public void execute() {
				System.out.println("익명 클래스 - Task Danger danger!!!");
			}
		});
		
		Task.doSomthing(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("익명 클래스 - Runabel Danger danber!!");
			}
		}); 

		/*
		 * 위 익명 클래스를 람다 표현식으로 바꾸면 메서드를 호출할 때 Runnable와 Task 모두 대상 형식이 될 수 있으므로 모호함이 발생한다. 
		 */		
		// 모호함 때문에 아래 주석을 해제하면 에러가 발생한다. 
		// Task.doSomthing(() -> System.out.println("Danger danger"));
		
		// 명시적 형변환을 이용해서 모호함을 제거할 수 있다.		
		Task.doSomthing((Task)() -> System.out.println("람다 표현식 - Task Danger danger"));		
		Task.doSomthing((Runnable)() -> System.out.println("람다 표현식 - Runnable Danger danger"));
		
		/*
		 * 8.1.3 람다 표현식을 메서드 레퍼런스로 리팩토링하기
		 * 
		 * 람다 표현식 대신 메서드 레퍼런스를 이용하면 가독성을 높일 수 있다.
		 * 메서드 레퍼런스의 메서드명으로 코드의 의도를 명확하게 알릴 수 있기 때문이다.
		 */
		Map<Dish.CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream()
			.collect(Collectors.groupingBy(dish -> {
				if(dish.getCalories() <= 400) {
					return Dish.CaloricLevel.DIET;
				} else if(dish.getCalories() <= 700) {
					return Dish.CaloricLevel.NORMAL;
				} else {
					return Dish.CaloricLevel.FAT;
				}
			}));
		
		System.out.println("8.1.3 람다 표현식을 메서드 레퍼런스로 리팩토링하기 - 람다 표현식 : " + dishesByCaloricLevel);
		
		/*
		 * 람다 표현식을 별도의 메서드로 추출한 다음에 groupingBy에 인수로 전달할 수 있다.
		 */
		dishesByCaloricLevel = menu.stream()
			.collect(Collectors.groupingBy(Dish::getCaloricLevel));

		System.out.println("8.1.3 람다 표현식을 메서드 레퍼런스로 리팩토링하기 - 메서드 레퍼런스 : " + dishesByCaloricLevel);
		
		/*
		 * sum, maximum 등 자주 사용하는 리듀싱 연산은 메서드 레퍼런스와 함께 사용할 수 있는 내장 헬퍼 메서드를 제공한다.
		 * 최대값이나 합계를 계산할 때 람다 표현식과 저수준 리듀싱 연산을 조합하는것 보다 Collectors API를 사용하면 코드의 의도가
		 * 더 명확해진다.
		 */
		
		// 저수준 리듀싱 조합 코드.
		int totalCalories = menu.stream()
				.map(Dish::getCalories)
				.reduce(0, (c1, c2) -> c1 + c2);
		
		System.out.println("8.1.3 람다 표현식을 메서드 레퍼런스로 리팩토링하기 - 저수준 리듀싱 조합 : " + totalCalories);

		// Collectors API 코드.
		totalCalories = menu.stream()
			.collect(Collectors.summingInt(Dish::getCalories));
		
		System.out.println("8.1.3 람다 표현식을 메서드 레퍼런스로 리팩토링하기 - Collectors API : " + totalCalories);
		
		/*
		 * 8.1.4 명령형 데이터 처리를 스트림으로 리팩토링하기
		 * 
		 * 이론적으로는 반복자를 이용한 기존의 모든 컬렉션 처리 코드를 스트림 API로 바꿔야 한다.
		 * 스트림 API는 데이터 처리 파이프라인의 의도를 더 명확하게 보여주고 쇼트서킷과 게으름이라는
		 * 강력한 최적화뿐 아니라 멀티코어 아키텍처를 활용할 수 있는 지름길을 제공한다.
		 */
		
		/*
		 * 아래 명령형 코드는 필터링과 추출이란 두 가지 패턴으로 엉킨 코드다.
		 * 이런 코드를 접한 다른 개발자는 전체 구현을 자세히 살펴본 이후에야 전체 코드의 의도를 파악할 수 있을것이다.
		 */
		List<String> dishNames = new ArrayList<>();
		for(Dish dish : menu) {
			if(dish.getCalories() > 300) {
				dishNames.add(dish.getName());
			}
		}
		
		System.out.println("8.1.4 명령형 데이터 처리를 스트림으로 리팩토링하기 - 명령형 코드 : " + dishNames);
		
		/*
		 * 스트림 API를 이용하면 문제를 좀 더 직접적으로 기술할 수 있을 뿐 아니라 쉽게 병렬화할 수 있다.
		 */
		dishNames = menu.stream()
			.filter((dish) -> dish.getCalories() > 300)
			.map(Dish::getName)
			.collect(Collectors.toList());
		
		System.out.println("8.1.4 명령형 데이터 처리를 스트림으로 리팩토링하기 - 스트림 코드 : " + dishNames);
		
		/*
		 * 8.1.5 코드 유연성 개선
		 * 
		 * 람다 표현식을 이용하면 "동작 파라미터화(behaviour parameterization)"를 쉽게 구현할 수 있다.
		 * 즉, 다양한 람다를 전달해서 다양한 동작을 표현할 수 있다. 따라서 변화하는 요구사항에 대응할 수 있는 코드를
		 * 구현할 수 있다. 
		 */
	}

}
