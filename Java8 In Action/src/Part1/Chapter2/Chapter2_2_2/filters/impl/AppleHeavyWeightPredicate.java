package Part1.Chapter2.Chapter2_2_2.filters.impl;

import Part1.Chapter2.Chapter2_2_2.entity.AppleEntity;
import Part1.Chapter2.Chapter2_2_2.filters.inter.ApplePredicate;

public class AppleHeavyWeightPredicate implements ApplePredicate {

	@Override
	public boolean test(AppleEntity appleEntity) {
		return appleEntity.getWeight() > 150;
	}

}
