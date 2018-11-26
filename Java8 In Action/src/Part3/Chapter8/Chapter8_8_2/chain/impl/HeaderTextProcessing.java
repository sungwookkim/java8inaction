package Part3.Chapter8.Chapter8_8_2.chain.impl;

import Part3.Chapter8.Chapter8_8_2.chain.abs.ProcessingObject;

public class HeaderTextProcessing extends ProcessingObject<String>{

	@Override
	protected String handleWork(String input) {
		return "From Raoul. Mario and Alan : " + input;
	}

}
