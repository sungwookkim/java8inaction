package Part3.Chapter8.Chapter8_8_1.task;

public interface Task {
	public void execute();
	
	public static void doSomthing(Runnable r) {
		r.run();
	}
	
	public static void doSomthing(Task a) {
		a.execute();
	}
}

