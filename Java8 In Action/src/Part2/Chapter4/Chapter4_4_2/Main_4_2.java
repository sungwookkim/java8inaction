package Part2.Chapter4.Chapter4_4_2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Part2.Chapter4.Chapter4_4_1.entity.Dish;

/*
 * 4.2 스트림 시작하기
 * 
 * 스트림이란 "데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소"로 정의할 수 있다.
 * 
 * 연속된 요소
 * 컬렉션과 마찬가지로 스트림은 특정 요소 형식으로 이루어진 연속된 값 집합의 인터페이스를 제공한다.
 * 컬렉션은 자료구조이므로 컬렉션에서는 시간과 공간의 복잡성과 관련된 요소 저장 및 접근 연산이 주를 이룬다.
 * 반면 스트림은 filter, sorted, map처럼 표현 계산식이 주를 이룬다.
 * 즉, 컬렉션의 주제는 데이터고 스트림의 주제는 계산이다.
 * 
 * 소스
 * 스트림은 컬렉션, 배열, I/O 자원 등의 데이터 제공 소스로부터 데이터를 소비(consume)한다.
 * 정렬된 컬렉션으로 스트림을 생성하면 정렬이 그대로 유지된다.
 * 즉 리스트로 스트림을 만들면 스트림의 요소는 리스트의 요소와 같은 순서를 유지한다.
 * 
 * 데이터 처리 연산
 * 스트림은 함수형 프로그래밍 언어에서 일반적으로 지원하는 연산과 데이터베이스와 비슷한 연산을 지원한다.
 * 예를 들어 filter, map, reduce, find, match, sort 등으로 데이터를 조작할 수 있다.
 * 스트림 연산은 순차적으로 또는 병렬로 실행할 수 있다. 
 */
public class Main_4_2 {

	static public void main(String[] args) {
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
		 * 또한 스트림은 다음과 같은 두 가지 중요한 특징을 갖는다.	 
		 * 
		 * 파이프라이닝
		 * 대부분 스트림 연산은 스트림 연산끼리 연결해서 커다란 파이프라인을 구성할 수 있도록 스트림 자신을 반환한다.
		 * 그 덕분에 게으름(laziness), 쇼트서킷(short-circuiting - 단선)같은 최적화도 얻을 수 있다.
		 * 연산 파이프라인은 데이터 소스에 적용하는 데이터베이스 질의와 비슷하다.
		 * 
		 * 내부 반복
		 * 반복자를 이용해서 명시적으로 반복하는 컬렉션과 달리 스트림은 내부 반복을 지원한다.
		 */
		System.out.println("내부 반복 : " + menu.stream()
				.filter((d) -> d.getCalories() > 300)
				.map(Dish::getName)
				.limit(3)
				.collect(Collectors.toList() ));
		/*
		 * menu에 stream 메서드를 호출해서 요리 리스트로부터 스트림을 얻었다.
		 * 		1. 여기서 "데이터 소스"는 요리 리스트(menu 변수)다.
		 * 		2. 데이터 소스는 "연속된 요소"를 스트림에 제공한다.
		 * 		3. 스트림에 filter, map, limit, collect로 이어지는 일련의 "데이터 처리 연산"을 적용한다.
		 * 		4. collect를 제외한 모든 연산은 서로 "파이프라인"을 형성할 수 있도록 스트림을 반환한다.
		 * 
		 * 파이프라인은 소스에 적용하는 질의 같은 존재다.
		 * 마지막으로 collect 연산으로 파이프라인을 처리해서 결과를 반환한다.
		 * (collect는 스트림이 아니라 List를 반환한다.)
		 * 마지막에 collect를 호출하기 전까지는 menu에서 아무것도 선택되지 않으며 출력 결과도 없다.
		 * 즉, collect가 호출되기 전까지 메서드 호출이 저장되는 효과가 있다.
		 * 
		 * 아래는 일련의 스트림 연산이 적용되는 모습을 보여준다. filter, map, limit, collect는 각각 다음 작업을 수행한다.
		 * (아래 순서는 위 코드 기준으로 한다.) 
		 * filter
		 * 		람다를 인수로 받아 스트림에서 특정 요소를 제외시킨다.
		 * map
		 * 		람다를 이용해서 요소를 다른 요소로 변환하거나 정보를 추출한다.
		 * limit
		 * 		정해진 개수 이상의 요소가 스트림에 저장되지 못하게 스트림 크기를 축소(truncate)한다.
		 * collect
		 * 		스트림을 다른 형식으로 변환한다.
		 * 
		 * 위 코드 "3개의 고칼로리 요리명을 찾아라"처럼 좀 더 선언형으로 데이터를 처리할 수 있다.
		 * 스트림 라이브러리에서 필터링(filter), 추출(map), 축소(limit)기능을 제공하므로 직접 이 기능을
		 * 구현할 필요가 없다.
		 * 결과적으로 스트림 API는 파이프라인을 더 최적화할 수 있는 유연성을 제공한다.
		 */
	}
}
