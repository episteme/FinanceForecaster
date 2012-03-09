import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class Story {

	private int rank;
	private double sentiment;
	private String title;
	private int sites;
	private String link;
	private LinkedList<keyWord> keyWords;
	private Date timestamp;
	
	// Creates basic story - sentiment and keywords not added yet
	Story(int s, int o, String t, String l){
		rank = s/o;
		title = t;
		link = l;
		keyWords = new LinkedList<keyWord>();
		sentiment = 0;
		sites = s;
		timestamp = new Date();
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(timestamp);
	}
	
	// prints the keywords to a string
	public String printKeyWords() {
		String temp = "";
		for (final ListIterator<keyWord> listiter = keyWords.listIterator(); listiter.hasNext();)
		{
			keyWord word = listiter.next();
			temp += word.getWord() + ": " + word.getRel() + "; " + word.getSentiment() + " ";
		}
		return temp;
	}
	
	public String top5keyWords() {
		List castList = (List) keyWords;
		Collections.sort(castList);
		Collections.reverse(castList);
		String returnString = "";
		int j = 5;
		if (castList.size() < 5) {
			j = castList.size();
		}
		for (int i = 0; i < j; i++) {
			keyWord nextWord = ((keyWord) castList.get(i));
			returnString += nextWord.getWord() + "@" + nextWord.getSentiment() +";\n";
		}
		return returnString;
	}
	
	public void clearKeyWords() {
		keyWords = new LinkedList<keyWord>();
	}
	
	public LinkedList<keyWord> getList(){
		return keyWords;
	}
	
	public int getSites() {
		return sites;
	}

	public void setSites(int sites) {
		this.sites = sites;
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

	public void setSentiment(double sentiment) {
		this.sentiment = sentiment;
	}

	public void setRank(int r, int s) {
		this.rank = s/r;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	
	
}

//contains the keyword and its relevance
class keyWord implements Comparable<keyWord> {
	String word;
	Double relevance;
	Double sentiment;
	
		keyWord(String w, String r, String s) {
			word = w;
			relevance = Double.parseDouble(r);
			sentiment = Double.parseDouble(s);
		}
		
		public int compareTo(keyWord k) {
			return (this.relevance).compareTo(k.getRel());
		}
		
		public String getWord(){
			return word;
		}
		
		public double getSentiment() {
			return sentiment;
		}

		public double getRel(){
			return relevance;
		}
}
