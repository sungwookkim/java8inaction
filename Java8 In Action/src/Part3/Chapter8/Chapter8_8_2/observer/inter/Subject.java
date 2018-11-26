package Part3.Chapter8.Chapter8_8_2.observer.inter;

public interface Subject {
	void registerObserver(Observer o);
	void notifyObservers(String tweet);
}
