package Part1.Chapter2.Chapter2_2_2.filters.impl;

import Part1.Chapter2.Chapter2_2_2.entity.AppleEntity;
import Part1.Chapter2.Chapter2_2_2.filters.inter.ApplePredicate;

public class AppleHeavyColorPredicate implements ApplePredicate {

	@Override
	public boolean test(AppleEntity appleEntity) {
		return "green".equals(appleEntity.getColor());
	}

}
