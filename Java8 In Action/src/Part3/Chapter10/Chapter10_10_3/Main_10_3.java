package Part3.Chapter10.Chapter10_10_3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/*
 * 10.3 Optional 적용 패턴
 */
public class Main_10_3 {

	public static void main(String[] args) {
		/*
		 * 10.3.1 Optional 객체 만들기
		 */
		
		/*
		 *  빈 Optional
		 *  정적 팩토리 메서드 Optional.empty로 빈 Optional 객체를 얻을수 있다.
		 */
		Optional<Car> optCar = Optional.empty();
		System.out.println("빈 Optional : " + optCar);
		
		Car car = new Car("avante");
		Insurance insurance = new Insurance("sinnake");
		/*
		 * null이 아닌 값으로 Optional 만들기
		 * 정적 팩토리 메서드 Optional.of로 null이 아닌 값을 포함하는 Optional을 만들 수 있다.
		 * 
		 * 이제 car가 null이라면 즉시 NullPointerException이 발생한다.
		 * (Optional을 사용하지 않았다면 car의 프로퍼티에 접근하려 할 때 에러가 발생했을 것이다.)
		 */
		optCar = Optional.of(car);
		System.out.println("null이 아닌 값으로 Optional 만들기 : " + optCar);

		/*
		 * null로 Optional 만들기
		 * 
		 * 정적 팩토리 메서드 Optional.ofNullable로 null을 저장할 수 있는 Optional을 만들 수 있다.
		 * car가 null이면 빈 Optional 객체가 반환된다.
		 */
		car = null;
		optCar = Optional.ofNullable(car);
		System.out.println("null로 Optional 만들기 : " + optCar);
		
		/*
		 * 10.3.2 맵으로 Optional의 값을 추출하고 변환하기
		 * 
		 * 보통 객체의 정보를 추출할 때는 Optional을 사용할 때가 많다.
		 * 예를 들어 보험회사의 이름을 추출한다고 가정하자.
		 * 아래 코드처럼 이름 정보에 접근하기 전에 insurance가 null인지 확인해야 한다.
		 */
		String name = null;
		if(insurance != null) {
			name = insurance.getName();
		}
		System.out.println("10.3.2 맵으로 Optional의 값을 추출하고 변환하기 : [name] = " + name);
		
		/*
		 * 이런 유형의 패턴에 사용할 수 있도록 Optional을 map 메서드를 지원한다.
		 * 
		 * Optional의 map 메서드는 스트림의 map 메서드와 개념적으로 비슷하다.
		 * 스트림의 map은 스트림의 각 요소에 제공된 함수를 적용하는 연산이다.
		 * 여기서 Optional 객체를 최대 요소의 개수가 한 개 이하인 데이터 컬렉션으로 생각할 수 있다.
		 * 
		 * Optional이 값을 포함하면 map의 인수로 제공된 함수가 값을 바꾼다.
		 * Optional이 비어있으면 아무 일도 일어나지 않는다.
		 */
		Optional<Insurance> optInsurance = Optional.ofNullable(new Insurance("sinnake"));
		Optional<String> optName = optInsurance.map(Insurance::getName);
		System.out.println("10.3.2 맵으로 Optional의 값을 추출하고 변환하기 - map : [name] = " + optName);
		
		/*
		 * 10.3.3 flatMap으로 Optional 객체 연결 
		 */
		Optional<Person> mapPerson = Optional.ofNullable(new Person("kim sung wook")) ;
		Optional<Car> mapCar = Optional.ofNullable(new Car("avante"));		
		Optional<Insurance> mapInsurance = Optional.ofNullable(new Insurance("sinnake"));		

		mapCar.get().setInsurance(mapInsurance);
		mapPerson.get().setCar(mapCar);
		
		/*
		 * mapPerson
		 * 		.map(Person::getCar)
		 * 		.map(Car::getInsurance)
		 * 		.map(Insurance::getName)
		 * 
		 * 위 코드는 컴파일이 되지 않는다.
		 * mapPerson의 형식은 Optional<Person>이므로 map 메서드를 호출할 수 있다.
		 * 하지만 getCar는 Optional<Car> 형식의 객체를 반환한다.
		 * 즉, map 연산의 결과는 Optional<Optional<Car>> 형식의 객체다.
		 * getInsurance는 또 다른 Optional 객체를 반환하므로 getInsurance 메서드를 지원하지 않는다. 
		 */
	
		/*
		 * Optional은 스트림의 flatMap처럼 인수로 받은 함수를 적용해서 생성된 각각의 스트림에서 콘텐츠만 남길 수 있다.
		 * 즉 이차원 Optional을 일차원 Optional로 평준화를 시킬 수 있다.
		 */
		System.out.println("10.3.3 flatMap으로 Optional 객체 연결 - flatMap : " + getCarInsuranceName(mapPerson));
		
		/*
		 * Optional을 이용한 Person/Car/Insurance 참조 체인
		 * 
		 * Person을 Optional로 감싼 다음에 flatMap(Person::getCar)를 호출했다. 이 호출은 두 단계의 논리적 과정으로 생각할 수 있다.
		 * 첫 번째 단계에서는 Optional 내부의 Person에 Function을 적용한다. 여기서는 Person의 getCar 메서드가 Function이다.
		 * getCar 메서드는 Optional<Car>를 반환하므로 Optional 내부의 Person이 Optional<Car>로 변환되면서 중첩 Optional이 생성된다.
		 * 따라서 flatMap연산으로 Optional을 평준화한다.
		 * 
		 * 평준화 과정이란 이론적으로 두 Optional을 합치는 기능을 수행하면서 둘 중 하나라도 null이면 빈 Optional을 생성하는 연산이다.
		 * flatMap을 빈 Optional에 호출하면 아무 일도 일어나지 않고 그대로 반환된다.
		 * 반면 Optional이 Person을 감싸고 있다면 flatMap에 전달된 Function이 Person에 적용된다.
		 * Function을 적용한 결과가 이미 Optional이므로 flatMap 메서드는 결과를 그대로 반환할 수 있다.
		 * 
		 * 두 번째 단계도 첫 번째 단계와 비슷하게 Optional<Car>를 Optional<Insurance>로 변환한다.
		 * 세 번째 단계에서는 Optional<Insurance>를 Optional<String>으로 변환한다.
		 * 세 번재 단계에서 Insurance.getName()은 String을 반환하므로 flatMap을 사용할 필요가 없다.
		 * 
		 * 호출 체인 중 어떤 메서드가 빈 Optional을 반환한다면 전체 결과로 빈 Optional을 반환하고 아니면 관련 보험회사의 이름을 포함하는
		 * Optional을 반환한다.
		 * 호출 체인의 결과로 Optional<String>이 반환되는데 여기에 회사 이름이 저장되어 있을수도 없을수도 있다.
		 * Optional이 비어있을 때 디폴트 값(default value)을 제공하는 orElse 메서드를 사용했다.
		 * Optional은 디폴트값을 제공하거나 Optional을 언랩(unwrap)하는 다양한 메서드를 제공한다.
		 */
		
		/*
		 * 10.3.4 디폴트 액션과 Optional 언랩
		 * 
		 * Optional이 비어있을 때 디폴트값을 제공할 수 있는 orElse 메서드로 값을 읽자.
		 * Optional 클래스는 Optional 인스턴스에서 값을 읽을 수 있는 다양한 인스턴스 메서드를 제공한다.
		 * 
		 * - get()은 값을 읽는 가장 간단한 메서드면서 동시에 가장 안전하지 않는 메서드다.
		 * 래핑된 값이 있으면 값을 반환하고 값이 없으면 NoSuchElementException을 발생시킨다.
		 * 따라서 Optional에 값이 반드시 있다고 가정할 수 없으면 get 메서드를 사용하지 않는것이 좋다.
		 * 결국 이 상황은 중첩된 null 확인 코드를 넣는 상황과 다르지 않을수 있다.
		 * 
		 * - orElse(T other) 메서드를 이용하면 Optional이 값을 포함하지 않았을 때 디폴트값을 제공한다.
		 * 
		 * - orElseGet(Supplier<? extends T> other)는 orElse 메서드에 대응하는 게으른 버전 메서드다.
		 * Optional에 값이 없을 때만 Supplier가 실행되기 때문이다.
		 * 디폴트 메서드를 만드는데 시간이 걸리거나(효율성 때문에) Optional이 비었을 때만 디폴트값을 생성하고 싶다면
		 * (디플트값이 반드시 필요한 상황) orElseGet(Supplier<? extends T> other)를 사용해야 한다.
		 * 
		 * - orElseThrow(Supplier<? extends X> exceptionSupplier)는 Optional이 비어있을 때 예외를 발생시킨다.
		 * get 메서드와 다르게 해당 메서드는 발생시킬 예외의 종류를 선택할 수 있다.
		 * 
		 * - ifPresent(Consumer<? super T> consumer)를 이용하면 값이 존재할 때 인수로 넘겨준 동작을 실행할 수 있다.
		 * 값이 없으면 아무 일도 일어나지 않는다.
		 */
		
		/*
		 * 정 map으로 하고 싶다면 아래 메서드처럼 할순 있다.
		 */
		System.out.println("10.3.3 flatMap으로 Optional 객체 연결 - map : " + getCarInsuranceNameMAP(mapPerson));
		
		/*
		 * 도메인 모델에 Optional을 사용했을 때 데이터를 직렬화할 수 없는 이유
		 * 
		 * Optional 클래스는 필드 형식으로 사용할 것을 가정하지 않았으므로 Serializable 인터페이스를 구현하지 않았다.
		 * 따라서 우리 도메인 모델에 Optional을 사용한다면 직렬화(serializable)모델을 사용하는 도구나 프레임워크에서 문제가 생길 수 있다.
		 * 이와 같은 단점에도 불구하고 여전히 optional을 사용해서 도메인 모델을 구성하는 것이 바람직하다고 생각한다.
		 * 특히 객체 그래프에서 일부 또는 전체가 null일 수 있는 상황이라면 더욱 그렇다.
		 * 직렬화 모델이 필요하다면 아래 코드처럼 Optional로 값을 반환받을 수 있는 메서드를 추가하는 방식을 권장한다.
		 * 
		 * public class Person {
		 * 		private Car car;
		 * 
		 * 		public Optional<Car> getCarAsOptional() {
		 * 			return Optional.ofNullable(car);
		 * 		}
		 * }
		 */
		
		/*
		 * 10.3.5 두 Optional 합치기
		 */
		mapPerson = Optional.ofNullable(new Person("김성욱")) ;
		mapCar = Optional.ofNullable(new Car("아반테"));		

		mapPerson.get().setCar(Optional.ofNullable(new Car("아반테")) );
		System.out.println("10.3.5 두 Optional 합치기 : " + nullSafefindCheapestInsurance(mapPerson, mapCar).get());
		System.out.println("10.3.5 두 Optional 합치기 : " + nullSafefindCheapestInsuranceQuiz(mapPerson, mapCar).get());
		
		/*
		 * 10.3.6 필터로 특정값 거르기
		 * 
		 * filter 메서드는 프레디케이트를 인수로 받는다.
		 * Optional 객체가 값을 가지고 프레디케이트와 일치하면 filter 메서드는 그 값을 반환하고 그렇지 않으면 빈 Optional 객체를 반환한다.
		 * Optional이 비어있다면 filter 연산은 아무 동작도 하지 않는다. Optional에 값이 있으면 그 값에 프레디케이트를 적용한다.
		 * 프레디케이트 적용 결과가 true면 Optional에는 아무 변화도 일어나지 않고 false면 값은 사라지고 빈 Optional이 된다.
		 */
		System.out.println(findInsurance("아반테"));
		
		/*
		 * Optional 클래스의 메서드
		 * 
		 * 메서드 : empty 
		 * 설명 : 빈 Optional 인스턴스 반환
		 * 
		 * 메서드 : filter
		 * 설명 : 값이 존재하고 프레디케이트와 일치하면 값을 포함하는 Optional 반환하고 
		 * 값이 없거나 프레디케이트와 일치하지 않으면 빈 Optional을 반환.
		 * 
		 * 메서드 : flatMap
		 * 설명 : 값이 존재하면 인수로 제공된 함수를 적용한 결과 Optional을 반환하고
		 * 값이 없으면 빈 Optional을 반환. 
		 * 
		 * 메서드 : get
		 * 설명 : 값이 존재하면 Optional이 감싸고 있는 값을 반환하고, 값이 없으면
		 * NoSuchElementException이 발생.
		 * 
		 * 메서드 : ifPresent
		 * 설명 : 값이 존재하면 지정된 Consumer를 실행하고 값이 없으면 아무 일도 일어나지 않음.
		 * 
		 * 메서드 : isPresent
		 * 설명 : 값이 존재하면 true 반환, 값이 없으면 false 반환.
		 * 
		 * 메서드 : map
		 * 설명 : 값이 존재하면 제공된 매핑 함수를 적용함.
		 * 
		 * 메서드 : of
		 * 설명 : 값이 존재하면 값을 감싸는 Optional을 반환하고, 값이 null이면 NullPointerException을 발생.
		 * 
		 * 메서드 : ofNullable
		 * 설명 : 값이 존재하면 값을 감싸는 Optional을 반환하고, 값이 null이면 빈 Optional을 반환.
		 * 
		 * 메서드 : orElse
		 * 설명 : 값이 존재하면 값을 반환, 값이 없으면 디폴트값을 반환.
		 * 
		 * 메서드 : orElseGet
		 * 설명 : 값이 존재하면 값을 반환, 값이 없으면 Supplier에서 제공하는 값을 반환.
		 * 
		 * 메서드 : orElseThrow
		 * 설명 : 값이 존재하면 값을 반환, 값이 없으면 Supplier에서 생성한 예외를 발쌩.
		 */
	}
	
	public static Insurance findInsurance(String insuranceName) {		

		return Optional.ofNullable(new Insurance("신나게", "아반테", 10_000_000))
			.filter(i -> i.getCarKind().equals(insuranceName))
			.orElseGet(() -> new Insurance("없음", "없음", 0));
			//.orElse(new Insurance("없음", "없음", 0));
	}
	
	public static Optional<Insurance> nullSafefindCheapestInsuranceQuiz(Optional<Person> person, Optional<Car> car) {
		/*
		 * 첫 번재 Optional에 flatMap을 호출했으므로 첫 번째 Optional이 비어있다면 인수로 전달한 람다 표현식이 
		 * 실행되지 않고 그대로 빈 Optional을 반환한다.
		 * 반면 persion 값이 있으면 flatMap 메서드에 필요한 Optional<Insurance>를 반환하는 Function의 입력으로
		 * person을 사용한다.
		 * 이 함수 바디에서는 두 번째 Optional에 map을 호출하므로 Optional이 car 값을 포함하지 않으면 Function은
		 * 빈 Optional을 반환하므로 결국 해당 메서드는 빈 Optional을 반환한다.
		 */
		return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
	}
	
	public static Optional<Insurance> nullSafefindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
		if(person.isPresent() && car.isPresent()) {
			return Optional.of(findCheapestInsurance(person.get(), car.get())) ;
		}
		
		return Optional.empty();
	}
	
	public static Insurance findCheapestInsurance(Person person, Car car) {
		List<Insurance> insurancesList = Arrays.asList(
			new Insurance("신나게_1", "아반테", 70_000_0)
			, new Insurance("신나게_2", "그렌져", 20_000_0)
			, new Insurance("신나게_3", "비엠더블유", 30_000_0)
			, new Insurance("신나게_4", "벤츠", 40_000_0)
			, new Insurance("신나게_4", "벤츠", 20_000_0)
			, new Insurance("신나게_5", "아반테", 50_000_0)
			, new Insurance("신나게_6", "아반테", 60_000_0)
		);

		Insurance cheapesInsurace = insurancesList.stream()
			.filter((insurances) -> Optional.ofNullable(car)
				.map(Car::getName)
				.orElse("Unknown")
				.equals(insurances.getCarKind() ))
			.filter((insurances) -> Optional.ofNullable(person.getCar())
				.map(Optional::get)
				.map(Car::getName)
				.orElse("Unknown")
				.equals(insurances.getCarKind() ))
			.min(Comparator.comparingInt(Insurance::getPrice))
			.orElse(new Insurance("없음", "없음", 0));

		return cheapesInsurace;
	}	
	
	public static String getCarInsuranceName(Optional<Person> person) {
		return person
			.flatMap((p) -> Optional.ofNullable(p.getCar()).map(c -> c.get()) )
			.flatMap((c) -> Optional.ofNullable(c.getInsurance()).map(i -> i.get()) )
			.map(Insurance::getName)
			.orElse("Unknown");
	}
	
	public static String getCarInsuranceNameMAP(Optional<Person> person) {
		return person
			.map(Person::getCar)
			.map(i -> i.get().getInsurance())
			.map(s -> s.get().getName())
			.orElse("Unknown");
	}
}

class Person {
	// 사람은 차가 있을수도 없을수도 있으므로 Optional로 정의한다.
	private Optional<Car> car;
	private String name;
	
	public Person() { }
	
	public Person(String name) {
		this.name = name;
	}

	public Optional<Car> getCar() {
		return this.car;
	}
	
	public void setCar(Optional<Car> car) {
		this.car = car;
	}
	
	public String getName() {
		return this.name;
	}
}

class Car {
	// 자동차가 보험에 가입되어 있을수도 없을수도 있으므로 Optional로 정의한다.
	private Optional<Insurance> insurance;
	private String name;
	
	public Car() { }
	
	public Car(String name) {
		this.name = name;
	}
	
	public Optional<Insurance> getInsurance() {
		return this.insurance;
	}
	
	public void setInsurance(Optional<Insurance> insurance) {
		this.insurance = insurance;
	}
	
	public String getName() {
		return this.name;
	}
}

class Insurance {
	// 보험회사에는 반드시 이름이 있다.
	private String name;
	private String carKind;
	private int price;
	
	public Insurance() { }

	public Insurance(String name) {
		this.name = name;
	}

	public Insurance(String name, String carKind, int price) {
		this.name = name;
		this.carKind = carKind;
		this.price = price;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCarKind(String carKind) {
		this.carKind = carKind;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCarKind() {
		return carKind;
	}
	
	public int getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "{"
			+ "price : " + this.price
			+ ", name : " + this.name
			+ ", carKind : " + this.carKind
			+"}";
	}
}
