package Part3.Chapter8.Chapter8_8_2.templateMethod.impl;

import Part3.Chapter8.Chapter8_8_2.templateMethod.abs.OnlineBanking;
import Part3.Chapter8.Chapter8_8_2.templateMethod.customer.Customer;

public class OnlineDepositBanking extends OnlineBanking {
	
	@Override
	public void makeCustomerHappy(Customer c) {
		System.out.println(c.getName() + "님 입금이 완료 되었습니다.");
	}
}
