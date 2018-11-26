package Part3.Chapter8.Chapter8_8_2.chain.impl;

import Part3.Chapter8.Chapter8_8_2.chain.abs.ProcessingObject;

public class SpellCheckerProcessing extends ProcessingObject<String>{

	@Override
	protected String handleWork(String input) {
		return input.replaceAll("labda", "lambda");				
	}

}
