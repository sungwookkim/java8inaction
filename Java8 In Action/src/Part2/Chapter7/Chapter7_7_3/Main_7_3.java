package Part2.Chapter7.Chapter7_7_3;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import Part2.Chapter7.Chapter7_7_3.wordCounter.WordCounter;
import Part2.Chapter7.Chapter7_7_3.wordCounterSpliterator.WordCounterSpliterator;

/*
 * 7.3 Spliterator
 * 
 * Spliterator는 '분할할 수 있는 반복자(splitable iterator)'라는 의미이다.
 * Iterator처럼 Spliterator는 소스의 요소 탐색 기능을 제공한다는 점은 같지만 Spliterator는 병렬 작업에 특화되어 있다.
 * 자바 8은 컬렉션 프레임워크에 포함된 모든 자료구조에 사용할 수 있는 디폴트 Spliterator 구현을 제공한다.
 * 컬렉션은 spliterator라는 메서드를 제공하는 Spliterator 인터페이스를 구현한다.
 * 
 *  public Interface Spliterator<T> {
 *  	boolean tryAdvance(Consumer<? super T> action);
 *  	Spliterator<T> trySplit();
 *  	long estimateSize();
 *  	int characteristics();
 *  }
 *  
 * T : Spliterator에서 탐색하는 요소의 형식
 * tryAdvance : Spliterator 요소를 하나씩 순차적으로 소비하면서 탐색해야 할 요소가 남아있으면 참을 반환. (일반적인 Iterator 동작과 같다.)
 * trySplit : Spliterator의 일부 요소(자신이 반환한 요소)를 분할해서 두 번째 Spliterator를 생성하는 메서드.
 * estimateSize : 탐색해야 할 요소 수 정보를 제공. 특히 탐색해야 할 요소 수가 정확하지 않더라도 제공된 값을 이용해서 더 쉽고 공평하게
 * Spliterator를 분할할 수 있다.  
 * 
 * 7.3.1 분할 과정
 * 
 * 스트림을 여러 스트림으로 분할하는 과정은 재귀적으로 일어난다.
 * 1단계 : 첫 번째 Spliterator에 trySplit을 호출하면서 두 번째 Spliterator가 생성된다.
 * 2단계 : 두 개의 spliterator에 trySplit를 다시 호출하면서 네 개의 Spliterator가 생성된다.
 * 이처럼 trySplit의 결과가 null이 될 때까지 이 과정을 반복한다.
 * 이 분할 과정은 characteristics 메서드로 정의하는 Spliterator의 특성에 영향을 받는다.
 * 
 * Spliterator 특성
 * 
 * characteristics는 추상 메서드이며, Characteristics 메서드는 Spliterator 자체의 특성 집합을 포함하는 int를 반환한다.
 * 
 * 특성 : ORDERED 
 * 의미 : 리스트처럼 요소에 정해진 순서가 있으므로 Spliterator는 요소를 탐색하고 분할할 때 이 순서에 유의해야 한다.
 * 
 * 특성 : DISTINCT 
 * 의미 : x, y 두 요소를 방문했을 때 x.equals(y)는 항상 false를 반환한다.
 * 
 * 특성 : SORTED
 * 의미 : 탐색된 요소는 미리 정의된 정렬 순서를 따른다.
 * 
 * 특성 : SIZED
 * 의미 : 크기가 알려진 소스(예를 들면 Set)로 Spliterator를 생성했으므로 estimatedSize()는 정확한 값을 반환한다.
 * 
 * 특성 : NONNULL
 * 의미 : 탐색하는 모든 요소는 null이 아니다.
 * 
 * 특성 : IMMUTABLE
 * 의미 : 이 Spliterator의 소스는 불변이다. 즉, 요소를 탐색하는 동안 요소를 추가, 삭제, 고칠 수 없다.
 * 
 * 특성 : CONCURRENT
 * 의미 : 동기화 없이 Spliterator의 소스를 여러 스레드에서 동시에 고칠 수 있다.
 * 
 * 특성 : SUBSIZED 
 * 의미 : 이 Spliterator 그리고 분할되는 모든 Spliterator는 SIZED 특성을 갖는다.
 */
public class Main_7_3 {

	public static int countWordsIteratively(String s) {
		int counter = 0;
		boolean lastSpace = true;
		
		for(char c : s.toCharArray()) {
			if(Character.isWhitespace(c)) {
				lastSpace = true;
			} else {
				if(lastSpace) {
					counter++;
				}
				
				lastSpace = false;
			}
		}
		
		return counter;
	}
	
	public static int countWords(Stream<Character> stream) {
		WordCounter wordCounter = stream.reduce(new WordCounter(0, true)
			, WordCounter::accumulate
			, WordCounter::combine);
		
		return wordCounter.getCounter();
	}
	
	public static void main(String[] args) {
		final String SENTENCE = "Nel     mezzo   del   cammin     di    nostra     vita  "
			+ "mi    ritrovai    in una    selva   oscura"
			+ "  ch     la     dritta   via    era    smrrita   ";

		/*
		 * 7.3.2 커스텀 Spliterator 구현하기
		 */
		System.out.println("countWordsIteratively Found " + countWordsIteratively(SENTENCE) + " words");
		
		Stream<Character> stream = IntStream.range(0, SENTENCE.length())
			.mapToObj(SENTENCE::charAt);		
		System.out.println("countWords Found " + countWords(stream) + " words");
		
		/*
		 * 병렬 실행 시 잘못된 결과가 나온다.
		 * 이는 원래 문자열을 임의의 위치에서 둘로 나누다보니 예상치 못하게 하나의 단어를 둘로 계산하는 상황이 발생할 수 있다.
		 * 즉, 순차 스트림을 병렬 스트림으로 바꿀 때 스트림 분할 위치에 따라 잘못된 결과가 나온 것이다.
		 */
		stream = IntStream.range(0, SENTENCE.length())
			.mapToObj(SENTENCE::charAt);
		System.out.println("countWords(parallel) Found " + countWords(stream.parallel()) + " words");
		
		/*
		 * 커스텀 Spliterator을 이용한 올바른 병렬 실행.
		 */
		Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
		// StreamSupport.stream 팩토리 메서드로 전달한 두 번째 불린 인수는 병렬 스트림 생성 여부이다.
		stream = StreamSupport.stream(spliterator, true);
		
		System.out.println("countWords(spliterator parallel) Found " + countWords(stream) + " words");
		
	}

}
