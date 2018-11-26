package Part3.Chapter8.Chapter8_8_2.observer.impl;

import java.util.ArrayList;
import java.util.List;

import Part3.Chapter8.Chapter8_8_2.observer.inter.Observer;
import Part3.Chapter8.Chapter8_8_2.observer.inter.Subject;

public class Feed implements Subject {
	private final List<Observer> observers = new ArrayList<>();	
	
	@Override
	public void registerObserver(Observer o) {
		this.observers.add(o);
	}
	
	@Override
	public void notifyObservers(String tweet) {
		this.observers.forEach(o -> o.notify(tweet));		
	}
}
