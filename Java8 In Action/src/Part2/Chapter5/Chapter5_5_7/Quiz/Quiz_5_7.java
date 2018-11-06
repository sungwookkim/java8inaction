package Part2.Chapter5.Chapter5_5_7.Quiz;

import java.util.stream.Stream;

public class Quiz_5_7 {

	public static void main(String[] args) {
		/*
		 * 퀴즈 5-4 피보나치수열 집합
		 */
		Stream.iterate(new int[] {0, 1}, t 
			-> new int[] {t[1], t[0] + t[1]})
			.limit(20)
			.map(t -> t[0])
			.forEach(t -> System.out.print(t + ", ")); 
	}

}
