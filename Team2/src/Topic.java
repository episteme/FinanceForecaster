import java.util.HashMap;
import java.util.LinkedList;

// A Topic is the large concept of a story,
// it is a collection of relevant keywords,
// links to the relevant articles and the
// dates these articles were picked up
public class Topic {
	
	private HashMap<String, Double> words;
	private LinkedList<Article> articles;
	
	// Constructor, begins list with first article
	public Topic(HashMap<String, Double> words, Article article) {
		this.words = words;
		this.articles = new LinkedList<Article>();
		articles.push(article);
	}
	
	public void addArticle(Article article) {
		articles.push(article);
	}
	
	public void addWord(String s, Double d) {
		words.put(s, d); 
	}

	public HashMap<String, Double> getWords() {
		return words;
	}

	public LinkedList<Article> getArticles() {
		return articles;
	}
	
}