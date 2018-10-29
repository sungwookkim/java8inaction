package Part1.Chapter3.Chapter3_3_7.impl;

import java.util.Comparator;

import Part1.Chapter3.Chapter3_3_7.entity.AppleEntity;

public class AppleEntityComparator implements Comparator<AppleEntity>{

	@Override
	public int compare(AppleEntity o1, AppleEntity o2) {
		return o1.getWeight().compareTo(o2.getWeight());
	}

}
