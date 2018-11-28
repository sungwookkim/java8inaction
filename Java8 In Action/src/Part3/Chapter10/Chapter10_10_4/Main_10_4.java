package Part3.Chapter10.Chapter10_10_4;

import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;

import Part3.Chapter10.Chapter10_10_4.optional.OptionalUtil;

/*
 * 10.4 Optional을 사용한 실용 예제
 * 
 * 자바 8에서 제공하는 Optional 클래스를 효과적으로 사용하려면 값이 없는 상황을 처리하던 기존의 알고리즘과는
 * 다른 과점에서 접근해야 한다. 즉, 코드 구현만 바꾸는 것이 아니라 네이티브 자바 API와 상호작용하는 방식도 바꿔야 한다.
 */
public class Main_10_4 {

	public static void main(String[] args) {
		HashMap<String, String> hashMap = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			
			{
				put("number", "123456");
				put("string", "sinnake");
			}
		};
		
		/*
		 * 10.4.1 잠재적으로 null이 될 수 있는 대상으 Optional로 감싸기
		 * 
		 * 기존 자바 API에서는 null을 반환하면서 요청한 값이 없거나 어떤 문제로 계산에 실패했음을 알린다.
		 * 예를 들어 Map의 get 메서드는 요청한 키에 대응하는 값을 찾지 못했을 때 null을 반환한다.
		 * get 메서드의 시그너처는 우리가 고칠 수 없지만 get 메서드의 반환값은 Optional로 감쌀 수 있다.
		 */
		String value = hashMap.get("key");
		System.out.println("[Normal] key - key Value : " + value);
		
		/*
		 * map에서 반환하는 값을 Optional로 감싸서 개선할 수 있다.
		 * 코드가 복잡한 기존 if-then-else를 사용하지 않고 Optional.ofNullable를 이용할 수 있다. 
		 */
		value = Optional.ofNullable(hashMap.get("string")).orElse("value");
		System.out.println("[Optional] key - key Value : " + value);
		
		/*
		 * 10.4.2 예외와 Optional
		 * 
		 * 자바 API는 값을 제공할 수 없을 때 null을 반환하는 대신 예외를 발생시킬 때도 있다.
		 * 전형적인 예가 Integer.parseInt(String) 정적 메서드다. 이 메서드는 문자열을 정수로 바꾸지 못했을 때
		 * NumberFormatException을 발생시킨다.
		 * 기존 값이 null일 수도 있을 때는 null 여부를 확인했지만 예외를 발생시키는 메서드는 try/catch 블록을 사용해야 한다는 점이 다르다.
		 * 
		 * OptionalUtil 같은 유틸리티 클래스를 만들어서 사용하면 유용하게 사용할 수 있다.
		 */
		System.out.println("10.4.2 예외와 Optional - 값이 문자열인 경우 : " + OptionalUtil.stringToInt(hashMap.get("string")).orElse(0));
		System.out.println("10.4.2 예외와 Optional - 값이 숫자인 경우 : " + OptionalUtil.stringToInt(hashMap.get("number")).orElse(0));
		System.out.println("10.4.2 예외와 Optional - null인 경우 : " + OptionalUtil.stringToInt(hashMap.get("key")).orElse(-1));
		
		/*
		 * 기본형 Optional과 이를 사용하지 말아야 하는 이유
		 * 
		 * 기본형으로 특화된 OptionalInt, OptionalLong, OptionalDouble 등의 클래스를 제공한다.
		 * 스트림이 많은 요소를 가질 때는 기본형 특화 스트림을 이용해서 성능을 향상시킬수 있다고 설명했다.
		 * 하지만 Optional의 최대 요소 수는 한 개이므로 Optional에서는 성능 개선을 할 수 없다.
		 * 
		 * 그리고 기본 특화형 Optional은 map, flatMap, filter 등을 지원하지 않으므로 권장하지 않는다.
		 * 게다가 기본 특화형 Optional로 생성한 결과는 다른 일반 Optional과 혼용할 수 없다.
		 */

		/*
		 * 10.4.3 응용
		 * 
		 * Properties를 읽어서 값을 초 단위의 지속시간(duration)으로 해석하는 예제이다.
		 * 지속시간은 양수여야 하고 문자열이 양의 정수를 가리키면 해당 정수를 반환하고 그 외에는 0을 반환한다.
		 */
		Properties props = new Properties();
		props.put("a", "5");
		props.put("b", "true");
		props.put("c", "-3");
		
		System.out.println("10.4.3 응용 - readDuration method : " + readDuration(props, "a"));
		System.out.println("10.4.3 응용 - readDuration method : " + readDuration(props, "b"));
		System.out.println("10.4.3 응용 - readDuration method : " + readDuration(props, "c"));
		System.out.println("10.4.3 응용 - readDuration method : " + readDuration(props, "d"));
		System.out.println("10.4.3 응용 - readDuration method : " + readDuration(props, null));
		
		System.out.println("10.4.3 응용 - readDurationQuiz method : " + readDurationQuiz(props, "a"));
		System.out.println("10.4.3 응용 - readDurationQuiz method : " + readDurationQuiz(props, "b"));
		System.out.println("10.4.3 응용 - readDurationQuiz method : " + readDurationQuiz(props, "c"));
		System.out.println("10.4.3 응용 - readDurationQuiz method : " + readDurationQuiz(props, "d"));
		System.out.println("10.4.3 응용 - readDurationQuiz method : " + readDurationQuiz(props, null));
	}

	public static int readDurationQuiz(Properties props, String name) {

		return Optional.ofNullable(props.getProperty(Optional.ofNullable(name).orElse("")) )
			.flatMap(OptionalUtil::stringToInt)
			.filter(p -> p > 0)
			.orElse(0);

/*
		return Optional.ofNullable(props.getProperty(Optional.ofNullable(name).orElse("")) )
			.flatMap(p -> {
				try {
					return Optional.ofNullable(Integer.parseInt(p));
				} catch(NumberFormatException e) {
					return Optional.empty();
				}
			})
			.filter(p -> p > 0)
			.orElse(0);	
*/
	}

	public static int readDuration(Properties props, String name) {
		String value = "";
		
		if(name != null ) {
			value = props.getProperty(name);
		}
		
		if(value != null) {
			try {
				int i = Integer.parseInt(value);

				if(i > 0) {
					return i;
				}
			} catch(NumberFormatException e) { }
		}		

		return 0;
	}
	
}
