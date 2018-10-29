package Part1.Chapter3.Chapter3_3_8.letter;

public class Letter {
	public static String addHeader(String text) {
		return "From Raoul, Mario and Alan : " + text;
	}
	
	public static String addFooter(String text) {
		return text + " Kind regards";
	}
	
	public static String checkSpelling(String text) {
		return text.replaceAll("labda", "lambda");
	}
}
