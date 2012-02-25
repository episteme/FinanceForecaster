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
	
	// Constructor, begins list with first article
	public Topic(HashMap<String, Double> words, Article article) {
		this.words = words;
		this.articles = new LinkedList<Article>();
		articles.push(article);
		numWords = 1;
	}
	
	public Topic(Article article) {
		this.articles = new LinkedList<Article>();
		articles.push(article);
		numWords = 0;
	}
	
	public void addArticle(Article article) {
		articles.push(article);
	}
	
	public void addWord(String s, Double d) {
		if (numWords == 0)
			words = new HashMap<String, Double>();
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
}