package Part2.Chapter7.Chapter7_7_2.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

// RecursiveTask를 상속받아 포크/조인 프레임워크에서 사용할 태스크를 생성한다.
public class ForkJoinSumCalculator extends RecursiveTask<Long> {

	private static final long serialVersionUID = 1L;
	
	// 더할 숫자 배열
	private final long[] numbers;
	
	// 이 서브태스크에서 처리할 배열의 초기 위치와 최종 위치
	private final int start;
	private final int end;
	
	// 이 값 이하의 서브테스크는 더이상 분할할 수 없다.
	public static final long THRESHOLD = 10_000;

	// 메인 태스크를 생성할 때 사용할 공개 생성자
	public ForkJoinSumCalculator(long[] numbers) {
		this(numbers, 0, numbers.length);
	}
	
	// 메인 태스크의 서브태스크를 재귀적으로 만들 때 사용할 비공개 생성자	
	private ForkJoinSumCalculator(long[] numbers, int start, int end) {
		this.numbers = numbers;
		this.start = start;
		this.end = end;
	}
	
	// RecursiveTask의 추상 메서드 오버라이드
	@Override
	protected Long compute() {
		// 이 태스크에서 더할 배열의 길이
		int length = this.end - this.start;
		
		// 기준값과 같거나 작으면 순차적으로 결과를 계산한다.
		if(length <= THRESHOLD) {
			return this.computeSequentially(); 
		}
		
		// 배열의 첫 번째 절반을 더하도록 서브태스크를 생성한다.
		ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(this.numbers, this.start, this.start + length / 2);
		// FokJoinPool의 다른 스레드로 새로 생성한 태스크를 비동기로 실행한다.
		leftTask.fork();
		
		// 배열의 나머지 절반을 더하도록 서브태스크를 생성한다.
		ForkJoinSumCalculator rigthTask = new ForkJoinSumCalculator(this.numbers, this.start + length / 2, this.end);
		// 두 번째 서브태스크를 동기 실행한다. 이때 추가로 분할이 일어날 수 있다.
		Long rightResult = rigthTask.compute();
		// 첫 번째 서브태스크의 결과를 읽거나 아직 결과가 없다면 기다린다.
		Long leftResult = leftTask.join();
		
		// 두 서브태스크의 결과를 조합한 값이 이 태스크의 결과다.
		return leftResult + rightResult;
	}
 
	// 더 분할할 수 없을 때 서브 태스크의 결과를 계산하는 단순 알고리즘
	private long computeSequentially() {
		long sum = 0;
		
		/*
		 * n까지의 자연수 덧셈 작업을 병렬로 수행하는 방법을 더 직관적으로 보여준다.
		 */
		for(int i = this.start; i < this.end; i++) {
			sum += this.numbers[i];
		}
		
		return sum;
	}
	
	public static long forkJoinSum(long n) {
		/*
		 * LongStream으로 n까지의 자연수를 포함하는 배열을 생성했다.
		 * 생성된 배열을 ForkJoinSumCalculator의 생성자로 전달해서 ForkJoinTask를 만들었다.
		 * 마지막으로 생성한 태스크를 새로운 ForkJoinPool의 invoke 메서드로 전달했다.
		 * ForkJoinPool에서 실행되는 마지막 invoke 메서드의 반환값은 ForkJoinSumCalculator에서 
		 * 정의한 태스크의 결과가 된다.
		 */		
		long[] numbers = LongStream.rangeClosed(1, n).toArray();
		
		ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);

		/*
		 * 일반적으로 애플리케이션에서는 둘 이상의 ForkJoinPool을 사용하지 않는다. 즉, 소프트웨어의 필요한 곳에서
		 * 언제든 가져다 쓸 수 있도록 FokJoinPool을 한 번만 인스턴스화해서 정적 필드에 싱글턴으로 저장한다.
		 * 
		 * ForkJoinPool을 만들면서 인수가 없는 디폴트 생성자를 이용했는데 이는 jvm에서 이용할 수 있는 모든 프로세스가
		 * 자유롭게 풀에 접근할 수 있음을 의미한다.
		 * 
		 * 더 정확하게 Runtime.availableProcessors의 반환값으로 풀에 사용할 스레드 수를 결정한다.
		 * availableProcessors, 즉 '사용할 수 있는 프로세서' 이름과는 달리 실제 프로세서 외에 하이퍼스레딩과 관련된
		 * 가상 프로세서도 개수에 포함된다.
		 */
		return new ForkJoinPool().invoke(task);
	}
}
