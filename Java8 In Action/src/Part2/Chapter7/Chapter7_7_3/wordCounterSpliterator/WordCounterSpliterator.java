package Part2.Chapter7.Chapter7_7_3.wordCounterSpliterator;

import java.util.Spliterator;
import java.util.function.Consumer;

public class WordCounterSpliterator implements Spliterator<Character>{
	private final String string;
	private int currentChar = 0;
	
	public WordCounterSpliterator(String string) {
		this.string = string;
	}
	
	/*
	 * 문자열에서 현재 인덱스에 해당하는 문자를 Consumer에 제공한 다음에 인덱스를 증가시킨다.
	 * 
	 * 인수로 전달된 Consumer는 스트림을 탐색하면서 적용해야 하는 함수 집합이 작업을 처리할 수 있도록
	 * 소비한 문자를 전달하는 자바 내부 클래스다.
	 * 
	 * 예제에서는 스트림을 탐색하면서 하나의 리듀싱 함수, 즉 WordCounter의 accumulate 메서드만 적용한다.
	 * 해당 메서드는 새로운 커서 위치가 전체 문자열 길이보다 작으면 참을 반환하며 이는 반복 탐색해야 할
	 * 문자가 남아있음을 알린다.
	 */
	@Override
	public boolean tryAdvance(Consumer<? super Character> action) {
		// 현재 문자를 소비한다.
		action.accept(string.charAt(this.currentChar++));
		// 소비할 문자가 남았다면 true를 반환한다.
		return this.currentChar < string.length();
	}
	
	/*
	 * 반복될 자료구조를 분할하는 로직을 포함한다.
	 * 
	 * 분할 동작을 중단할 한계를 설정해야 한다.
	 * 분할 과정에서 남은 문자 수가 한계값(예제에서는 10) 이하면 null을 반환. 즉 분할을 중지하도록 지시한다.
	 * 
	 * 반대로 분할이 필요한 상황에는 파싱해야 할 문자열 청크의 중간 위치를 기준으로 분할하도록 지시한다.
	 * 이때 단어 중간을 분할하지 않도록 빈 문자가 나올 때까지 분할 위치를 이동시킨다.
	 * 분할할 위치를 찾았다면 새로운 Spliterator를 만든다.
	 * 새로운 Spliterator는 현재 위치(currentChar)부터 분할된 위치까지의 문자를 탐색한다.
	 */
	@Override
	public Spliterator<Character> trySplit() {
		int currentSize = this.string.length() - this.currentChar;
		
		// 파싱할 문자열을 순차 처리할 수 있을 만큼 충분히 작아졌음을 알리는 null을 반환한다.
		if(currentSize < 10) {
			return null;
		}
		
		// 파싱할 문자열의 중간을 분할 위치로 설정한다.
		for(int splitPos = currentSize / 2 + this.currentChar; splitPos < this.string.length(); splitPos++) {
			// 다음 공백이 나올 때까지 분할 위치를 뒤로 이동 시킨다.
			if(Character.isWhitespace(this.string.charAt(splitPos))) {
				// 처음부터 분할 위치까지 문자열을 파싱할 새로운 WordCounterSpliterator를 생성한다.
				Spliterator<Character> spliterator = new WordCounterSpliterator(this.string.substring(this.currentChar, splitPos));
				
				// 이 WordCounterSpliterator의 시작 위치를 분할 위치로 설정한다.
				this.currentChar = splitPos;
				
				return spliterator;
			}
		}

		return null;
	}

	/*
	 * 탐색해야 할 요소의 개수는 Spliterator가 파싱할 문자열 전체 길이(string.length())와 현재 반복 중인 위치(currentChar)의 차다.
	 */
	@Override
	public long estimateSize() {
		return this.string.length() - this.currentChar;
	}
	
	/*
	 * Spliterator.ORDERED : 문자열의 문자 등장 순서가 유의미함.
	 * Spliterator.SIZED : estimateSize 메서드의 반환값이 정확함.
	 * Spliterator.SUBSIZED : trySplit으로 생성된 Spliterator도 정확한 크기를 가짐.
	 * Spliterator.NONNULL : 문자열에는 null 문자가 존재 하지 않음.
	 * Spliterator.IMMUTABLE : 문자열 자체가 불변 클래스이므로 문자열을 파싱하면서 속성이 추가되지 않음.
	 * 
	 * 등의 특성임을 알려준다.
	 */
	@Override
	public int characteristics() {
		return Spliterator.ORDERED + Spliterator.SIZED + Spliterator.SUBSIZED + Spliterator.NONNULL + Spliterator.IMMUTABLE;
	}
}
