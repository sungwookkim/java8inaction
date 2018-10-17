package Part1.Chapter2.Chapter2_2_2.Quiz.print.impl;

import Part1.Chapter2.Chapter2_2_2.Quiz.print.inter.ApplePrintPredicate;
import Part1.Chapter2.Chapter2_2_2.entity.AppleEntity;

public class AppleSimplePrint implements ApplePrintPredicate {

	@Override
	public String print(AppleEntity apple) {		
		return "An " + apple.getColor() + " apple of " + apple.getWeight() + "g";
	}

}
