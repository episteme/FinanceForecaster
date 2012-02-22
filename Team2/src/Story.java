import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
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
		for (final ListIterator<keyWord> listiter = keyWords.listIterator();listiter.hasNext();)
		{
			keyWord word = listiter.next();
			temp += word.getWord() + ": " + word.getRel() + "; ";
		}
		return temp;
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
