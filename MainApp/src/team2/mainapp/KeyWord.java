package team2.mainapp;

public class KeyWord {
	String word;
	double sentiment;

	KeyWord(String w, String s) {
		word = w;
		sentiment = Double.parseDouble(s);
	}
	
	public String getWord() {
		return word;
	}

	public double getSentiment() {
		return sentiment;
	}
}