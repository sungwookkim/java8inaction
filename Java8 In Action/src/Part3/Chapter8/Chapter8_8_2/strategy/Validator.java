package Part3.Chapter8.Chapter8_8_2.strategy;

import Part3.Chapter8.Chapter8_8_2.strategy.inter.ValidationStrategy;

public class Validator {
	private final ValidationStrategy strategy;
	
	public Validator(ValidationStrategy strategy) {
		this.strategy = strategy;
	}
	
	public boolean validate(String s) {
		return this.strategy.execute(s);
	}
}
