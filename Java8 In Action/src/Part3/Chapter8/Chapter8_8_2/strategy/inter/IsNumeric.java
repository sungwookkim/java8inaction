package Part3.Chapter8.Chapter8_8_2.strategy.inter;

public class IsNumeric implements ValidationStrategy {

	@Override
	public boolean execute(String s) {
		return s.matches("\\d+");
	}

}
