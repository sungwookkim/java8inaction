package Part1.Chapter2.Chapter2_2_2.Quiz.print.impl;

import Part1.Chapter2.Chapter2_2_2.Quiz.print.inter.ApplePrintPredicate;
import Part1.Chapter2.Chapter2_2_2.entity.AppleEntity;

public class AppleFancyPrint implements ApplePrintPredicate {
	int checkWeight = 0;
	
	public AppleFancyPrint(int checkWeight) {
		this.checkWeight = checkWeight;
	}
	
	@Override
	public String print(AppleEntity apple) {
		String printStr = apple.getWeight() > checkWeight ? "heavy" : "light";
		
		return "[Check Weight =" + this.checkWeight + ", Apple Weight = " + apple.getWeight() + "] " 
				+ "A " + printStr + " " + apple.getColor() + " apple";
	}

}
