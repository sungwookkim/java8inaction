package Part2.Chapter7.Chapter7_7_2;

import java.util.function.Function;

import Part2.Chapter7.Chapter7_7_2.forkjoin.ForkJoinSumCalculator;

/*
 * 7.2 포크/조인 프레임워크
 * 
 * 포크/조인 프레임워크는 병렬화할 수 있는 작업을 재귀적으로 작은 작업으로 분할한 다음에 서브태스크 각각의 결과를
 * 합쳐서 전체 결과를 만들도록 설계되었다.
 * 포크/조인 프레임워크에서는 서브태스크를 스레드 풀(FokJoinPool)의 작업자 스레드에 분산 할당하는 ExcutorService 인터페이스를 구현한다.
 */
public class Main_7_2 {

	public static long meansureSumPerf(Function<Long, Long> adder, long n) {
		long fastest = Long.MAX_VALUE;
		
		for(int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			long sum = adder.apply(n);
			long duration = (System.nanoTime() - start) / 1_000_000;
			
			System.out.println("Result : " + sum);
			
			if(duration < fastest) {
				fastest = duration;
			}			
		}
		
		return fastest;
	}
	
	public static void main(String[] args) {
		/*
		 * 7.2.1 RecursiveTask 활용
		 * 
		 * 스레드 풀을 이용하려면 RecursiveTask<R>의 서브클래스를 만들어야 한다. 
		 * 여기서 R은 병렬화된 태스크가 생성하는 결과 형식 또는 결과가 없을 때(결과가 없더라도 다른 비지역 구조를 바꿀 수 있다.) 
		 * RecursiveAction 형식이다.
		 * 
		 * compute 메서드는 태스크를 서브태스크로 분할하는 로직과 더 이상 분할할 수 없을 때 개별 서브태스크의 결과를 생산할 알고리즘을 정의한다.
		 * 따라서 대부분의 compute 메서드 구현은 다음과 같은 의사코드 형식을 유지한다.
		 * 
		 * if(태스크가 충분히 작거나 더 이상 분할할 수 없으면) {
		 * 		순차적으로 태스크 계산
		 * } else {
		 * 		태스크를 두 서브태스크로 분할.
		 * 		태스크가 다시 서브태스크로 분할되도록 이 메서드를 재귀적으로 호출함.
		 * 		모든 서브태스크의 연산이 완료될 때까지 기달림.
		 * 		각 서버태스크의 결과를 합침.
		 * }
		 * 
		 * 해당 알고리즘은 분할 후 정복(divide-and-conquer)알고리즘의 병렬화 버전이다.
		 * 
		 * ForkJoinSumCalculator를 ForkJoinPool로 전달하면 풀의 스레드가 ForkJoinSumCalculator의 compute 메서드를 실행하면서 작업을 수행한다.
		 * 
		 * 		1. compute 메서드는 병렬로 실행할 수 있을만큼 태스크의 크기가 충분히 작아졌는지 확인하며
		 * 		2. 아직 태스크의 크기가 크다고 판단되면 숫자 배열을 반으로 분할해서 두 개의 새로운 ForkJoinSumCalculator로 할당한다.
		 * 		3. 그러면 다시 새로운 ForkJoinPool이 새로 생성된 ForkJoinSumCalculator를 실행한다.
		 * 		4. 결국 이 과정이 재귀적으로 반복되면서 주어진 조건(예제에서는 덧샘을 수행할 항목이 만 개 이하여야 함)을 만족할 때까지 태스크를 분할한다.
		 * 
		 * 이제 각 서브태스크는 순차적으로 처리되며 포킹 프로세스로 만들어진 이진트리의 태스크를 루트에서 역순으로 방문한다.
		 * 즉, 각 서브태스크의 부분결과를 합쳐서 태스크의 최종 결과를 계산한다.
		 */
		System.out.println("7.2 포크/조인 프레임워크 : " + meansureSumPerf(ForkJoinSumCalculator::forkJoinSum, 100000));
		
		/*
		 * 7.2.2 포크/조인 프레임워크를 제대로 사용하는 방법
		 */
		
	}
}
