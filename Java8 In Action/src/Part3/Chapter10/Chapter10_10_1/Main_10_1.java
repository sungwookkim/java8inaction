package Part3.Chapter10.Chapter10_10_1;

import Part3.Chapter10.Chapter10_10_1.entity.Car;
import Part3.Chapter10.Chapter10_10_1.entity.Insurance;
import Part3.Chapter10.Chapter10_10_1.entity.Person;

/*
 * 10.1 값이 없는 상황을 어떻게 처리할까?
 */
public class Main_10_1 {

	/*
	 * 코드에 아무 문제가 없는 것처럼 보이지만 몇 가지 문제가 있다.
	 * Person객체가 null일수도 있고 pserson의 Car객체 null일수도 있으며
	 * Car객체의 Insurance객체 마찬가지로 null일수도 있다.
	 * 그러므로 총 3개의 객체가 null 일수도 있는 상황이 생긴다.
	 * 이 경우라면 런타임에 NullPointerException이 발생되면서 프로그램이 중단될수도 있다.   
	 */
	public String getCarInsuranceName(Person person) {
		return person.getCar().getInsurance().getName();
	}
	
	/*
	 * 10.1.1 보수적인 자세로 NullPointerException 줄이기
	 * 
	 * 변수를 참조할 때마다 null을 확인하며 중간 과정에 하나라도 null이 존재하면 "Unknown" 문자열을 반환한다.
	 * 상식적으로 모든 회사에는 이름이 있으므로 보험회사의 이름이 null인지는 확인하지 않았다.
	 * 우리가 확실히 알고 있는 영역을 모델링할 때는 이런 지식을 활용해서 null 확인을 생략할 수 있지만,
	 * 데이터를 자바 클래스로 모델링할 때는 이 같은(모든 회사는 반드시 이름을 갖는다) 사실을 단정하기 어렵다.
	 * 
	 * 모든 변수가 null인지 의미하므로 변수를 접근할 때마다 중첩된 if가 추가되면서 코드 들여쓰기 수준이 증가한다.
	 * 이와 같은 반복패턴(recurring pattern)코드를 "깊은 의심-deep doubt"이라고 부른다.
	 */
	public String getNullCheckCarInsuranceName(Person person) {
		if(person != null) {
			
			Car car = person.getCar();
			if(car != null) {
				
				Insurance insurance = car.getInsurance();
				if(insurance != null) {
					return insurance.getName();
				}
			}
		}
		
		return "Unknown";
	}
	
	/*
	 * 위 메서드와 다르겐 방법으로 중첩 if 블록을 없앴다.
	 * 
	 * 하지만 이 코드도 좋은 코드는 아니다. 메서드에 네 개의 출구가 생겼기 때문이다.
	 * 출구가 많아지면 유지보수 하기가 어려워고 게다가 null일 때 반환 되는 기본값 "Unknown"이
	 * 세 곳에서 반복되고 있다. 이 같은 경우는 문자열을 상수로 만들어서 이 문제를 해결할 수 있다.
	 */
	public String getNullcheckCarInsuranceName2(Person person) {
		if(person == null) {
			return "Unknown";
		}
		
		Car car = person.getCar();
		if(car == null) {
			return "Unknown";
		}
		
		Insurance insurance = car.getInsurance();
		if(insurance == null) {
			return "Unknown";
		}
		
		return insurance.getName();
	}
	
	public static void main(String [] args) {
		/*
		 * 10.1.2 null 때문에 발생하는 문제
		 * 
		 * 자바에서 null 레퍼런스를 사용하면서 발생할 수 있는 이론적, 실용적 문제를 살펴보자.
		 * 
		 * 에러의 근원이다.
		 * NullPointerException은 자바에서 가장 흔히 발생하는 에러다.
		 * 
		 * 코드를 어지럽힌다.
		 * 때로는 중첩된 null 확인 코드를 추가해야 하므로 null 때문에 코드 가독성이 떨어진다.
		 * 
		 * 아무 의미가 없다
		 * null은 아무 의미도 표현하지 않는다. 특히 정적 형식 언어에서 값이 없음을 표현하는 방법으로는
		 * 적철하지 않다.
		 * 
		 * 자바 철학에 위배된다.
		 * 자바는 개발자로부터 모든 포인터를 숨겼다. 하지만 예외가 있는데 그것이 바로 null 포인터다.
		 * 
		 * 형식 시스템에 구멍을 만든다.
		 * null은 무형식이며 정보를 포함하고 있지 않으므로 모든 레퍼런스 형식에 null을 할당할 수 있다.
		 * 이런 식으로 null이 할당되기 시작하면서 시스템의 다른 부분으로 null이 퍼졌을 때 애초에 null이
		 * 어떤 의미로 사용되었는지 알 수 없다.
		 */
		
		/*
		 * 10.1.3 다른 언어는 null 대신 무얼 사용하나?
		 * 
		 * 그루비 같은 언어는 "안전 내비게이션 연산자-safe navigation operator(..?)"를 도입해서 null 문제 해결.
		 * 		def carInsuranceName = person?.car?.insurance?.name
		 * 
		 * 하스켈은 선택형값(optional value)을 저장할 수 있는 Maybe라는 형식을 제공.
		 * Maybe는 주어진 형식의 값을 갖거나 아니면 아무 값도 갖지 않을 수 있다.
		 * 
		 * 스칼라도 T 형식의 값을 갖거나 아무 값도 갖지 않을 수 있는 Option[T]라는 고제를 제공한다.
		 * Option 형식에서 제공하는 연산을 사용해서 값이 있는지 여부를 명시적으로 확인해야 한다.(즉 null 확인)
		 * 형식 시스템에서 이를 강제하므로 null과 관련한 문제가 일어날 가능성이 줄어든다.
		 * 
		 * 자바 8은 '선택형값' 개념의 영향을 받아 java.util.Optional<T>라는 새로운 클래스를 제공한다.
		 */
	}
}
