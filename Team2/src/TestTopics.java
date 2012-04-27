import java.util.Date;


public class TestTopics {
	
	public static void main(String[] args) {
		Article article1 = new Article("google.com", "google", new Date(System.currentTimeMillis() - 80 * 60 * 1000));
		Article article2 = new Article("yahoo.com", "yahoo news", new Date(System.currentTimeMillis() - 20 * 60 * 1000));
		Article article3 = new Article("askjeeves.com", "jeevesy boy", new Date(System.currentTimeMillis() - 15 * 60 * 1000));
		Article article4 = new Article("goatse.cx", "wide load", new Date(System.currentTimeMillis() - 10 * 60 * 1000));
		Article article5 = new Article("lemonparty.org", "hot orgy", new Date());
		Topic cum = new Topic(article1, 1);
		cum.addArticle(article2);
		cum.addArticle(article3);
		cum.addArticle(article4);
		cum.addArticle(article5);
		System.out.println(cum.artsLastHour());
		
	}

}
