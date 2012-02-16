import java.util.LinkedList;
import java.util.ListIterator;


public class Story {

	private int rank;
	private double sentiment;
	private String title;
	private String link;
	private LinkedList<keyWord> keyWords;
	
	// Creates basic story - sentiment and keywords not added yet
	Story(int sites, int order, String t, String l){
		rank = sites/order;
		title = t;
		link = l;
		keyWords = new LinkedList<keyWord>();
	}
	
	// prints the keywords to a string
	public String printKeyWords() {
		String temp = "";
		for (final ListIterator<keyWord> listiter = keyWords.listIterator();listiter.hasNext();)
		{
			keyWord word = listiter.next();
			temp += word.getWord() + " : " + word.getRel() + "\n";
		}
		return temp;
	}
	
	public LinkedList<keyWord> getList(){
		return keyWords;
	}
	
	public int getRank(){
		return rank;
	}
	
	public double getSentiment(){
		return sentiment;
	}
	
	public String getLink(){
		return link;
	}
	
	public String getTitle(){
		return title;
	}
	
}

//contains the keyword and its relevance
class keyWord {
	String word;
	double relevance;
	
		keyWord(String w, String r) {
			word = w;
			relevance = Double.parseDouble(r);
		}
		
		public String getWord(){
			return word;
		}
		
		public double getRel(){
			return relevance;
		}
}
