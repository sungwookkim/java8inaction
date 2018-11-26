package Part3.Chapter8.Chapter8_8_2.observer.impl;

import Part3.Chapter8.Chapter8_8_2.observer.inter.Observer;

public class NYTimes implements Observer {

	@Override
	public void notify(String tweet) {
		if(tweet != null && "money".equals(tweet)) {
			System.out.println("Breaking new in NY! " + tweet);
		}
	}

}
