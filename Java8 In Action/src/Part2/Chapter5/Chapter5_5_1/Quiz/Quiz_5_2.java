package Part2.Chapter5.Chapter5_5_1.Quiz;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * 퀴즈 5-2 매핑
 */
public class Quiz_5_2 {

	public static void main(String[] args) {
		/************************************************************************************/
		/* 1. 숫자 리스트가 주어졌을 때 각 숫자의 제곱근으로 이루어진 리스트를 반환하시오. 	*/
		/* 예를 들어 [1, 2, 3, 4 5]가 주어지면 [1, 4, 9, 16, 25]를 반환해야 한다.			*/
		/************************************************************************************/	
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
		System.out.println(numbers.stream()
			.map(n -> n * n)
			.collect(Collectors.toList() ));
		
		/************************************************************************/
		/* 2. 두 개의 숫자 리스트가 있을 떄 모든 숫자 쌍의 리스트를 반환하시오.	*/
		/* 예를 들어 두 개의 리스트[1, 2, 3]과 [3, 4]가 주어지면				*/
		/* [(1,3), (1,4), (2,3), (2,4), (3,3), (3,4)]를 반환해야 한다.			*/
		/************************************************************************/
		List<Integer> numbers1 = Arrays.asList(1, 2, 3);
		List<Integer> numbers2 = Arrays.asList(3, 4);
		
		/*
		 * [데이터가 배열인 경우]
		 */
		List<Integer[]> arrResult =  numbers1.stream()
				.flatMap(i -> numbers2.stream().map(j -> new Integer[] {i, j}) )							
				.collect(Collectors.toList());

		// [데이터가 배열인 경우] 평면화한 결과
		System.out.println("[데이터가 배열인 경우] 평면화한 결과 : " + arrResult.stream()
			.flatMap(Arrays::stream)
			.collect(Collectors.toList()) );

		// [데이터가 배열인 경우] 평명화된 결과를 Integer에서 String으로 변환한 결과
		System.out.println("[데이터가 배열인 경우] 평명화된 결과를 Integer에서 String으로 변환한 결과 : " + arrResult.stream()
			.map(results -> Arrays.stream(results)
				.map(result -> String.valueOf(result))
				.toArray(String[]::new))
			.flatMap(Arrays::stream)
			//.flatMap(intArray -> Arrays.asList(intArray).stream().map(i -> String.valueOf(i) ))
			.collect(Collectors.toList()) );
		
		/*
		 * [데이터가 List인 경우] 
		 */
		List<List<Integer>> listResult = numbers1.stream()
			.flatMap(i -> numbers2.stream().map(j -> Arrays.asList(i, j)) )
			.collect(Collectors.toList());

		// [데이터가 List인 경우] 평면화 하지 않는 결과.
		System.out.println("[데이터가 List인 경우] 평면화 하지 않는 결과 : " + listResult);
	
		// [데이터가 List인 경우] 평면화한 결과.
		System.out.println("[데이터가 List인 경우] 평면화한 결과 : " + listResult.stream()
			.map(param -> param.stream().toArray(Integer[]::new))
			.flatMap(Arrays::stream)
			.collect(Collectors.toList()) );

		// [데이터가 List인 경우] List의 Integer형식을 String으로 변환하고 평면화한 결과.
		System.out.println("[데이터가 List인 경우] List의 Integer에서 String으로 변환하고 평면화한 결과 : " + listResult.stream()			
			.map(param -> param.stream()
				.map(value -> String.valueOf(value))
				.toArray(String[]::new))
			.flatMap(Arrays::stream)
			//.flatMap(intArray -> intArray.stream().map(i -> String.valueOf(i) ))
			.collect(Collectors.toList()) );
		
		/********************************************************************************/
		/* 3. 이전 예제에서 합이 3으로 나누어떨어지는 쌍만 반환하려면 어떻게 해야 할까?	*/
		/* 예를 들어 (2, 4), (3, 3)을 반환해야 한다.									*/
		/********************************************************************************/
		System.out.println(numbers1.stream()
			.flatMap(i -> numbers2.stream()
					.filter(j -> (i+j) % 3 == 0)
					.map(j -> new Integer[] {i, j}) )							
				.collect(Collectors.toList())
				.stream()					
				/*					
				.map(results -> Arrays.stream(results)
					.map(result -> result.toString())					
					.toArray(String[]::new))
				*/
				.flatMap(Arrays::stream)
				.collect(Collectors.toList()) );
	}

}
