import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

// A Topic is the large concept of a story,
// it is a collection of relevant keywords,
// links to the relevant articles and the
// dates these articles were picked up
public class Topic {
	
	private HashMap<String, Double> words;
	private LinkedList<Article> articles;
	private int numWords;
	private Date timestamp;
	private String recentTitle;
	
	// Constructor, begins list with first article
	public Topic(HashMap<String, Double> words, Article article) {
		this.words = words;
		this.articles = new LinkedList<Article>();
		articles.push(article);
		numWords = 1;
		timestamp = new Date();
		recentTitle = article.getTitle();
	}
	
	public String getRecentTitle() {
		return recentTitle;
	}

	public Topic(Article article) {
		this.articles = new LinkedList<Article>();
		articles.push(article);
		numWords = 0;
		timestamp = new Date();
	}
	
	// Add article only if new
	public void addArticle(Article article) {
	    Iterator<Article> iterator = articles.iterator();
	    boolean repeat = false;
	    while (iterator.hasNext()) {
	       if (article.getURL().compareTo(iterator.next().getURL()) == 0) {
	    	   repeat = true;
	    	   System.out.println("Repeat URL");
	    	   break;
	       }
	    }
	    if (!repeat)
	    	articles.push(article);
	    timestamp = new Date();
	    recentTitle = article.getTitle();
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void addWord(String s, Double d) {
		if (numWords == 0)
			words = new HashMap<String, Double>();
		if (words.containsKey(s)) {
			Double currentVal = words.get(s);
			words.remove(s);
			words.put(s, (currentVal + d) / 2.0);
		}
		else
			words.put(s, d);
		numWords++;
	}

	public HashMap<String, Double> getWords() {
		return words;
	}

	public LinkedList<Article> getArticles() {
		return articles;
	}
	
	public boolean containsWord(String s) {
		if (words.containsKey(s))
			return true;
		else
			return false;
	}
	
	// Displays all the keyword information
	public void printWords() {
	    Iterator<String> iterator = words.keySet().iterator();
	    while (iterator.hasNext()) {  
	       String key = iterator.next().toString();  
	       String value = words.get(key).toString(); 
	       System.out.println(key + " " + value);  
	    }  
	}

	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(timestamp);
	}
}