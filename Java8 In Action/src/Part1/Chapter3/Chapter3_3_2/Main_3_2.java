package Part1.Chapter3.Chapter3_3_2;
/*
 * 3.2 어디에, 어떻게 람다를 사용할까?
 * 
 * 함수형 인터페이스라는 문맥에서 람다 표현식을 사용할 수 있다.
 * 
 * 
 * 3.2.1 함수형 인터페이스
 * 앞 전에서 만든 Predicate<T>인터페이스로 필터 메서드를 파라미터화할 수 있음을 기억하는가?(못해도 한다고 하자.)
 * 바로 Predicate<T>가 함수형 인터페이스이다. Predicate<T>는 오직 하나의 추상 메서드만 지정하기 때문이다.
 * 
 * public interface Predicate<t> {
 * 		boolean test(T t);
 * }
 * 
 * 간단히 말해 함수형 인터페이스는 "정확히 하나의 추상 메서드를 지정하는 인터페이스" 다.
 * 지금까지 살펴본 자바 API의 함수형 인터페이스로 Comprator, Runnable 등이 있다.
 * (아래에 나오는 인터페이스는 자바 API의 함수형 인터페이스다.)
 * 
 * java.util.Comparator
 * public interface Comparator<T> {
 * 		int compare(T o1, T o2);
 * }
 * 
 * java.lang.Runnable
 * public interface Runnable {
 * 		void run();
 * }
 * 
 * java.awt.event.ActionListener
 * public interface ActionListener exends EventLstener {
 * 		void actionPerformed(ActionEvent e);
 * }
 * 
 * java.util.concureent.Callable
 * public interface Callable<V> {
 * 		V call();
 * }
 * 
 * java.security.PrivilegedAction
 * public interface PrivilegedAction<V> {
 * 		T run();
 * }
 *
 * 함수형 인터페이스로 뭘 할 수 있을까?(많은걸 할수 있으니깐 만들어겠지..)
 * 람다 표현식으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달할 수 있으므로
 * 		전체 표현식을 함수형 인터페이스의 인스턴스로 취급
 * 		(기술적으로 따지면 함수형 인터페이스를 concrete 구현한 클래스의 인스턴스)
 * 할 수 있다.
 * 
 * Note
 * 		인터페이스는 디폴트 메서드(인터페이스의 메서드를 구현하지 않은 클래스를 
 * 		고려해서 기본 구현을 제공하는 바디를 포함하는 메서드)를 포함할 수 있다.
 * 		여기서 많은 디폴트 메서드가 있더라도 추상 메서드가 오직 하나면 함수형 인터페이스다.
 */
public class Main_3_2 {
	
	public static void process(Runnable r) {
		r.run();
	}

	
	/*
	 * 3.2.1 함수형 인터페이스 예제
	 * 
	 * 아래 예제의 코드들은 Runnable이 오직 하나의 추상 메서드 run을
	 * 정의하는 함수형 인터페이스이므로 올바른 코드다.
	 */
	static void runnable() {
		// 람다 사용.
		Runnable r1 = () -> System.out.println("hello hell 1?");		
		process(r1);
		
		// 익명 클래스 사용.
		process(new Runnable() {

			@Override
			public void run() {
				System.out.println("hello hell 2?");
				
			}
		});
		
		// 직접 전달된 람다 표현식.
		process(() -> System.out.println("hello hell 3?"));
	}
	
	/*
	 * 3.2.2 함수 디스크립터
	 * 
	 * 함수형 인터페이스의 추상 메서드 시그너처(signature)는 람다 표현식의 시그너처를 가리킨다.
	 * 람다 표현식의 시그너처를 서술하는 메서드를 함수 디스크립터(function descriptor)라고 한다.
	 * 예를 들어
	 * 		Runnable에 유일한 추상 메서드인 run메서드는 인수와 반환값이 없으므로
	 * 		Runnable 인터페이스는 인수와 반환값이 없는 시그너처로 생각할 수 있다.
	 * 
	 * 람다 표현식의 유효성을 확인하는 방법은 일단 람다 표현식은 변수에 할당하거나 함수형 인터페이스를
	 * 인수로 받는 메서드로 전달할 수 있으며, 함수형 인터페이스의 추상 메서드와 같은 시그너처를 갖는다는
	 * 사실을 기억하자.
	 * 
	 * 함수형 인터페이스에 추상 메서드로 전달 되는 람다 표현식은 해당 메서드의 반환값 혹은 타입, 인수 타입 혹은 갯수에
	 * 맞춰서 표현식을 전달 해야 한다.
	 * 		Runnable의 run 메서드는 반환값은 void이고 인수는 없다. 
	 * 		위 예제 코드에서
	 * 			process(() -> System.out.println("hello hell 3?"));
	 * 		코드를 람다 표현식으로 직접 전달 하였다.  		
	 * 		여기서 직접 전달된 람다 표현식을 분석 하자면 () 인수가 없고 메서드 바디 영역에서는 println메서드만 호출하고
	 * 		반환값이 없다. 즉, run 함수 시그너처와 같은 인수가 없고 반환값이 없는 형태와 동일하므로 올바른 람다 표현식이다.
	 */
	public static void main(String[] args) {
		runnable();
	}

	/*
	 * @FunctionalInterface는 무엇인가?
	 * 새로운 자바 API를 살펴보면 함수형 인터페이스에 @FunctionalInterface라는 어노테이션이 추가 되었다.
	 * @FunctionalInterface는 함수형 인터페이스임을 가리키는 어노테이션이다.
	 * @FunctionalInterface로 인터페이스를 선언했지만 실제로 함수형 인터페이스가 아니면 컴파일러가 에러를 발생시킨다.
	 */
}
