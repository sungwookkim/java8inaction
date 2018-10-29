package Part1.Chapter2.Chapter2_2_4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import Part1.Chapter2.Chapter2_2_4.entity.AppleEntity;

public class Main_2_4 {
	/*
	 * 2.4.1 Comparator로 정렬하기.
	 * 
	 * 우리의 갑님 농부님께서 무게를 기준으로 목록에서 정렬하고 싶다고 하셨다.
	 * 그러나 갈대 같은 마음을 가진 농부님께서는 색을 기준으로 사과를 정렬하고 싶다고 하실거 같기도 하다.
	 * 일상에서 흔히 일어나는 일이지 않는가?(....뒤..ㅈ...)
	 * 
	 * 그러나 걱정하지 말아라 자바에서는 ㅓㅁㅍㄻ.ㅕ샤ㅣ.채ㅡㅔㅁㄱㅁ색(음?;; 해석해보시오) 객체를 이용해서 sort의
	 * 동작을 파라미터화할 수 있다. 
	 */	
	static void appleCompare(List<AppleEntity> inventory) {
		/*
		 * 아래와 같이 Comparator를 구현해서 sort 메서드의 동작을 다양화할 수 있다.
		 * 아래 코드는 익명 클래스를 이용해서 무게가 적은 순으로 목록에서 사과를 정렬한다.
		 */
		inventory.sort(new Comparator<AppleEntity>() {

			@Override
			public int compare(AppleEntity o1, AppleEntity o2) {
				return o1.getWeight().compareTo(o2.getWeight());
			}			
		});
		
		inventory.stream()
			.forEach((AppleEntity apple) -> System.out.println("[Anonymous Class - Ascending] color : " + apple.getColor() 
				+ ", weight : " + apple.getWeight() ));

		/*
		 * 아래 코드는 람다표현식으로 무게가 큰 순으로 목록에서 사과를 정렬한다. 
		 */
		inventory.sort((AppleEntity a1, AppleEntity a2) -> a2.getWeight().compareTo(a1.getWeight()) );
		
		inventory.stream()
			.forEach((AppleEntity apple) -> System.out.println("[Lambda - Descending] color : " + apple.getColor() 
				+ ", weight : " + apple.getWeight() ));		
	}
	
	/*
	 * 2.4.2 Runnable로 코드 블록 실행하기
	 * 
	 * 자신만의 코드 블록을 수행한다는 점에서 스레드 동작은 경량 프로세스와 비슷하다.
	 * 각각의 스레드는 각기 다른 코드를 실행할 수 있다.
	 * 나중에 실행될 코드 조각을 어떻게 표현할 수 있는가가 문제다.
	 * 
	 * 자바에서는 Runnable 인터페이스를 이용해서 실행할 코드 블록을 지정할 수 있다.
	 */
	static void runnable() {
		// 익명 클래스.
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				/*
				 * for(int i = 0; i < 100; i++) {
				 * 		System.out.println("Anonymous Class [" + i + "] hello gab nim");
				 * }
				 * 를 람다 형식으로 하면 아래와 같다.
				 */
				IntStream.range(0, 100).forEach((int i) -> System.out.println("Anonymous Class [" + i + "] hello gab nim") );				
			}
		}).start();
		
		// 람다 표현식.
		new Thread(() -> IntStream.range(0, 100).forEach((int i) -> System.out.println("Lambda [" + i + "] hello gab nim") )).start();
	}
	static public void main(String[] args) {
		// 2.4.1 Comparator로 정렬하기.
		appleCompare(Arrays.asList(new AppleEntity("green", 90)
				, new AppleEntity("green", 150)
				, new AppleEntity("red", 120)));		
		
		// 2.4.2 Runnable로 코드 블록 실행하기.
		runnable();
		
	}
}
