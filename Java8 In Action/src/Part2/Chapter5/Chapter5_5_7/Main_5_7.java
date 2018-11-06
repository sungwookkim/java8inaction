package Part2.Chapter5.Chapter5_5_7;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * 5.7 스트림 만들기
 */
public class Main_5_7 {

	public static void main(String[] args) throws UnsupportedEncodingException, URISyntaxException {
		/*
		 * 5.7.1 값으로 스트림 만들기
		 * 
		 * 임의의 수를 인수로 받는 정적 메서드 Stream.of를 이용해서 스트림을 만들 수 있다.
		 */
		Stream<String> streamString = Stream.of("Java 8", "Lambdas", "In ", "Action");
		System.out.println("5.7.1 값으로 스트림 만들기");
		streamString.map(String::toUpperCase).forEach(System.out::println);
		
		/*
		 * 5.7.2 배열로 스트림 만들기
		 * 
		 * 배열을 인수로 받는 정적 메서드 Array.stream을 이용해서 스트림을 만들 수 있다.
		 */
		int[] numbers = {2, 3, 5, 7, 11, 13};
		System.out.println("5.7.2 배열로 스트림 만들기 : " + Arrays.stream(numbers).sum());
		
		/*
		 * 5.7.3 파일로 스트림 만들기
		 *
		 * 파일을 처리하는 등의 I/O 연산에 사용하는 자바의 NIO API(비블록 I/O)도 스트림 API를
		 * 활용할 수 있도록 업데이트 되었다. 
		 * java.nio.file.Files의 많은 정적 메서드가 스트림을 반환한다.
		 */
		long uniquoWords = 0;		
		String fileName = URLDecoder.decode(Main_5_7.class.getResource("").getPath() + "Main_5_7.txt", "UTF-8")
				.replaceFirst("^/(.:/)", "$1");

		try (
			Stream<String> lines = 
			Files.lines(Paths.get(fileName), Charset.defaultCharset()) ) {
			
			uniquoWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
				.distinct().count();				

			System.out.println("5.7.3 파일로 스트림 만들기 : " + uniquoWords);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * 5.7.4 함수로 무한 스트림 만들기
		 * 
		 * 스트림 API는 함수에서 스트림을 만들 수 있는 두 개의 정적 메서드 Stream.iterate와 Stream.generate를
		 * 제공한다.
		 * 두 연산을 이용해서 무한 스트림(infinite stream), 즉 고정된 컬렉션에서 고정된 크기의 스트림이 아닌
		 * 크기가 고정되지 않는 스트림을 만들 수 있다.		
		 * iterate와 generate에서 만든 스트림은 요청할때마다 주어진 함수를 이용해서 값을 만든다.
		 * 보통 무한한 값을 출력하지 않도록 limit(n)함수를 함께 연결해서 사용한다.
		 */
		
		/*
		 * iterate
		 * 
		 * iterate는 요청할 때마다 값을 생산할 수 있으며 끝이 없으므로 무한 스트림(infinite stream)을 만든다.
		 * 이러한 스트림을 언바운드 스트림(unbounded stream)이라고 표현한다.
		 * 이런 특징이 스트림과 컬렉션의 가장 큰 차이점이다. 예제에서는 limit 메서드를 이용해서 스트림의 크기를
		 * 명시적으로 처음 10개의 짝수로 제한한다. 그리고 최종 연산인 forEach를 호출해서 스트림을 소비하고
		 * 개별 요소를 출력한다.
		 */
		System.out.println("5.7.4 함수로 무한 스트림 만들기 - iterate");
		Stream.iterate(0, n -> n + 2)
			.limit(10)
			.forEach(System.out::println);
		
		/*
		 * generate
		 * 
		 * generate도 요구할 때 값을 계산하는 무한 스트림을 만들 수 있다.
		 * 하지만 iterate와 달리 생산된 각 값을 연속적으로 계산하지 않는다.
		 */
		System.out.println("5.7.4 함수로 무한 스트림 만들기 - generate");
		Stream.generate(Math::random)
			.limit(5)
			.forEach(System.out::println);
		
		/* 우리가 사용한 공급자(supplier)는 상태가 없는 메서드, 즉 나중에 계산에 사용할
		 * 어떤 값도 저장해두지 않는다.
		 * 하지만 공급자에 꼭 상태가 없어야 하는 것은 아니다. 공급자가 상태를 저장한 다음에
		 * 스트림의 다음 값을 만들 때 상태를 고칠 수 있다.
		 * 여기서 중요한 점은 병렬 코드에서는 공급자에 상태가 있으면 안전하지 않다는 것이다.
		 * 
		 * 아래 코드는 IntSupplier 인스턴스를 만들었다.
		 * 만들어진 객체는 기존 피보나치 요소와 두 인스턴스 변수에 어떤 피보나치 요소가
		 * 들어있는지 추적하므로 "가변(mutable) 상태 객체"다.
		 * getAsInt를 호출하면 객체 상태가 바뀌며 새로운 값을 생산한다.
		 * iterate를 사용했을 때는 각 과정에서 새로운 값을 생성하면서도 기존 상태를 바꾸지 않는
		 * 순수한 "불변(immutable)" 상태를 유지했다.
		 * (iterate 피보나치는 Quiz_5_7 자바 파일에서 확인.)
		 * 
		 * 스트림을 병렬로 처리하면서 올바른 결과를 얻으려면 "불변 상태 기법"을 고수해야 한다.
		 */
		IntSupplier fib = new IntSupplier() {
			/*
			 * 추적 대상 변수들 - 위 설명에서 나온 "상태"를 뜻하기도 한다.
			 */
			private int prev = 0;
			private int curr = 1;
						
			@Override
			public int getAsInt() {
				int oldPrev = this.prev;
				int nextVal = this.prev + this.curr;
				
				this.prev = this.curr;
				this.curr = nextVal;
						
				return oldPrev;
			}
		};

		System.out.println("5.7.4 함수로 무한 스트림 만들기 - generate");
		IntStream.generate(fib).limit(10).forEach(t -> System.out.print(t + ", "));

	}

}
