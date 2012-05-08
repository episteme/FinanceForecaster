import java.util.Date;


public class TestTopics {
	
	public static void main(String[] args) {
		Article article1 = new Article("google.com", "google", new Date(System.currentTimeMillis() - 80 * 60 * 1000));
		Article article2 = new Article("yahoo.com", "yahoo news", new Date(System.currentTimeMillis() - 20 * 60 * 1000));
		Article article3 = new Article("askjeeves.com", "jeevesy boy", new Date(System.currentTimeMillis() - 15 * 60 * 1000));
		Article article4 = new Article("goat.cx", "wide load", new Date(System.currentTimeMillis() - 10 * 60 * 1000));
		Article article5 = new Article("lemon.org", "hobo", new Date());
		Topic cul = new Topic(article1, 1, 0);
		cul.addArticle(article2, 0);
		cul.addArticle(article3, 0);
		cul.addArticle(article4, 0);
		cul.addArticle(article5, 0);
		System.out.println(cul.artsLastHour());
		
	}

}
