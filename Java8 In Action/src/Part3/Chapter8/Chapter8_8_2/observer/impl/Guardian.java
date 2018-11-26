package Part3.Chapter8.Chapter8_8_2.observer.impl;

import Part3.Chapter8.Chapter8_8_2.observer.inter.Observer;

public class Guardian implements Observer {

	@Override
	public void notify(String tweet) {
		if(tweet != null && "queen".equals(tweet)) {
			System.out.println("Yet another news in London... " + tweet);
		}
	}

}
