package Part2.Chapter7.Chapter7_7_3.wordCounter;

public class WordCounter {
	private final int counter;
	private final boolean lastSpace;
	
	public WordCounter(int counter, boolean lastSpace) {
		this.counter = counter;
		this.lastSpace = lastSpace;
	}
	
	/*
	 * WordCounter의 상태를 어떻게 바꿀 것인지, 또는 엄밀히 WordCounter는 불변 클래스 이므로 새로운 WordCounter 클래스를
	 * 어떤 형태로 생성할 것인지 정의한다.
	 * 스트림을 탐색하면서 새로운 문자를 찾을 때마다 accmulate 메서드를 호출한다.
	 * 
	 * 반복 알고리즘처럼 accumulate 메서드는 문자열의 문자를 하나씩 탐색한다.
	 */
	public WordCounter accumulate(Character c) {
		if(Character.isWhitespace(c)) {
			return lastSpace ? this : new WordCounter(this.counter, true);
		} else {
			// 공백 문자를 만나면 지금까지 탐색한 문자를 단어로 간주(공백 문자는 제외) 단어 개수를 증가.
			return lastSpace ? new WordCounter(this.counter + 1, false) : this;
		}
	}
	
	/*
	 * 두 WordCounter의 counter 값을 더한다.
	 */
	public WordCounter combine(WordCounter wordCounter) {
		// counter 값만 더할 것이므로 마지막 공백은 신경 쓰지 않는다.
		return new WordCounter(this.counter + wordCounter.counter, wordCounter.lastSpace);
	}
	
	public int getCounter() {
		return this.counter;
	}
}
