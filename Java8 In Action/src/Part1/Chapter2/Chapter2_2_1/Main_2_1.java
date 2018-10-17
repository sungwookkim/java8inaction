package Part1.Chapter2.Chapter2_2_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Part1.Chapter2.Chapter2_2_1.entity.AppleEntity;

/*
 * 2.1 변화하는 요구사항에 대응하기.
 * 농부가 재고목록 조사를 쉽게 할 수 있는 어플리케이션이 있다고 가정하자.
 */
public class Main_2_1 {

	/*
	 * 2.1.1 첫 번재 시도 : 녹색 사과 필터링.
	 * 농장 재고목록 어플리케이션 녹색 사과만 필터링 하는 기능을 추가한다.
	 * (해당 코드는 갑님의 변심이 생기기 전에 쓰던 코드 이므로 사용하는 곳이 없다.)
	 */
	public static List<AppleEntity> filterGreenApples(List<AppleEntity> inventory) {
		List<AppleEntity> result = new ArrayList<>();
		
		for(AppleEntity apple : inventory) {
			if("green".equals(apple.getColor()) ) {
				result.add(apple);
			}
		}
		
		return result;
	}
	
	/*
	 * 2.1.2 두 번재 시도 : 색을 파라미터화
	 * 그런데 우리의 갑님께서의 변심으로 녹색 사과 말고 빨간 사과도 필터링하고 싶어졌다 하셨다.
	 * 다양항 색의 사과 필터링을 위해 아래와 같이 메서드를 만들었다.
	 */
	public static List<AppleEntity> filterAppleByColor(List<AppleEntity> inventory, String color) {
		List<AppleEntity> result = new ArrayList<>();
		
		for(AppleEntity apple : inventory) {
			if(apple.getColor().equals(color)) {
				result.add(apple);
			}
		}
		
		return result;
	}
	
	/*
	 * 갑님의 계속된 요구사항을 들어보니 색과 마찬가지로 무게 기준도 얼마든지 바뀔 수 있을거 같아
	 * 아래와 같이 다양한 무게에 대응할 수 있도록 무게 정보 파라미터도 추가한 메서드도 만들었다.
	 * 
	 * 아래 메서드가 좋은 해결책이라 할 수 있지만 구현 코드를 보면 목록을 검색하고, 각 사과에 
	 * 필터링 조건을 적용하는 부분의 코드가 색 필터링 코드와 대부분 중복된다는 사실을 확인했다.
	 * 이는 소프트웨어 공학의 DRY(don't repeat yourself - 같은 것을 반복하지 말 것)원칙을 어기는 것이다.
	 * 
	 * 탐색 과정을 고쳐서 성능을 개선하려 한다면? 그러면 코드 한 줄이 아닌 메서드 전체 구현을 고쳐야 한다.
	 * 즉, 엔지니어링적으로 비싼 대가를 치러야 한다.
	 */
	public static List<AppleEntity> filterAppleByWeight(List<AppleEntity> inventory, int weight) {
		List<AppleEntity> result = new ArrayList<>();
		
		for(AppleEntity apple : inventory) {
			if(apple.getWeight() > weight) {
				result.add(apple);
			}
		}
		
		return result;
	}
	
	/*
	 * 비싼 대가를 치루지 않기 위해 색과 무게를 filter라는 메서드로 합치는 방법을 선택 했다.
	 * 해당 메서드는 색이나 무게 중 어떤 것을 기준으로 필터링할지 가라키는 플래그를 추가 했다.
	 * 
	 * 그러나 형편 없는 코드이다..
	 * 대체 true와 false가 뭘 의미 하는지 알 수 없을 뿐더러 여기서 좀 더 다양한 요구사항이 생겼을시에
	 * 여러 중복된 필터 메서드를 만들거나 모든 것을 처리하는 거대한 하나의 필터 메서드를 구현해야 한다.
	 * 지금까지 사용한 문자열, 정수, 불린 등의 값으로 filterApples 메서드를 파라미터화 했다면 문제가 잘 정의되어 있는 상황에서의
	 * 이 방법이 잘 동작할 수 있다.
	 */
	public static List<AppleEntity> filterApples(List<AppleEntity> inventory, String color, int weight, boolean flag) {
		List<AppleEntity> result = new ArrayList<>();
		
		for(AppleEntity apple : inventory) {
			if((flag && apple.getColor().equals(color)) 
				|| (!flag && apple.getWeight() > weight)) {
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
		 * 2.1.2의 요구사항에 맞춰 만들었기 때문에 갑님도 만족해 할것이다.(애증의 갑님..)
		 */
		// 색깔 필터링.
		filterAppleByColor(inventory, "red").stream()
			.forEach((apple) -> System.out.println("[filterAppleByColor] color : " + apple.getColor() + ", weight : " + apple.getWeight()) );
		
		filterAppleByColor(inventory, "green").stream()
			.forEach((apple) -> System.out.println("[filterAppleByColor] color : " + apple.getColor() + ", weight : " + apple.getWeight()) );
		
		// 무게 필터링.
		filterAppleByWeight(inventory, 90).stream()
			.forEach((apple) -> System.out.println("[filterAppleByWeight] weight = 90, color : " + apple.getColor() + ", weight : " + apple.getWeight()) );
		
		filterAppleByWeight(inventory, 100).stream()
			.forEach((apple) -> System.out.println("[filterAppleByWeight] weight = 100, color : " + apple.getColor() + ", weight : " + apple.getWeight()) );
		
		// 색깔/무게 필터링.
		filterApples(inventory, "", 110, false).stream()
			.forEach((apple) -> System.out.println("[filterApples] weight filter = 110, color : " + apple.getColor() + ", weight : " + apple.getWeight()) );
		
		filterApples(inventory, "green", 0, true).stream()
			.forEach((apple) -> System.out.println("[filterApples] color filter = " + apple.getColor() + ", color : " + apple.getColor() + ", weight : " + apple.getWeight()) );
	}

}
