package Part3.Chapter8.Chapter8_8_2;

import java.util.function.UnaryOperator;

import Part3.Chapter8.Chapter8_8_2.chain.abs.ProcessingObject;
import Part3.Chapter8.Chapter8_8_2.chain.impl.HeaderTextProcessing;
import Part3.Chapter8.Chapter8_8_2.chain.impl.SpellCheckerProcessing;
import Part3.Chapter8.Chapter8_8_2.factory.LambdaProductFactory;
import Part3.Chapter8.Chapter8_8_2.factory.ProductFactory;
import Part3.Chapter8.Chapter8_8_2.observer.impl.Feed;
import Part3.Chapter8.Chapter8_8_2.observer.impl.Guardian;
import Part3.Chapter8.Chapter8_8_2.observer.impl.LeMonde;
import Part3.Chapter8.Chapter8_8_2.observer.impl.NYTimes;
import Part3.Chapter8.Chapter8_8_2.strategy.Validator;
import Part3.Chapter8.Chapter8_8_2.strategy.inter.IsAllLowerCase;
import Part3.Chapter8.Chapter8_8_2.strategy.inter.IsNumeric;
import Part3.Chapter8.Chapter8_8_2.templateMethod.impl.OnlineBankingLambda;
import Part3.Chapter8.Chapter8_8_2.templateMethod.impl.OnlineDepositBanking;
import Part3.Chapter8.Chapter8_8_2.templateMethod.impl.OnlineWithdrawBanking;

/*
 * 8.2 람다로 객체지향 디자인 패턴 리팩토링하기
 */
public class Main_8_2 {

	public static void main(String[] args) {
		/*
		 * 8.2.1 전략 패턴
		 * 
		 * 전략 패턴은 한 유형의 알고리즘을 보유한 상태에서 런타임에 적절한 알고르짐을 선택하는 기법이다.
		 */
		Validator numericValidator = new Validator(new IsNumeric());
		System.out.println("8.2.1 전략 디자인 패턴 - IsNumeric 객체 : " + numericValidator.validate("aaaa"));
		
		Validator lowerCaseValidator = new Validator(new IsAllLowerCase());
		System.out.println("8.2.1 전략 디자인 패턴 - IsAllLowerCase 객체 : " + lowerCaseValidator.validate("bbbb"));
		
		/*
		 * ValidationStrategy는 함수형 인터페이스 Predicate<String>과 같은 함수 디스크립터를 갖고 있으므로
		 * 다양한 전략을 새로운 클래스 구현할 필요 없이 람다 표현식으로 구현할 수 있다.
		 */
		numericValidator = new Validator((s) -> s.matches("\\d+"));		
		System.out.println("8.2.1 전략 디자인 패턴 - IsNumeric의 람다 표현식 : " + numericValidator.validate("aaaa"));
		
		lowerCaseValidator = new Validator((s) -> s.matches("[a-z]+"));
		System.out.println("8.2.1 전략 디자인 패턴 - IsAllLowerCase의 람다 표현식 : " + lowerCaseValidator.validate("bbbb"));
		
		/*
		 * 8.2.2 템플릿 메서드 패턴
		 * 
		 * 알고리즘의 개요를 제시한 다음에 알고리즘의 일부를 고칠 수 있는 유연함을 제공해야 할 때 템플릿 메서드 패턴을 사용한다.
		 * 다시 말해 '이 알고리즘을 사용하고 싶은데 그대로는 안 되고 조금 고쳐야 하는'상황에 적합하다.
		 */
		System.out.println("8.2.2 템플릿 메서드 패턴 : ");
		OnlineDepositBanking onlineDepositBanking = new OnlineDepositBanking();
		onlineDepositBanking.processCustomer(1);
		
		OnlineWithdrawBanking onlineWithdrawBanking = new OnlineWithdrawBanking();
		onlineWithdrawBanking.processCustomer(2);

		/*
		 * 인제 OnlineBanking 클래스를 상속받지 않고 직접 람다 표현식을 전달해서 다양한 동작을 추가할 수 있다.
		 */
		System.out.println("8.2.2 템플릿 메서드 패턴 - 람다 표현식 : ");
		new OnlineBankingLambda().processCustomer(3, (customer)-> {
			System.out.println(customer.getName() + "님의 찾으신 ID는 " + customer.getId() + " 입니다.");
		});		
		new OnlineBankingLambda().processCustomer(1, (customer)-> {
			System.out.println(customer.toString());
		});
		new OnlineBankingLambda().processCustomer(2, onlineDepositBanking::makeCustomerHappy);
		new OnlineBankingLambda().processCustomer(1, onlineWithdrawBanking::makeCustomerHappy);
		
		/*
		 * 8.2.3 옵저버
		 * 
		 * 어떤 이벤트가 발생했을 때 한 객체(주체-subject라 불리는)가 다른 객체 리스트(옵저버 observer)에 자동으로 알림을
		 * 보내야 하는 상황에서 옵저버 디자인 패턴을 사용한다.
		 * 예를 들어 주식의 가격(주체) 변동에 반응하는 다수의 거래자(옵저버)에서도 옵저버 패턴을 사용할 수 있다.
		 */
		System.out.println("8.2.3 옵저버 패턴 : ");
		Feed f = new Feed();
		f.registerObserver(new NYTimes());
		f.registerObserver(new Guardian());
		f.registerObserver(new LeMonde());
		f.notifyObservers("money");
		f.notifyObservers("queen");
		f.notifyObservers("wine");;
		
		System.out.println("8.2.3 옵저버 패턴 - 람다 표현식 : ");
		f.registerObserver((tweet) -> {
			if(tweet != null && "moenyLambda".equals(tweet)) {
				System.out.println("Lambda Breaking new in NY! " + tweet);
			}
		});
		f.registerObserver((tweet) -> {
			if(tweet != null && "wineLambda".equals(tweet)) {
				System.out.println("Lambda Today cheese, wine and news! " + tweet);
			}
		});
		f.registerObserver((tweet) -> {
			if(tweet != null && "queenLambda".equals(tweet)) {
				System.out.println("Lambda Yet another news in London... " + tweet);
			}
		});
		f.notifyObservers("moenyLambda");
		f.notifyObservers("wineLambda");
		f.notifyObservers("queenLambda");
		
		/*
		 * 8.1.4 의무 체인
		 * 
		 * 작업처리 객체의 체인(동작 체인 등)을 만들 때는 의무 체인 패턴을 사용한다.
		 * 한 객체가 어떤 작업을 처리한 다음에 다른 객체로 결과를 전달하고, 다른 객체도 해야 할 작업을
		 * 처리한 다음에 또 다른 객체로 전달하는 식이다.
		 */
		ProcessingObject<String> p1 = new HeaderTextProcessing();
		ProcessingObject<String> p2 = new SpellCheckerProcessing();
		
		p1.setSuccessor(p2);
		System.out.println("8.2.4 의무 체인 : " + p1.handle("Aren't labdas really sexy?!!"));
		
		UnaryOperator<String> headerProcessing = (text) -> "From raoul, Mario and Alan : " + text;
		UnaryOperator<String> spellCheckerProcessing = (text) -> text.replaceAll("labda", "lambda");
		
		System.out.println("8.2.4 의무 체인 - 람다 표현식 : " + headerProcessing
			.andThen(spellCheckerProcessing)
			.apply("Aren't labdas really sexy?!!"));
		
		/*
		 * 8.2.5 팩토리
		 * 
		 * 인스턴화 로직을 클라이언트에 노출하지 않고 객체를 만들 때 팩토리 디자인 패턴을 사용한다.
		 * createProduct 메서드는 생성자와 설정을 외부로 노출하지 않음으로써 클라이언트가 단순하게
		 * 상품을 생산할 수 있다.
		 */
		System.out.println("8.1.5 팩토리 : " + ProductFactory.createProduct("loan"));
		System.out.println("8.1.5 팩토리 : " + ProductFactory.createProduct("stock"));
		System.out.println("8.1.5 팩토리 : " + ProductFactory.createProduct("bond"));
		
		/*
		 * 여러 인수를 전달하는 상황에서는 단순한 Supplier로는 사용하기 힘들다.
		 * 만약 생성자 인수가 3개가 필요하다고 한다면 세 인수를 지원하는 새로운 함수형 인터페이스를 만들어야 한다.
		 *  
		 * 		public interface TriFunction<T, U, V, R> {
		 * 			R apply(T t, U u, V v);
		 * 		}
		 * 
		 * 		Map<String, TriFunction<Integer, Integer, String, Product>> map = new HashMap<>();  
		 */
		System.out.println("8.1.5 팩토리 - 람다 표현식 : " + LambdaProductFactory.createProduct("loan"));
		System.out.println("8.1.5 팩토리 - 람다 표현식 : " + LambdaProductFactory.createProduct("stock"));
		System.out.println("8.1.5 팩토리 - 람다 표현식 : " + LambdaProductFactory.createProduct("bond"));
		
	}

}
