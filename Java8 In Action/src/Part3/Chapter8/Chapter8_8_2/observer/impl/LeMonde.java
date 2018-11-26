package Part3.Chapter8.Chapter8_8_2.observer.impl;

import Part3.Chapter8.Chapter8_8_2.observer.inter.Observer;

public class LeMonde implements Observer {
	
	@Override
	public void notify(String tweet) {
		if(tweet != null && "wine".equals(tweet)) {
			System.out.println("Today cheese, wine and news! " + tweet);
		}
	}
	
}
