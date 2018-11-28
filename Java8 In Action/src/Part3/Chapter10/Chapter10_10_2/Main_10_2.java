package Part3.Chapter10.Chapter10_10_2;

import java.util.Optional;

/*
 * 10.2 Optional 클래스 소개
 * 
 * 자바 8은 하스켈과 스칼라의 영향을 받아 java.util.Optional<T>라는 새로운 클래스를 제공한다.
 * Optional은 선택형값을 캡슐화하는 클래스다.
 * 
 * 값이 있으면 Optional 클래스는 값을 감싼다. 반면 값이 없으면 Optional.empty 메서드로 Optional을 반환한다.
 * Optional.empty는 Optional의 특별한 싱글턴 인스턴스를 반환하는 정적 팩토리 메서드다.
 * 
 * null 레퍼런스와 Optional.empty()는 의미론상으론 둘이 비슷하지만 실제로는 차이점이 많다.
 * null을 참조하면 NullPointerException이 발생하지만 Optional.empty()는 Optional 객체이므로 이를 다양한 방식으로
 * 활용할 수 있다.
 */
public class Main_10_2 {

	public static void main(String[] args) {
		
	}
}

/*
 * Optinal 클래스를 사용하면서 모델의 의미(semantic)가 더 명확해졌음을 알수 있다.
 * 사람은 Optional<Car>를 참조하면서 차는 Optional<Insurace>를 참조하는데,
 * 이는 사람이 자동차가 있을수도 없을수도, 자동차는 보험이 있을수도 없을수도 있음을 명확히 설명한다.
 * 
 * 또한 보험회사 이름은 Optional<String>이 아니라 String 형식이므로 이는 보험회사는 반드시 이름을 가져야 함을 보여준다.
 * 따라서 보험회사 이름을 참조할 때 NullPointerException이 발생할 수도 있다는 정보를 확인할 수 있다.
 * 하지만 보험회사 이름이 null인지 확인하는 코드를 추가할 필요는 없다. 오히려 고쳐야 할 문제를 감추는 꼴이 되기 때문이다.
 * 보험회사는 이름을 반드시 가져야 하며 이름이 없는 보험회사를 발견했다면 예외를 처리하는 코드를 추가하는 것이 아니라
 * 보험회사 이름이 없는 이유가 무엇인지 밝혀서 문제를 해결해야 한다.
 * 
 * Optional을 이용하면 값이 없는 상황이 우리 데이터에 문제가 있는 것인지 아니면 알고리즘의 버그인지 명확하게 구분할 수 있다.
 * 모든 null 레퍼런스를 Optional로 대치하는 것은 바람직하지 않다.
 * Optional의 역할은 더 이해하기 쉬운 API를 설계하도록 돕는 것이다. 즉, 메서드의 시그너처만 보고도 선택형값인지 여부를 구별할 수 있다.
 * Optional이 등장하면 이를 언랩해서 값이 없을 수도 있는 상황에 적절하게 대응하도록 강제하는 효과가 있다.
 */
class Person {
	// 사람은 차가 있을수도 없을수도 있으므로 Optional로 정의한다.
	private Optional<Car>  car;
	
	public Optional<Car> getCar() {
		return this.car;
	}

}

class Car {
	// 자동차가 보험에 가입되어 있을수도 없을수도 있으므로 Optional로 정의한다.
	private Optional<Insurance> insurance;
	
	public Optional<Insurance> getInsurance() {
		return this.insurance;
	}
	
}

class Insurance {
	// 보험회사에는 반드시 이름이 있다.
	private String name;

	public String getName() {
		return this.name;
	}
}

