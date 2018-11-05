package Part2.Chapter5.Chapter5_5_5;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import Part2.Chapter5.Chapter5_5_5.entity.Trader;
import Part2.Chapter5.Chapter5_5_5.entity.Transaction;

/*
 * 5.5 실전 연습
 */
public class Main_5_5 {

	static public void main(String[] args) {
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Cambridge");
		Trader brian = new Trader("Brian", "Cambridge");

		List<Transaction> transactions = Arrays.asList(
			new Transaction(brian, 2011, 300)
			, new Transaction(raoul, 2012, 1000)
			, new Transaction(raoul, 2011, 400)
			, new Transaction(mario, 2012, 700)
			, new Transaction(mario, 2012, 700)		
			, new Transaction(alan, 2012, 950));
		
		// 1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오른차순으로 정리하시오.
		System.out.println("1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오른차순으로 정리하시오.");
		transactions.stream()
			.filter((tran) -> tran.getYear() == 2011)
			.sorted((tran1, tran2) -> Integer.compare(tran1.getValue(), tran2.getValue()))
			.collect(Collectors.toList())
			.stream().forEach(tran -> System.out.println(tran.toString()) );
		
		System.out.println("[풀이] 1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오른차순으로 정리하시오.");
		transactions.stream()
			.filter((tran) -> tran.getYear() == 2011)
			.sorted(Comparator.comparing(Transaction::getValue))
			.collect(Collectors.toList())
			.stream().forEach(tran -> System.out.println(tran.toString()) );
		
		// 2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
		System.out.println("\n2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.");
		transactions.stream()			
			.map((tran) -> tran.getTrader().getCity())
			.distinct()
			.collect(Collectors.toList())
			.stream().forEach(tran -> System.out.println(tran.toString()) );
		
		System.out.println("[풀이] 2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.");
		transactions.stream()
			.map(tran -> tran.getTrader().getCity())			
			.collect(Collectors.toSet())
			.stream().forEach(tran -> System.out.println(tran.toString()) );

		// 3. 케임브리지(cambridge)에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.
		System.out.println("\n3. 케임브리지(cambridge)에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.");
		transactions.stream()
			.filter(transaction -> "cambridge".equals(transaction.getTrader().getCity().toLowerCase()) )
			.sorted((o1, o2) -> o1.getTrader().getName().compareTo(o2.getTrader().getName()))
			.map(tranName -> tranName.getTrader().getName())
			.distinct()
			.map(traderName -> {
				return transactions.stream()
					.filter(tranNameFilter -> tranNameFilter.getTrader().getName().equals(traderName))
					.sorted(Comparator.comparing(Transaction::getYear).reversed())
					.limit(1)
					.collect(Collectors.toList());
			})			
			.collect(Collectors.toList())
			.stream().forEach(System.out::println);
		
		System.out.println("[풀이] 3. 케임브리지(cambridge)에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.");
		transactions.stream()
			.map(Transaction::getTrader)
			.filter(trader -> "Cambridge".equals(trader.getCity()))
			.distinct()
			.sorted(Comparator.comparing(Trader::getName))
			.collect(Collectors.toList())
			.stream().forEach(tran -> System.out.println(tran.toString()) );

		// 4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.
		System.out.println("\n4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.");
		transactions.stream()
			.map(tran -> tran.getTrader().getName())
			.distinct()
			.sorted((o1, o2) -> o1.compareTo(o2))
			.map(traderName -> {
				return transactions.stream()
					.filter(tranFilter -> traderName.equals(tranFilter.getTrader().getName()))
					.sorted(Comparator.comparing(Transaction::getYear).reversed())
					.limit(1)
					.collect(Collectors.toList());
			})
			.collect(Collectors.toList())
			.forEach(System.out::println);
		
		System.out.println("[풀이-1] 4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.");
		System.out.println(transactions.stream()
			.map(tran -> tran.getTrader().getName())
			.distinct()
			.sorted()
			.reduce("", (n1, n2) -> n1 + n2) );
		/*
		 * 각 반복 과정에서 모든 문자열을 반복적으로 연결해서 새로운 문자열 객체를 만든다.
		 * 따라서 위 코드는 효율성이 부족하다. 이를 해결하기 위해 joining()을 이용하면 된다.
		 */
		System.out.println("[풀이-2] 4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.");
		System.out.println(transactions.stream()
			.map(tran -> tran.getTrader().getName())
			.distinct()
			.sorted()
			.collect(Collectors.joining()) );		
		
		// 5. 밀라노(milan)에서 거래자가 있는가?
		System.out.println("\n5. 밀라노(milan)에서 거래자가 있는가?");
		System.out.println(transactions.stream()
			.anyMatch((tran) -> "milan".equals(tran.getTrader().getCity().toLowerCase()) ));
		
		System.out.println("[풀이] 5. 밀라노(milan)에서 거래자가 있는가?");
		System.out.println(transactions.stream()
			.anyMatch((tran) -> "Milan".equals(tran.getTrader().getCity()) ));
		
		// 6. 케임브리지에 거주하는 모든 거래자의 모든 트랜잭션값을 출력하시오.
		System.out.println("\n6. 케임브리지에 거주하는 모든 거래자의 모든 트랜잭션값을 출력하시오.");
		System.out.println(transactions.stream()
			.filter(tran -> "cambridge".equals(tran.getTrader().getCity().toLowerCase()) )
			.map(tran -> tran.getValue())
			.reduce(0, (tran1, tran2) -> tran1 + tran2));

		System.out.println("[풀이] 6. 케임브리지에 거주하는 모든 거래자의 모든 트랜잭션값을 출력하시오.");
		System.out.println(transactions.stream()
			.filter(tran -> "Cambridge".equals(tran.getTrader().getCity()))
			.map(Transaction::getValue)
			.reduce(0, Integer::sum));
		
		// 7. 전체 트랜잭션 중 최대값은 얼마인가?
		System.out.println("\n7. 전체 트랜잭션 중 최대값은 얼마인가?");
		System.out.println(transactions.stream()
			.map(tran -> tran.getValue())
			.reduce(Integer::max).get() );

		System.out.println("[풀이] 7. 전체 트랜잭션 중 최대값은 얼마인가?");
		System.out.println(transactions.stream()
			.map(Transaction::getValue)
			.reduce(Integer::max).get() );

		// 8. 전체 트랜잭션 중 최소값은 얼마인가?
		System.out.println("\n8. 전체 트랜잭션 중 최소값은 얼마인가?");
		System.out.println(transactions.stream()
			.map(tran -> tran.getValue())
			.reduce(Integer::min).get() );
		
		System.out.println("[풀이] 8. 전체 트랜잭션 중 최소값은 얼마인가?");
		System.out.println(transactions.stream()
			.reduce((t1, t2) -> 
				t1.getValue() < t2.getValue() ? t1 : t2
			).get() );		
	}
}
