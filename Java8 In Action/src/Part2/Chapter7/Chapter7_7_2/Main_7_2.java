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
		 * 		각 서브태스크의 결과를 합침.
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
		System.out.println("7.2 포크/조인 프레임워크 : " + meansureSumPerf(ForkJoinSumCalculator::forkJoinSum, 10_000_000));
		
		/*
		 * 7.2.2 포크/조인 프레임워크를 제대로 사용하는 방법
		 * 
		 * 다음은 포크/조인 프레임워크를 효과적으로 사용하는 방법이다.
		 * 
		 * - join 메서드를 태스크에 호출하면 태스크가 생산하는 결과가 준비될 때까지 호출자를 블록시킨다.
		 * 따라서 두 서브태스크가 모두 시작된 다음에 join을 호출해야 한다.
		 * 그렇지 않으면 각각의 서브태스크가 다른 태스크가 끝나길 기달리는 일이 발생하며 원래 순차 알고리즘보다
		 * 느리고 복잡한 프로그램이 될수 있다. 
		 * 
		 * - RecursiveTask 내에서는 ForkJoinPool의 invoke 메서드를 사용하지 말아야 한다.
		 * 대신 compute나 fork 메서드를 직접 호출할 수 있다. 순차 코드에서 병렬 계산을 시작할 때만 invoke를 사용한다.
		 * 
		 * - 서브태스크에 fork 메서드를 호출해서 ForkJoinPool의 일정을 조절할 수 있다.
		 * 왼쪽 작업과 오른쪽 작업 모두에 fork 메서드를 호출하는 것이 자연스러울 것 같지만 한쪽 작업에는 fork를
		 * 호출하는 것보다는 compute를 호출하는 것이 효율적이다.
		 * 그러면 두 서브태스크의 한 태스크에는 같은 스레드를 재사용할 수 있으므로 풀에 불필요한 태스크를 할당하는
		 * 오버헤드를 피할 수 있다.
		 * 
		 *  - 포크/조인 프레임워크를 이용하는 병렬 계산은 디버깅이 어렵다.
		 *  보통 IDE로 디버깅할 때 스택 트레이스(stack trace)로 문제가 일어난 과정을 쉽게 확인할 수 있는데,
		 *  포크/조인 프레임워크에서는 fork라 불리는 다른 스레드에서 compute를 호출하므로 스택 트레이스가 도움이
		 *  되지 않는다.
		 *  
		 *  - 멀티코어에 포크/조인 프레임워크를 사용하는 것이 순차처리보다 무조건 빠를 거라는 생각은 버려야 한다.
		 *  병렬 처리로 성능을 개선하려면 태스크를 여러 독립적인 서브태스크로 분할할 수 있어야 한다.
		 *  각 서브태스크의 실행시간은 새로운 서브태스크를 포킹하는데 드는 시간보다 길어야 한다.
		 *  	예)
		 *  		I/O를 한 서브태스크에 할당. 다른 서브태스크에서는 계산을 실행.
		 *  		즉 I/O와 계산을 병렬로 실행할 수 있다.
		 * 순차버전과 병렬버전의 성능을 비교할 때는 다른 요소도 고려해야 한다.
		 * 다른 자바 코드와 마찬가지로 jit 컴파일러에 의해 최적화되려면 몇 차례의 '준비 과정(warmed up)' 또는 실행과정을
		 * 거쳐야 한다.
		 * 따라서 성능을 측정할 때는 하니스(meansureSumPerf 메서드와 같은)를 통해 여러 번 프로그램을 실행한 결과를 측정해야한다.
		 * 또한 컴파일러 최적화는 병렬버전보다는 순차버전에 집중될 수 있다는 사실도 기억하자
		 * (예를 들어 순차버전에서는 죽은 코드를 분석해서 사용되지 않는 계산은 아예 삭제하는 등의 최적화를 달성하기 쉽다.)
		 */
		
		/*
		 * 7.2.3 작업 훔치기
		 * 
		 * 기기의 코어 수보다 서브태스크가 많이 생성 되는게 자원만 낭비 하는것 처럼 보일수 있다.
		 * 실제로 각각의 태스크가 CPU로 할당되는 상황이라면 어차피 서브태스크로 분할한다고 해서 성능이 좋아지지는 않을것이다.
		 * 하지만 실제로 코어 개수와 관계없이 적절한 크기로 분할된 많은 태스크를 포킹하는 것이 바람직하다.
		 * 
		 * 이론적으로 코어 개수만큼 병렬화된 태스크로 작업부하를 분할하면 모든 CPU 코어에 태스크를 실행할 것이고 크기가 같은 각각의
		 * 태스크는 같은 시간에 종료될 것이라고 생각할 수 있다.
		 * 
		 * 하지만 복잡한 로직인 경우에는 각각의 서브태스크의 작업완료 시간이 크게 달라질 수 있다.
		 * 이는 분할 기법이 효율적이지 않을수도 있고 예기치 않게 디스크 접근 속도 저하 및 외부 서비스와 협력 과정 중 지연이 생길 수 있기 때문이다.
		 * 
		 * 포크/조인 프레임워크에서는 "작업 훔치기(work stealing)"라는 기법으로 이 문제를 해결한다.
		 * 작업 훔치기 기법은 ForkJoinPool의 모든 스레드를 거의 공정하게 분할한다.
		 * 각각의 스레드는 자신에게 할당된 태스크를 포함하는 이중 연결 리스트(doubly linked list)를 참조하면서 작업이 끝날 때마다 큐의 헤드에서
		 * 다른 태스크를 가져와서 작업을 처리한다. 이때 한 스레드는 다른 스레드보다 자신에게 할당된 태스크를 더 빨리 처리할 수 있다.
		 * 즉, 다른 스레드는 바쁘게 일하고 있는데 한 스레드는 할일이 다 떨어진 상황이다. 이때 할일이 없어진 스레드는 유휴 상태로 바뀌는 것이 아니라
		 * 다른 스레드 큐의 꼬리(tail)에서 작업을 훔쳐온다. 모든 태스크가 작업을 끝낼 때까지, 즉 모든 큐가 빌 때까지 이 과정을 반복한다.
		 * 따라서 태스크의 크기를 작게 나누어야 작업자 스레드 간의 작업부하를 비슷한 수준으로 유지할 수 있다.
		 * 
		 */
	}
}

