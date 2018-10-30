package Part2.Chapter4.Chapter4_4_4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Part2.Chapter4.Chapter4_4_4.entity.Dish;

/*
 * 4.4 스트림 연산
 * 
 * 스트림 인터페이스의 연산을 크게 두 가지로 구분할 수 있다.
 */
public class Main_4_4 {

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
		 * 아래 스트림은 연산을 두 그룹으로 구분할 수 있다.
		 * filter, map, limit는 서로 연결되어 파이프라인을 형성한다.
		 * collect로 파이프라인을 실행한 다음에 닫는다.
		 * 
		 * 연결할 수 있는 스트림 연산을 중간 연산(intermediate operation)이라고 하며,
		 * 스트림을 닫는 연산을 최종 연산(terminal operation)이라고 한다.
		 */
		System.out.println(menu
			// 요리 리스트에서 스트림 얻기
			.stream()
			// 중간 연산
			.filter((d) -> d.getCalories() > 300)
			// 중간 연산
			.map(Dish::getName)
			// 중간 연산
			.limit(3)
			// 최종 연산(스트림을 리스트로 변환)
			.collect(Collectors.toList() ));
		
		/*
		 * 4.4.1 중간 연산
		 * 
		 * filter나 sorted 같은 중간 연산은 다른 스트림을 반환한다.
		 * 따라서 여러 중간 연산을 연결해서 질의를 만들 수 있다.
		 * 중간 연산의 중요한 특징은 단말 연산을 스트림 파이프라인에 실행하기 전까지는
		 * 아무 연산도 수행하지 않는다는 것, 즉 게으르다(lazy)는 것이다.
		 * 중간 연산을 합친 다음에 합쳐진 중간 연산을 최종 연산으로 한 번에 처리하기 때문이다.
		 * 
		 * 스트림의 게으른 특성 덕분에 몇 가지 최적화 효과를 얻을 수 있었다.
		 * 		1. 300칼로리가 넘는 요리는 여러 개지만 오직 처음 3개만 선택되었다.
		 * 		이는 limit 연산 그리고 "쇼트서킷"이라 불리는 기법 덕분이다.
		 * 		2. filter와 map은 서로 다른 연산이지만 한 과정으로 병합되었다.
		 * 		이 기법을 루프 퓨전(loop fusion)이라고 한다. 
		 */
		System.out.println(menu.stream()
			.filter((d) -> {
				System.out.println("filtering : " + d.getName());
				return d.getCalories() > 300;	
			})
			.map((d) -> {
				System.out.println("mapping : " + d.getName());
				return d.getName();
			})
			.limit(3)
			.collect(Collectors.toList() ));
		
		/*
		 * 4.4.2 최종 연산
		 * 
		 * 최종 연산은 스트림 파이프라인에서 결과를 도출한다. 보통 최종 연산에 의해
		 * List, Integer, void 등 스트림 이외의 결과가 반환된다.
		 */
		// 파이프라인에서 forEach는 소스의 각 요리에 람다를 적용한 다음에 void를 반환하는 최종 연산이다.
		menu.stream().forEach(System.out::println);
		
		/*
		 * 4.4.3 스트림 이용하기
		 * 
		 * - 질의를 수행할(컬렉션 같은) "데이터 소스"
		 * - 스트림 파이프라인을 구성할 "중간 연산" 연결
		 * - 스트림 파이프라인을 실행하고 결과를 만들 "최종 연산"
		 * 
		 * 스트림 파이프라인의 개념은 빌더 패턴(builder pattern)과 비슷한다.
		 * 빌드 패턴에서는 호출을 연결해 설정을 만든다(중간 연산을 연결하는 것과 같다.)
		 * 그리고 준비된 설정에 build 메서드를 호출한다.(최종 연산에 해당함)
		 * 
		 * 중간연산
		 * 연산 : filter
		 * 형식 : 중간 연산
		 * 반환 형식 : Stream<T>
		 * 연산의 인수 : Prdeicate<T>
		 * 함수 디스크립터 : T -> boolean
		 * 
		 * 연산 : map
		 * 형식 : 중간 연산
		 * 반환 형식 : Stream<T> 
		 * 연산의 인수 : Function<T,R> 
		 * 함수 디스크립터 :T -> R
		 *
 		 * 연산 : limit
		 * 형식 : 중간 연산
		 * 반환 형식 : Stream<T> 
		 * 연산의 인수 :  
		 * 함수 디스크립터 :
		 * 
		 * 연산 : sorted
		 * 형식 : 중간 연산
		 * 반환 형식 : Stream<T> 
		 * 연산의 인수 : Comparator<T> 
		 * 함수 디스크립터 : (T, T) -> int 
		 * 
		 * 연산 : distinct
		 * 형식 : 중간 연산
		 * 반환 형식 : Stream<T> 
		 * 연산의 인수 :  
		 * 함수 디스크립터 : 
		 * 
		 * 최종 연산
		 * 연산 : forEach 
		 * 형식 : 최종 연산
		 * 목적 : 스트림의 각 요소를 소비하면서 람다를 적용한다. void 반환.
		 * 
		 * 연산 : count 
		 * 형식 : 최종 연산
		 * 목적 : 스트림의 요소 개수를 반환한다. long을 반환.
		 * 
		 * 연산 : collect 
		 * 형식 : 최종 연산
		 * 목적 : 스트림을 리듀스해서 리스트, 맵, 정수 형식의 컬렉션을 만든다.
		 */

	}

}
