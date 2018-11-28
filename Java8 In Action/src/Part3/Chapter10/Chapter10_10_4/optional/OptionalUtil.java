package Part3.Chapter10.Chapter10_10_4.optional;

import java.util.Optional;

public class OptionalUtil {

	public static Optional<Integer> stringToInt(String s) {
		/*
		 * 정수로 변환할 수 없는 문자열 문제를 빈 Optional로 해결할 수 있다.
		 * 즉, parseInt가 Optional을 반환하도록 모델링할 수 있다.
		 */
		try {
			return Optional.of(Integer.parseInt(s));
		} catch(NumberFormatException e) {
			return Optional.empty();
		}
	}
	
	public static Optional<Long> stringToLong(String s) {
		try {
			return Optional.of(Long.parseLong(s));
		} catch(NumberFormatException e) {
			return Optional.empty();
		}
	}	
}
