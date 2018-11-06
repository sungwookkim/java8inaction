package Part1.Chapter3.Chapter3_3_3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.function.Function;

import Part1.Chapter3.Chapter3_3_3.buffer.inter.BufferedReaderProcessor;

/*
 * 3.3 람다 활용 : 실행 어라운드 패턴
 * 
 * 자원 처리(예를 들면 데이터베이스의 파일 처리)에 사용하는 순환 패턴(recurrent pattern)은
 * 		자원을 열고,
 * 		처리한 다음에,
 * 		자원을 닫는
 * 순서로 이루어진다. 설정(setup)과 정리(cleanup)과정은 대부분 비슷하다.
 * 즉, 실제 자원을 처리하는 코드를 설정과 정리 두 과정이 둘러싸는 형태를 갖는다.
 * 
 * 		초기화/준비 코드
 * 		작업 A
 * 		정리/마무리 코드
 * 		
 * 		혹은
 * 
 * 		초기화/준비 코드
 * 		작업 B
 * 		정리/마무리 코드
 * 
 * 와 같은 형식의 코드를 실행 어라운드 패턴(execute around pattern)이라고 한다.
 */
public class Main_3_3 {
	public static FileReader getExmpFile() throws FileNotFoundException, UnsupportedEncodingException {		
		return new FileReader(URLDecoder.decode(Main_3_3.class.getResource("").getPath() + "Main_3_3.txt", "UTF-8"));
	}

	/*
	 * 3.3.1 1단계 : 동작 파라미터화를 기억하라.
	 * 
	 * 아래 코드는 한 줄만 읽을 수 있다. 만약 한 번에 두 줄을 읽거나 가장 자주 사용되는 단어를 반환하려면 어떻게 해야 할까?
	 * 기존의 설정, 정리 과정은 재사용하고 badProcessFile 메서드만 다른 동작을 수행하도록 명령할 수 있다면 좋을 것이다.
	 * 이를 위해 badProcessFile의 동작을 파라미터화 하는 것이다. 
	 */
	public static String badProcessFile() throws IOException {
		/*
		 * 아래의 try 문법을 보면 다른 try랑 특이한데 해당 문법은 try-with-resources문법으로
		 * 자바 7에 추가된 새로운 기능이다. 이를 사용하면 자원을 명시적으로 닫을 필요가 없다.
		 * (즉, close 같은 메서드를 호출하지 않아도 된다.)
		 */
		try(BufferedReader br = new BufferedReader(getExmpFile()) ) {
			return br.readLine();
		}
	}

	/*
	 * 3.3.2 2단계 : 함수형 인터페이스를 이용해서 동작 전달.
	 * 
	 * 함수형 인터페이스 자리에 람다를 사용할 수 있다. 따라서 BufferedReader -> String과 IOException을 던질(throw)수 있는
	 * 시그너처와 일치하는 함수형 인터페이스를 만들어야 한다.
	 * (BufferedReaderProcessor라는 함수형 인터페이스를 만들어서 인수로 받음.)
	 */
	public static String functionInterfaceProcessFile(BufferedReaderProcessor b) throws IOException {
		try(BufferedReader br = new BufferedReader(getExmpFile()) ) {
			return b.process(br);
		}
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("badProcessFile : " + badProcessFile());
		
		/*
		 * 3.3.3 3단계 : 동작 실행!
		 * 
		 * 이제 BufferReaderProcessor에 정의된 process 메서드의 시그너처(BufferedReader -> String)와
		 * 일치하는 람다를 전달할 수 있다.
		 * 람다 표현식으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달할 수 있으며 전달된 코드는 
		 * 		"함수형 인터페이스의 인스턴스로 전달된 코드와 같은 방식으로 처리"
		 * 한다. 따라서 functionInterfaceProcessFile 바디 내에서 BufferedReaderProcessor 객체의 process를
		 * 호출할 수 있다.
		 */
		// 람다 표현식
		System.out.println(functionInterfaceProcessFile((BufferedReader br) 
				-> "functionInterfaceProcessFile : " + br.readLine() + br.readLine()) );

		// 익명 클래스
		System.out.println(functionInterfaceProcessFile(new BufferedReaderProcessor() {
				@Override
				public String process(BufferedReader b) throws IOException {
					return "익명 클래스 :" + b.readLine() + b.readLine();
				}
			}) );
		
		/*
		 * 예외, 람다, 함수형 인터페이스의 관계
		 * 
		 * 함수형 인터페이스는 확인된 예외를 던지는 동작을 허용하지 않는다. 즉, 예외를 던지는 람다 표현식을 만들려면
		 * 확인된 예외를 선언하는 함수형 인터페이스를 직접 정의하거나 람다를 try / catch 블록으로 감싸야 한다.
		 * 
		 * 본 코드에서 사용 된 BufferedReaderProcessor에서 process 메서드 시그너처에 명시적으로 IOException을 선언.
		 * 
		 * 그러나 우리는 BufferedReaderProcessor는 Function<T, R> 형식의 함수형 인터페이스를 기대하는 API를 사용하고 있으며
		 * 직접 함수형 인터페이스를 만들기 어려운 상황이다. 이런 상황에서는 아래 코드와 같이 명시적으로 확인된 예외를 잡을 수 있다.
		 */
		Function<BufferedReader, String> f = (BufferedReader br) -> {
			try {
				/*
				 * 강제로 Exception을 확인하고 싶다면 아래 return을 주석처리하고 
				 * throw new IOException(); 부분을 주석을 해제 한다.
				 */
				// throw new IOException();
				return br.readLine();
			} catch (IOException e) {
				System.out.println("!!!!! 강제로 Exception이 발생 하였습니다. !!!!!");
				throw new RuntimeException(e);
			}
		};

		System.out.println("try-catch Function : " + f.apply(new BufferedReader(getExmpFile()) ));
		
	}

}
