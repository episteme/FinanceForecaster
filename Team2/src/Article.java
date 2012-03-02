import java.util.Date;


// An Article contains a link to an article
// and the date it was picked up
public class Article {
	
	private String URL;
	private Date date;
	private String title;
	
	public Article(String URL, String title, Date date) {
		this.URL = URL;
		this.date = date;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getURL() {
		return URL;
	}

	public Date getDate() {
		return date;
	}
	
	public void printMe() {
		System.out.println(title);
		System.out.println(URL);
	}

}
