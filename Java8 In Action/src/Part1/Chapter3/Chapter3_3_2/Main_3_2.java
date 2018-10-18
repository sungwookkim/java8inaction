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
	
	public static class subMain_3_2 {
		public static void process(Runnable r) {
			r.run();
		}
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
		subMain_3_2.process(r1);
		
		// 익명 클래스 사용.
		Runnable r2 = new Runnable() {
			
			@Override
			public void run() {
				System.out.println("hello hell 2?");
			}
		};
		subMain_3_2.process(r2);
		
		// 직접 전달된 람다 표현식.
		subMain_3_2.process(() -> System.out.println("hello hell 3?"));
	}
	public static void main(String[] args) {
		runnable();
	}

}
