package Part3.Chapter8.Chapter8_8_2.templateMethod.impl;

import Part3.Chapter8.Chapter8_8_2.templateMethod.abs.OnlineBanking;
import Part3.Chapter8.Chapter8_8_2.templateMethod.customer.Customer;

public class OnlineWithdrawBanking extends OnlineBanking  {

	@Override
	public void makeCustomerHappy(Customer c) {
		System.out.println(c.getId() + " 아이디를 가지신 " + c.getName() + "님 출금이 완료 되었습니다.");
	}

}
