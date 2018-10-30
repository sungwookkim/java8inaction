package Part2.Chapter4.Chapter4_4_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Part2.Chapter4.Chapter4_4_3.entiity.Dish;

/*
 * 4.3 스트림과 컬렉션
 * 
 *  자바의 기존 컬렉션과 새로운 스트림은 모두 연속된 형식의 값을 저장하는 자료구조의 인터페이스를 제공한다.
 *  여기서 "연속된(sequenced)"이라는 표현은 순서와 상관없이 아무 값에나 접속하는 것이 아니라
 *  순차적으로 값에 접근한다는 것을 의미한다.
 *  
 *  스트림과 컬렉션의 큰 차이는 데이터를 언제 계산하느냐이다.
 *  컬렉션은 현재 자료구조가 포함하는 모든 값을 메모리에 저장하는 자료구조다.
 *  즉, 컬렉션의 모든 요소는 컬렉션에 추가하기 전에 계산되어야 한다.
 *  (요소를 추가 혹은 삭제할 수 있다. 이런 연산을 수행할 때마다 컬렉션의 모든 요소를 메모리에 저장해야 하며
 *  컬렉션에 추가하려는 요소는 미리 계산되어야 한다.)
 *  
 *  스트림은 이론적으로 "요청할 때만 요소를 계산"하는 고정된 자료구조다.
 *  (스트림에 요소를 추가하거나 요소를 제거할 수 없다.)
 *  결과적으로 스트림은 생산자(producer)와 소비자(consumer)관계를 형성한다.
 *  또한 스트림은 게으르게 만들어지는 컬렉션과 같다.
 *  즉, 사용자가 데이터를 요청할 때만 값을 계산한다
 *  (경영학에서는 이를 요청 중심 제조[demand-driven manufacturing]) 또는 즉석 제조[just-in-time manufacturing]라고 부른다.)
 *  반면 컬렉션은 적극적으로 생성된다.
 *  (생산자 중심[supplier-driven:팔기도 전에 창고를 가득 채움])
 *  
 *  컬렉션은 DVD에 비유할 수 있고, 스트림은 인터넷 스트리밍에 비유할 수 있다.
 *  DVD로 비디오를 보기 위해서는 DVD에 모든 파일들이 로드 될 때까지 기달려야 하지만
 *  인터넷 스트리밍은 현재 받은 바이너리로 비디오를 볼 수 있다.  
 */
public class Main_4_3 {

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
		 * 4.3.1 딱 한 번만 탐색할 수 있다!
		 * 
		 * 반복자와 마찬가지로 스트림도 한 번만 탐색할 수 있다.
		 * 즉, 탐색된 스트림의 요소는 소비(consume)된다.
		 * 한 번 탐색한 요소를 다시 탐색하려면 초기 데이터 소스에서 새로운 스트림을 만들어야 한다.
		 * (그러려면 컬렉션처럼 반복 사용할 수 있는 데이터 소스여야 한다.
		 * 만일 데이터 소스가 I/O 채널이라면 소스를 반복 사용할 수 없으므로 새로운 스트림을 만들 수 없다.)
		 */
		List<String> title = Arrays.asList("Java8", "In", "Action");
		Stream<String> s = title.stream();
		s.forEach(System.out::println);
		// 스트림이 이미 소비되었거나 닫혔기 때문에 에러가 발생한다.
		// s.forEach(System.out::println);
		
		/*
		 * 4.3.2 외부 반복과 내부 반복
		 * 
		 * 컬렉션 인터페이스를 사용하려면 사용자가 직접 요소를 반복해야 한다.(예를 들어 for-each등을 사용)
		 * 이를 외부 반복(external iteration)이라고 한다.
		 * 반면 스트림 라이브러리는 내부 반복(internal iteration)을 사용한다.
		 * (반복을 알아서 처리하고 결과 스트림값을 어딘가에 저장한다.) 
		 */
		// 컬렉션 : for-each 루프를 이용하는 외부 반복
		List<String> names = new ArrayList<>();
		for(Dish d : menu) {
			names.add(d.getName());
		}
		names.stream().forEach((name) -> System.out.println("컬렉션 : for-each 루프를 이용하는 외부 반복 : " + name));

		/*
		 * for-each 구문은 반복자를 사용하는 불편함을 어느 정도 해결해준다.
		 * for-each를 이용하면 Iteratorr 객체를 이용하는 것보다 더 쉽게 컬렉션을 반복할 수 있다. 
		 */

		// 컬렉션 : 내부적으로 숨겨졌던 반복자를 사용한 외부 반복(Iteratorr 이용)
		names = new ArrayList<>();
		Iterator<Dish> iterator = menu.iterator();
		
		while(iterator.hasNext()) {
			Dish d = iterator.next();
			names.add(d.getName());
		}
		names.stream().forEach((name) -> System.out.println("컬렉션 : 내부적으로 숨겨졌던 반복자를 사용한 외부 반복 : " + name));
		
		// 스트림 : 내부 반복
		names = menu.stream()
			// map 메서드를 getName 메서드로 파라미터화해서 요리명을 추출.
			.map(Dish::getName)
			// 파이프라인을 실행한다. 반복자는 필요 없다.
			.collect(Collectors.toList());
		names.stream().forEach((name) -> System.out.println("스트림 : 내부 반복 : " + name));
		
		/*
		 * 컬렉션은 "외부적으로" 반복, 즉 명시적으로 컬렉션의 항목을 하나씩 가져와서 처리한다.
		 * 그러나 스트림의 내부 반복을 이용하면 작업을 투명하게 병렬로 처리하거나 더 최적화된 다양한 순서로 처리할 수 있다.
		 * 스트림 라이브러리의 내부 반복은 데이터 표현과 하드웨어를 활용한 병렬성 구현을 자동으로 선택한다.
		 * 반면 for-each를 이용하는 외부 반복에서는 병렬성을 스스로 관리해야 한다.
		 * (병렬성을 "스스로 관리"한다는 것은 병렬성을 포기하던가 synchronized 사용함을 뜻한다.) 
		 */
	}
}
