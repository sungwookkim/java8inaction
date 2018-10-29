package Part1.Chapter2.Chapter2_2_2.Quiz;

import java.util.Arrays;
import java.util.List;

import Part1.Chapter2.Chapter2_2_2.Quiz.print.impl.AppleFancyPrint;
import Part1.Chapter2.Chapter2_2_2.Quiz.print.impl.AppleSimplePrint;
import Part1.Chapter2.Chapter2_2_2.Quiz.print.inter.ApplePrintPredicate;
import Part1.Chapter2.Chapter2_2_2.entity.AppleEntity;

/*
 * 퀴즈 2-1
 * 유연한 prettyPrintApple 메서드 구현하기.
 * 
 * 사과 리스트를 인수로 받아 다양한 방법으로 문자열을 생성(커스터마이즈된 다양한 toString 메서드와 같이)
 * 할 수 있도록 파라미터화된 prettyPrintApple 메서드를 구현하시오.
 * 아래에 조건을 충족하시오.
 * 		각각의 사과 무게를 출력하도록 지시할 수 있다.
 * 		각각의 사과가 무거운지, 가벼운지 출력하도록 지시할 수 있다.
 */
public class Quiz2_1 {

	public static void prettyPrintApple(List<AppleEntity> inventory, ApplePrintPredicate p) {
		for(AppleEntity apple : inventory) {
			System.out.println(p.print(apple));
		}
	}
	
	public static void main(String[] args) {
		List<AppleEntity> inventory = Arrays.asList(new AppleEntity("green", 90)
				, new AppleEntity("red", 120)
				, new AppleEntity("green", 150));
		
		prettyPrintApple(inventory, new AppleSimplePrint());
		prettyPrintApple(inventory, new AppleFancyPrint(10));
		prettyPrintApple(inventory, new ApplePrintPredicate() {			
			@Override
			public String print(AppleEntity apple) {
				return "[Anonymous Class] apple Color : " + apple.getColor()
					+ ", apple weight : " + apple.getWeight();
			}
		});
	}
}
